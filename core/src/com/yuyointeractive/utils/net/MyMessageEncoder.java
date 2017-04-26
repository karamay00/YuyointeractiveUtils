package com.yuyointeractive.utils.net;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by fenghuaxz on 2016/11/26.
 */
public class MyMessageEncoder {
  // private MyByteBuffer myByteBuffer = new MyByteBuffer();
//  public static final int TYPE_HEARBEAT_MESSAGE = 0;
//  public static final int TYPE_TEXT_MESSAGE = 1;
//  public static final int TYPE_BYTES_MESSAGE = 2;
//  public static final int TYPE_FILEBLOCK_MESSAGE = 3;
//  public static final int TYPE_RSA_ENCRYPT_OF_AES_PWD = 4;
  // public static byte[] Int2Bytes(int intValue) {
  // byte[] result = new byte[4];
  // result[0] = (byte) ((intValue & 0xFF000000) >> 24);
  // result[1] = (byte) ((intValue & 0x00FF0000) >> 16);
  // result[2] = (byte) ((intValue & 0x0000FF00) >> 8);
  // result[3] = (byte) ((intValue & 0x000000FF));
  // return result;
  // }
  //public void encoder(Object msg, OutputStream out, String AESPwd) {
    public void encoder(Object msg, OutputStream out) {
    
    /*
     * if (msg instanceof String) { if (msg.equals("ping")) { String account =
     * "xvzsdf"; String authorizationCode = "sftgdd"; byte sex = 0; String
     * nikeName = "nihaonihao"; // float temp = MathUtils.random() * 20161108 /
     * 2; // int r = (int) temp; // int a = 20161108 / r; // int b = 20161108 %
     * r; // byte c = 1; // MyByteBuffer bufTemp = new MyByteBuffer(512); //
     * bufTemp.writeUTF(account); // bufTemp.writeUTF(authorizationCode); //
     * bufTemp.writeBye(sex); // bufTemp.writeUTF(nikeName); //
     * bufTemp.writeInt(r); // bufTemp.writeInt(a); // bufTemp.writeInt(b); //
     * bufTemp.writeBye(c); // byte[] buf = new byte[6 + bufTemp.postion]; //
     * buf[0] = (byte) (1000 >> 8); // buf[1] = (byte) (1000 >> 0); // buf[2] =
     * (byte) ((buf.length - 4) >> 8); // buf[3] = (byte) ((buf.length - 4) >>
     * 0); // buf[4] = (byte) (1001 >> 8); // buf[5] = (byte) (1001 >> 0); //
     * for (int i = 0; i < bufTemp.postion; i++) { // buf[6 + i] =
     * bufTemp.bytes[i]; // }
     * 
     * //System.out.println("MyGame.authorizationCode="+MyGame.authorizationCode
     * );
     * 
     * myByteBuffer.beginWrite(1000); myByteBuffer.writeUTF("ddz22"); //
     * myByteBuffer.writeUTF(MyGame.authorizationCode);
     * myByteBuffer.writeUTF("authorization"); myByteBuffer.writeBye((byte)1);
     * myByteBuffer.writeUTF("??QOotECzQ"); myByteBuffer.endWrite();
     * 
     * 
     * out.write(myByteBuffer.getByteArrary()); out.flush();
     * System.out.println("axzasdasd12346549879"); return; } }
     */
    // System.out.println("LOOOOOOOOOOOOO");
    if (((MyByteBuffer) msg).getByteArrary() instanceof byte[]) {
      // System.out.println("CCCCCCCCCCCCCCCCCCC");
      // System.out.println("ggggg;out="+out.toString());
      byte[] data = ((MyByteBuffer) msg).getByteArrary();
      // if (AESPwd != null) {
      // data = Base64.encode(AESTookit.Exec(AESPwd, AESTookit.ENCRYPT_MODEL,
      // data)).getBytes("utf-8");
      // }
      // System.out.println("vdata.length="+data.length);
      try {
        out.write(data);
        out.flush();
      } catch (IOException e) {
        // System.out.println("vvvvvvvvvvvv="+e.getMessage());
        e.printStackTrace();
      }
      // System.out.println("mmmmmmmmmmmmmmmmmmmmmmmm");
    } else {
      System.out.println("msg不是byte[]，是=" + msg.getClass());
    }
  }
//  private int target;
//  public void polling(SocketClient sc) {
//    target += SocketClient.THREAD_SLEEP_TIME;
//    if (target / 1000 > SocketClient.WRITE_TIMEOUT) {
//      target = 0;
//      Object[] msg = {(byte) 0};
//      Fight14.procNetMsg.sendMessage(0, msg);
//    }
//  }
}