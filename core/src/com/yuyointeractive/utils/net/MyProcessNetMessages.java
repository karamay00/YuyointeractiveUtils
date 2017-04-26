package com.yuyointeractive.utils.net;

import com.badlogic.gdx.Screen;
import com.yuyointeractive.utils.net.MyByteBuffer;
import com.yuyointeractive.utils.net.MyResponseListener;

public abstract class MyProcessNetMessages implements MyResponseListener {
  protected Screen screen;
  // protected MyByteBuffer myByteBuffer;
  protected MySocketClient socketClient;
  public void setScreen(Screen screen) {
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
  abstract public void sendMessage(int type, Object[] messages);
  @Override
  abstract public void recevieMessage(int type, byte[] bytes);
  abstract public void disconnect();
}