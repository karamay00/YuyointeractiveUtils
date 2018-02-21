package com.yuyointeractive.utils;

/**
 * 继承此类的screen须在assign方法最前面加入下面的判断
 * 
 * @Override public void assign() { if (!MyGame.commonAssets.isLoadingFinish) {
 *           return; } super.assign();}
 */
public class MyScreenForLoadCommonAssets extends MyScreen {
	public MyScreenForLoadCommonAssets(MyGame myGame, String name) {
		super(myGame, name);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (!MyGame.commonAssets.isLoadingFinish) {
			if (MyGame.commonAssets.assetManager.update()) {
				MyGame.commonAssets.assign();
				MyGame.commonAssets.isLoadingFinish = true;
				if (MyGame.isLoadingFinished) {
					assign();
				}
			}
		}
	}
}