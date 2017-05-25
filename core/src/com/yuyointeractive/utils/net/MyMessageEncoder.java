package com.yuyointeractive.utils.net;

import java.io.IOException;
import java.io.OutputStream;

public class MyMessageEncoder {
  public void encoder(Object msg, OutputStream out) {
    if (((MyByteBuffer) msg).getByteArrary() instanceof byte[]) {
      byte[] data = ((MyByteBuffer) msg).getByteArrary();
      try {
        out.write(data);
        out.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("msg不是byte[]，是=" + msg.getClass());
    }
  }
}