package com.yuyointeractive.utils.net;

import java.io.UnsupportedEncodingException;

public final class MyByteBuffer {
  protected int seed = 1;
  int type;
  byte[] bytes;
  public int postion = 0;
  public void writeUTF(String string) {
    try {
      byte[] bytesTemp = string.getBytes("UTF-8");
      writeShort(bytesTemp.length);
      for (int i = 0; i < bytesTemp.length; i++) {
        bytes[postion + i] = bytesTemp[i];
      }
      postion = postion + bytesTemp.length;
      bytes[2] = (byte) ((postion - 4) >> 8);
      bytes[3] = (byte) ((postion - 4) >> 0);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  public void writeInt(int i) {
    bytes[postion + 0] = (byte) ((i & 0xFF000000) >> 24);
    bytes[postion + 1] = (byte) ((i & 0x00FF0000) >> 16);
    bytes[postion + 2] = (byte) ((i & 0x0000FF00) >> 8);
    bytes[postion + 3] = (byte) ((i & 0x000000FF));
    postion = postion + 4;
    bytes[2] = (byte) ((postion - 4) >> 8);
    bytes[3] = (byte) ((postion - 4) >> 0);
  }
  public void writeShort(int s) {
    bytes[postion + 0] = (byte) ((s & 0x0000FF00) >> 8);
    bytes[postion + 1] = (byte) ((s & 0x000000FF));
    postion = postion + 2;
    bytes[2] = (byte) ((postion - 4) >> 8);
    bytes[3] = (byte) ((postion - 4) >> 0);
  }
  public void writeByte(int b) {
    bytes[postion] = (byte) ((b & 0x000000FF));
    postion = postion + 1;
    bytes[2] = (byte) ((postion - 4) >> 8);
    bytes[3] = (byte) ((postion - 4) >> 0);
  }
  public void writeBoolean(boolean value) {
    writeByte((byte) (value ? 1 : 0));
  }
  private void writeCheckCode() {
    int num = (type * seed) ^ seed;
    num &= 0xFFFF;
    seed++;
    writeShort(num);
  }
  public void writeObjects(Object[] objects) {
    for (Object object : objects) {
      if (object instanceof Boolean) {
        writeBoolean((Boolean) object);
      } else if (object instanceof Byte) {
        writeByte((Byte) object);
      } else if (object instanceof Short) {
        writeShort((Short) object);
      } else if (object instanceof Integer) {
        writeInt((Integer) object);
      } else if (object instanceof Long) {
        // writeLong((Long)object);
      } else if (object instanceof String) {
        writeUTF((String) (object));
      }
    }
  }
  public void beginWrite(int type) {
    bytes = null;
    bytes = new byte[256];
    postion = 0;
    this.type = type;
    writeShort(type);
    writeShort(2);
    writeCheckCode();
  }
  public void endWriteOnlyByLogin(int versionCode) {
    float temp = ((float) Math.random()) * versionCode / 2;
    int r = (int) temp;
    int a = versionCode / r;
    int b = versionCode % r;
    byte c = 1;
    writeInt(r);
    writeInt(a);
    writeInt(b);
    writeByte(c);
  }
  public static int readShort(byte[] bytes) {
    int values[] = new int[2];
    values[0] = (int) bytes[0] << 8;
    values[1] = (int) bytes[0 + 1];
    if (values[0] < 0) {
      values[0] = values[0] & 0xFF00;
    }
    if (values[1] < 0) {
      values[1] = values[1] & 0xFF;
    }
    return (values[0] | values[1]);
  }
  public static int readInt(byte[] bytes) {
    int values[] = new int[4];
    values[0] = (int) bytes[0] << 24;
    values[1] = (int) bytes[0 + 1] << 16;
    values[2] = (int) bytes[0 + 2] << 8;
    values[3] = (int) bytes[0 + 3];
    if (values[0] < 0) {
      values[0] = values[0] & 0xFFFFFF00;
    }
    if (values[1] < 0) {
      values[1] = values[1] & 0xFFFF00;
    }
    if (values[2] < 0) {
      values[2] = values[2] & 0xFF00;
    }
    if (values[3] < 0) {
      values[3] = values[3] & 0xFF;
    }
    return (values[0] | values[1] | values[2] | values[3]);
  }
  public void beginRead(byte[] bytes) {
    this.bytes = null;
    this.bytes = bytes;
    postion = 0;
  }
  public String readString() {
    int strLen = readShort();
    byte[] buffStr = new byte[strLen];
    for (int j = 0; j < buffStr.length; j++) {
      buffStr[j] = bytes[postion + j];
    }
    postion = postion + strLen;
    return new String(buffStr);
  }
  public boolean readBoolean() {
    boolean value = (bytes[postion] != 0);
    postion = postion + 1;
    return value;
  }
  public int readByte() {
    int value;
    value = (int) bytes[postion];
    if (value < 0) {
      value = (int) bytes[postion] & 0xFF;
    }
    postion = postion + 1;
    return value;
  }
  public int readShort() {
    int values[] = new int[2];
    values[0] = (int) bytes[postion + 0] << 8;
    values[1] = (int) bytes[postion + 1];
    if (values[0] < 0) {
      values[0] = values[0] & 0xFF00;
    }
    if (values[1] < 0) {
      values[1] = values[1] & 0xFF;
    }
    postion = postion + 2;
    return (values[0] | values[1]);
  }
  public int readInt() {
    int values[] = new int[4];
    values[0] = (int) bytes[postion + 0] << 24;
    values[1] = (int) bytes[postion + 1] << 16;
    values[2] = (int) bytes[postion + 2] << 8;
    values[3] = (int) bytes[postion + 3];
    if (values[0] < 0) {
      values[0] = values[0] & 0xFF000000;
    }
    if (values[1] < 0) {
      values[1] = values[1] & 0xFF0000;
    }
    if (values[2] < 0) {
      values[2] = values[2] & 0xFF00;
    }
    if (values[3] < 0) {
      values[3] = values[3] & 0xFF;
    }
    postion = postion + 4;
    return (values[0] | values[1] | values[2] | values[3]);
  }
  public long readLong() {
    long values[] = new long[8];
    values[0] = (long) bytes[postion] << 56;
    values[1] = (long) bytes[postion + 1] << 48;
    values[2] = (long) bytes[postion + 2] << 40;
    values[3] = (long) bytes[postion + 3] << 32;
    values[4] = (long) bytes[postion + 4] << 24;
    values[5] = (long) bytes[postion + 5] << 16;
    values[6] = (long) bytes[postion + 6] << 8;
    values[7] = (long) bytes[postion + 7];
    if (values[0] < 0) {
      values[0] = values[0] & 0xFF00000000000000l;
    }
    if (values[1] < 0) {
      values[1] = values[1] & 0xFF000000000000l;
    }
    if (values[2] < 0) {
      values[2] = values[2] & 0xFF0000000000l;
    }
    if (values[3] < 0) {
      values[3] = values[3] & 0xFF00000000l;
    }
    if (values[4] < 0) {
      values[4] = values[4] & 0xFF000000;
    }
    if (values[5] < 0) {
      values[5] = values[5] & 0xFF0000;
    }
    if (values[6] < 0) {
      values[6] = values[6] & 0xFF00;
    }
    if (values[7] < 0) {
      values[7] = values[7] & 0xFF;
    }
    postion = postion + 8;
    return (values[0] | values[1] | values[2] | values[3] | values[4] | values[5] | values[6] | values[7]);
  }
  public Object[] readObjects(Object[] objects) {
    for (Object object : objects) {
      if (object instanceof Boolean) {
        object = readBoolean();
      } else if (object instanceof Byte) {
        object = readByte();
      } else if (object instanceof Short) {
        object = readShort();
      } else if (object instanceof Integer) {
        object = readInt();
      } else if (object instanceof Long) {
        object = readLong();
      } else if (object instanceof String) {
        object = readString();
      }
    }
    return objects;
  }
  public byte[] getByteArrary() {
    byte[] bytesTemp = new byte[postion];
    for (int i = 0; i < postion; i++) {
      bytesTemp[i] = bytes[i];
    }
    return bytesTemp;
  }
}