package com.yuyointeractive.utils.net;

import com.badlogic.gdx.Screen;
import com.yuyointeractive.utils.MyScreen;
import com.yuyointeractive.utils.net.MyResponseListener;

public abstract class MyProcessNetMessages implements MyResponseListener {
  protected MyScreen screen;
  protected MySocketClient socketClient;
  public void setScreen(MyScreen screen) {
    this.screen = screen;
  }
  public Screen getScreen() {
    return this.screen;
  }
  @Override
  public void onActive() {
  }
  @Override
  public void onBreak() {
  }
  @Override
  public void onFileBytes(byte[] data, String fileName, float progress) {
  }
  abstract public void sendMessage(int type, Object object);
  @Override
  abstract public void recevieMessage(int type, Object object);
  abstract public void disconnect();
}