package com.yuyointeractive.utils.net.codec;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;
import com.yuyointeractive.utils.net.MyByteBuffer;
import com.yuyointeractive.utils.net.ResponseListener;
import com.yuyointeractive.utils.net.SocketClient;

public class MessageDecoder {
  private String AESPwd;
  int type;
  int length;
  byte[] buffer;
  boolean hasContinuetReceive = false;
  public String decoder(final ResponseListener listener, InputStream input, RSAPublicKey key) throws Exception {
    this.READ_TIMEOUT_COUNT = 0;
    this.target = 0;
    int inputAvailable = input.available();
    //System.out.println("input.available()=" + input.available());
    //System.out.println("hasContinuetReceive=" + hasContinuetReceive);
    if (hasContinuetReceive) {
      byte[] bufferTemp = new byte[buffer.length + inputAvailable];
      for (int i = 0; i < buffer.length; i++) {
        bufferTemp[i] = buffer[i];
      }
      for (int i = 0; i < inputAvailable; i++) {
        bufferTemp[buffer.length + i] = (byte) input.read();
      }
      buffer = null;
      buffer = bufferTemp;
      int bufferLength = bufferTemp.length;
      if (bufferLength < 4) {
        System.out.println("小于最小4字节肯定不全");
        hasContinuetReceive = true;
        buffer = bufferTemp;
        return AESPwd;
      }
      length = getLength();
      if (bufferLength == 4) {
        if (length == 0) {
          //System.out.println("注意心跳包判断");
          hasContinuetReceive = false;
          receiveMsg(listener, length);
        } else {
          hasContinuetReceive = true;
          buffer = bufferTemp;// 这里是因为getLength()时把buffer截掉了一截子，但是发送丢包了，所以就整体缓存下来跟后面收到的一起解析，所以在这里要把截掉的部分恢复
        }
        return AESPwd;
      }
      while (bufferLength - 4 > length) {
        receiveMsg(listener, length);
        bufferLength = bufferLength - 4 - length;
        length = getLength();
      }
      if (bufferLength - 4 == length) {
        receiveMsg(listener, length);
        hasContinuetReceive = false;
        return AESPwd;
      }
      if (bufferLength - 4 < length) {
        hasContinuetReceive = true;
        buffer = bufferTemp;// 此时还没读取type，防止太短，连type也不够读取出来，都先暂存起来备用
        return AESPwd;
      }
    } else {
      if (inputAvailable < 4) {
        System.out.println("小于最小4字节肯定不全");
        hasContinuetReceive = true;
        buffer = getBuffer(input, inputAvailable);
        return AESPwd;
      }
      length = getLength(input);
      if (inputAvailable == 4) {
        if (length == 0) {
          //System.out.println("注意心跳包判断");
          hasContinuetReceive = false;
          receiveMsg(listener, input, 0);
        } else {
          hasContinuetReceive = true;
          buffer = getBuffer(input, inputAvailable);
        }
        return AESPwd;
      }
      while (inputAvailable - 4 > length) {
        receiveMsg(listener, input, length);
        inputAvailable = inputAvailable - 4 - length;
        length = getLength(input);
      }
      if (inputAvailable - 4 == length) {
        receiveMsg(listener, input, length);
        hasContinuetReceive = false;
        return AESPwd;
      }
      if (inputAvailable - 4 < length) {
        buffer = getBuffer(input, inputAvailable - 4);// 此时还没读取type，防止太短，连type也不够读取出来，都先暂存起来备用
        hasContinuetReceive = true;
      }
    }
    return AESPwd;
  }
  private void receiveMsg(ResponseListener listener, InputStream input, int length) throws IOException {
    type = getType(input);
    buffer = getBuffer(input, length);
    if (listener != null) {
     // System.out.println("0receiveType=" + type);
      listener.recevieMessage(type, buffer);
    }
  }
  private void receiveMsg(ResponseListener listener, int length) throws IOException {
    type = getType();
    if (listener != null) {
    //  System.out.println("1receiveType=" + type);
      listener.recevieMessage(type, getBuffer(length));
    }
  }
  private int getType(InputStream input) throws IOException {
    byte[] ts = new byte[2];
    for (int i = 0; i < 2; i++) {
      ts[i] = (byte) input.read();
    }
    return MyByteBuffer.readShort(ts);
  }
  private int getLength(InputStream input) throws IOException {
    byte[] ls = new byte[2];
    for (int i = 0; i < 2; i++) {
      ls[i] = (byte) input.read();
    }
    return MyByteBuffer.readShort(ls);
  }
  private byte[] getBuffer(InputStream input, int length) throws IOException {
    byte[] buf = new byte[length];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = (byte) input.read();
    }
    return buf;
  }
  private byte[] getBuffer(int length) {
    byte[] buf = new byte[length];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = buffer[i];
    }
    byte[] bufferTemp = new byte[buffer.length - length];
    for (int i = length; i < bufferTemp.length; i++) {
      bufferTemp[i - length] = buffer[i];
    }
    buffer = null;
    buffer = bufferTemp;
    return buf;
  }
  private int getLength() {
    byte[] bufferTemp = new byte[buffer.length - 2];
    for (int i = 2; i < bufferTemp.length; i++) {
      bufferTemp[i - 2] = buffer[i];
    }
    byte[] ls = new byte[2];
    for (int i = 0; i < 2; i++) {
      ls[i] = buffer[i];
    }
    buffer = null;
    buffer = bufferTemp;
    return MyByteBuffer.readShort(ls);
  }
  private int getType() {
    byte[] bufferTemp = new byte[buffer.length - 2];
    for (int i = 2; i < bufferTemp.length; i++) {
      bufferTemp[i - 2] = buffer[i];
    }
    byte[] ts = new byte[2];
    for (int i = 0; i < 2; i++) {
      ts[i] = buffer[i];
    }
    buffer = null;
    buffer = bufferTemp;
    return MyByteBuffer.readShort(ts);
  }
  private int READ_TIMEOUT_COUNT;
  private int target;
  public void polling(SocketClient sc) {
    target += SocketClient.THREAD_SLEEP_TIME;
    if (target / 1000 > SocketClient.READ_TIMEOUT) {
      this.target = 0;
      if (READ_TIMEOUT_COUNT++ > 3) {
        READ_TIMEOUT_COUNT = 0;
        // 读超时3次断开连接
        System.out.println("读超时3次断开连接");
        sc.close();
      }
    }
  }
}