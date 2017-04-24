package com.yuyointeractive.utils.net;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPublicKey;
import com.yuyointeractive.utils.net.codec.MessageDecoder;
import com.yuyointeractive.utils.net.codec.MessageEncoder;

/**
 * Created by fenghuaxz on 2016/11/26.
 */
public class SocketClient{

  private Host host;

  private ResponseListener listener;

  private boolean connected;

 // private DataInputStream input;
  private InputStream input;

  private OutputStream output;

  private MessageEncoder encoder;

  private MessageDecoder decoder;

  private String AESPwd;

  /**
   * 读超时(秒) 请与服务器配对
   */
  public static int READ_TIMEOUT=3;

  /**
   * 写超时(秒) 轻语服务器配对
   */
  public static int WRITE_TIMEOUT=3;

  /**
   * 连接处理线程休眠时间(毫秒)
   */
  public static int THREAD_SLEEP_TIME=10;



//  public SocketClient(final Host host,final ResponseListener listener){
  public SocketClient(final Host host){
      this.host=host;
      //this.listener=listener;
      setCoder(new MessageEncoder(),new MessageDecoder());
     // reConnect(null);

  }
  public void setResponseListener(final ResponseListener listener){
    this.listener=listener;
  }
  

  /**
   * 重连
   * @param msg 消息体
   */
  public void reConnect(final Object msg){
      this.connected=true;
      if (host!=null){
          new Thread(new Runnable() {
              @Override
              public void run() {
                  Socket socket=null;
                  try {
                    
                   // System.out.println("nnnuuuuu8888888nnnn555connected="+connected);
                      socket=new Socket(host.getHost(),host.getPort());
                      SocketClient.this.input=socket.getInputStream();
                      SocketClient.this.output=socket.getOutputStream();
                      
                      //System.out.println("SocketClient.reConnect(...).new Runnable() {...}.run().out="+output.toString());
                      
                      if (listener!=null)listener.onActive();
                      if (msg!=null)sendContent(msg);
                  } catch (IOException e) {
                      SocketClient.this.connected=false;
                      if (listener!=null)listener.onBreak();
                  }

                  while (connected){
                      try {
                          int length=input.available();
                          if (length!=0){
                              if (decoder!=null){
                                  SocketClient.this.AESPwd=decoder.decoder(listener,input,openSecurity());
                              }else throw new RuntimeException("MessageDecoder uninitialized!");
                          }
                          //encoder.polling(SocketClient.this);
                          //decoder.polling(SocketClient.this);
                          Thread.sleep(SocketClient.THREAD_SLEEP_TIME);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
                  try {
                      if (socket!=null)socket.close();
                      if (listener!=null)listener.onBreak();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }).start();
          return;
      }
      throw new RuntimeException("Host can not be null!");
  }

  /**
   * 发送消息
   * @param msg 消息体
   */
  public void sendContent(final Object msg){
  
 
      if (encoder!=null){
          if (connected){
          
              try {
              //System.out.println("encoder="+encoder);
                  encoder.encoder(msg,output,AESPwd);
                 // System.out.println("XXXXVSDAFASDAWEWE");
              } catch (Exception e) {
                  close();
              }
              return;
          }
          if (listener!=null)listener.onBreak();
          return;
      }
      throw new RuntimeException("MessageEncoder uninitialized!");
  }

  /**
   * 设置编/解码器
   * @param encoder 编码器
   * @param decoder 解码器
   * @param <T>
   * @param <V>
   */
  public <T extends MessageEncoder,V extends MessageDecoder> void setCoder(T encoder,V decoder){
      this.encoder=encoder;
      this.decoder=decoder;
  }

  /**
   * 断开连接
   */
  public void close(){
      this.connected=false;
      try {
          if (input!=null)input.close();
          if (output!=null)output.close();
      }catch (Exception e){
          e.printStackTrace();
      }
  }

  /**
   * 是否连接
   * @return
   */
  public boolean isConnected() {
      return connected;
  }

  /**
   * 请返回一个RSA安全实例 用于开启加密
   * @return
   */
  public RSAPublicKey openSecurity(){
      return null;
  }

  public static class Host{

      private String host;

      private int port;

      public Host(final String host,final int port){
          this.host=host;
          this.port=port;
      }

      public String getHost() {
          return host;
      }

      public int getPort() {
          return port;
      }
  }
}
