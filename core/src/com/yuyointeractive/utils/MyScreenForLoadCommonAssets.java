package com.yuyointeractive.utils;

public class MyScreenForLoadCommonAssets extends MyScreen {
  public MyScreenForLoadCommonAssets(MyGame myGame, String name) {
    super(myGame, name);
  }
//  @Override
//  public void init(MyGame myGame, String name) {
//    super.init(myGame, name);
//    MyGame.commonAssets.isLoadingFinish = false;
//    MyGame.commonAssets.load();
//  }
  @Override
  public void assign() {
    if (MyGame.commonAssets.isLoadingFinish) {
      super.assign();
    }
  }
  @Override
  public void render(float delta) {
    super.render(delta);
    if (!MyGame.commonAssets.isLoadingFinish) {
      if (MyGame.commonAssets.assetManager.update()) {
        MyGame.commonAssets.assign();
        if (MyGame.isLoadingFinish) {
          super.assign();
        }
        MyGame.commonAssets.isLoadingFinish = true;
      }
    }
  }
}