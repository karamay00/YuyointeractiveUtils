package com.yuyointeractive.utils.net;

/**
 * Created by fenghuaxz on 2016/11/26.
 */
public interface ResponseListener {
  public void onActive();
  public void recevieMessage(int type, byte[] bytes);
  public void onBreak();
  public void onFileBytes(byte[] data, String fileName, float progress);
}
