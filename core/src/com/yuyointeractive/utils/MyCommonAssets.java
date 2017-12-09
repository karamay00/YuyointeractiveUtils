package com.yuyointeractive.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
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

	public void assign() {
		assetManager.finishLoading();

		for (Texture texture : assetManager.getAll(Texture.class, new Array<Texture>())) {
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		for (BitmapFont bitmapFont : assetManager.getAll(BitmapFont.class, new Array<BitmapFont>())) {
			bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public void dispose() {
		assetManager.dispose();
	}
}
