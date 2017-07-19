package com.yuyointeractive.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

public abstract class MyGame extends Game {
    public static ShapeRenderer shapeRenderer = null;
    public static MyCommonAssets commonAssets = null;
    public static float screenScale = 1;
    public static float offX;
    public static float offY;
    public static short worldWidth;
    public static short worldHeight;
    public static boolean isChinese;
    public static boolean isMusicPlay = true;
    public static boolean isSoundPlay = true;
    public static boolean isLoadingFinish = false;
    public static Group loadingScreen = null;
    public static AssetManager assetManager = null;
    public static Preferences prefs = null;
    public static MyCrossPlatformHandler handler = null;
    public static Map<String, MyScreen> screens = null;
    public static float soundVolume = 0.5f;
    public static float musicVolume = 0.5f;
    public static boolean isPreloadSound = true;
    // protected NativeFontListener fontListener;
    // private int fontSize = 30;
    public static Map<String, NativeFont> fonts = new HashMap<String, NativeFont>();
    public static final String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*";

    public MyGame(MyCrossPlatformHandler handler) {
	MyGame.handler = handler;
	isChinese = Locale.getDefault().getLanguage().equals("zh");
	screens = new HashMap<String, MyScreen>();
	setWorldSize();
    }

    @Override
    public void create() {
	// Gdx.graphics.setContinuousRendering(false);
	// Gdx.graphics.requestRendering();
	boolean isCutHeight;
	// screen长宽，剪裁过但未缩放的尺寸
	float screenWidth;
	float screenHeight;
	isCutHeight = worldHeight * Gdx.graphics.getWidth() > worldWidth * Gdx.graphics.getHeight() ? true : false;
	if (isCutHeight) {
	    screenWidth = worldWidth;
	    screenHeight = (worldWidth * Gdx.graphics.getHeight()) / Gdx.graphics.getWidth();
	    screenScale = Gdx.graphics.getWidth() / screenWidth;
	    offX = 0;
	    offY = (worldHeight - screenHeight) / 2;
	} else {
	    screenHeight = worldHeight;
	    screenWidth = (worldHeight * Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();
	    screenScale = Gdx.graphics.getHeight() / screenHeight;
	    offX = (worldWidth - screenWidth) / 2;
	    offY = 0;
	}
	assetManager = new AssetManager();
	prefs = Gdx.app.getPreferences("LibGdxPreferences");
	isMusicPlay = true;
	isSoundPlay = true;
	isLoadingFinish = false;
	assignLoadingScreen();
	setStartScreen();
	NativeFont font = new NativeFont(new NativeFontPaint(25));
	font.appendText(DEFAULT_CHARS);
	MyGame.fonts.put("font", font);
	NativeFont font20 = new NativeFont(new NativeFontPaint(20));
	font20.appendText(DEFAULT_CHARS);
	fonts.put("font20", font20);
    }

    public static NativeFont getDefaultFont() {
	return fonts.get("font");
    }

    public static NativeFont getSmallFont() {
	return fonts.get("font20");
    }

    protected abstract void assignLoadingScreen();

    protected abstract void setWorldSize();

    public abstract void createScreen(String screenName);

    protected void setStartScreen() {
	createScreen("start");
	super.setScreen(screens.get("start"));
    }

    @Override
    public void dispose() {
	super.dispose();

	if (commonAssets != null) {
	    commonAssets.dispose();
	}
	assetManager.dispose();
	loadingScreen = null;
	for (MyScreen screen : screens.values()) {
	    screen.dispose();
	}
	screens.clear();
	if (shapeRenderer != null) {
	    shapeRenderer.dispose();
	}
	Gdx.app.exit();
    }
}