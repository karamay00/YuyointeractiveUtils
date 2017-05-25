package com.yuyointeractive.utils.net;

public interface MyResponseListener {
  public void onActive();
  public void recevieMessage(int type, Object object);
  public void onBreak();
  public void onFileBytes(byte[] data, String fileName, float progress);
}
