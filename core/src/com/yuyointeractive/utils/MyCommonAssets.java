package com.yuyointeractive.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.yuyointeractive.utils.MyAssetUtil;

public class MyCommonAssets {
  public AssetManager assetManager;
  public boolean isLoadingFinish = false;
  public MyCommonAssets() {
    assetManager = new AssetManager();
  }
  public void load() {
    MyAssetUtil.loadAssets(assetManager, "common");
  }
  // public Sound getSound(String name){
  // return MyAssetUtil.getSound(assetManager, "common", name);
  // }
  public void assign() {
    assetManager.finishLoading();
  }
  public void dispose() {
    assetManager.dispose();
  }
}
