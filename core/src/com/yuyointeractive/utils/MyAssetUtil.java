package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.yuyointeractive.view.TImage;

public class MyAssetUtil {
	public static String getAssetsPath(String assetsPath) {
		if (Gdx.app.getType() == ApplicationType.Desktop
				&& Gdx.files.getFileHandle("bin", FileType.Internal).exists()) {
			return "./bin/" + assetsPath + "/";
		} else {
			return assetsPath + "/";
		}
	}

	public static void loadAssets(AssetManager assetManager, String assetsPath) {
		assetsPath = getAssetsPath(assetsPath);
		if (MyGame.assetManager == assetManager) {
			MyGame.isLoadingFinish = false;
		}
		for (FileHandle file : Gdx.files.internal(assetsPath + "fnt/").list()) {
			if (file.extension().matches("fnt")) {
				assetManager.load("" + file, BitmapFont.class);
			}
		}
		for (FileHandle file : Gdx.files.internal(assetsPath + "atlas/").list()) {
			if (file.extension().matches("atlas")) {
				assetManager.load("" + file, TextureAtlas.class);
			}
		}
		if (MyGame.isPreloadSound) {
			for (FileHandle file : Gdx.files.internal(assetsPath + "sound/").list()) {
				if (Gdx.app.getType() == ApplicationType.Desktop) {
					if (file.extension().matches("mp3") || file.extension().matches("ogg")
							|| file.extension().matches("wav")) {
						assetManager.load("" + file, Sound.class);
					}
				} else {
					assetManager.load("" + file, Sound.class);
				}
			}
		}
		for (FileHandle file : Gdx.files.internal(assetsPath + "music/").list()) {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (file.extension().matches("mp3")) {
					assetManager.load("" + file, Music.class);
				}
			} else {
				assetManager.load("" + file, Music.class);
			}
		}
		for (FileHandle file : Gdx.files.internal(assetsPath + "texture/").list()) {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (file.extension().matches("jpg") || file.extension().matches("png")) {
					assetManager.load("" + file, Texture.class);
				}
			} else {
				assetManager.load("" + file, Texture.class);
			}
		}
	}

	public static <T> T getAsset(String assetsPath, String fileName, Class<T> type) {
		switch (type.getClass().getSimpleName()) {
		case "TextureAtlas":
			fileName = "atlas/" + fileName + ".atlas";
			break;
		case "BitmapFont":
			fileName = "fnt/" + fileName + ".fnt";
			break;
		case "Texture":
			fileName = "texture/" + fileName + ".png";
			break;
		case "Sound":
			fileName = "sound/" + fileName + ".mp3";
			break;
		case "Music":
			fileName = "music/" + fileName + ".mp3";
			break;
		}
		assetsPath = getAssetsPath(assetsPath) + fileName;
		if (Gdx.files.getFileHandle(assetsPath, FileType.Internal).exists()) {
			if (MyGame.assetManager.isLoaded(assetsPath, type)) {
				return MyGame.assetManager.get(assetsPath, type);
			} else {
				MyGame.assetManager.load(assetsPath, type);
				MyGame.assetManager.finishLoadingAsset(assetsPath);
				return MyGame.assetManager.get(assetsPath, type);
			}
		} else {
			assetsPath = getAssetsPath("common") + fileName;
			if (MyGame.commonAssets.assetManager.isLoaded(assetsPath, Music.class)) {
				return MyGame.commonAssets.assetManager.get(assetsPath, type);
			} else {
				MyGame.commonAssets.assetManager.load(assetsPath, type);
				MyGame.commonAssets.assetManager.finishLoadingAsset(assetsPath);
				return MyGame.commonAssets.assetManager.get(assetsPath, type);
			}
		}
	}

	public static void playMusic(String assetsPath, String mp3FileName) {
		if (MyGame.isMusicPlay) {
			Music music = getMusic(assetsPath, mp3FileName);
			music.setLooping(true);
			music.play();
		}
	}

	public static void playMusic(String assetsPath, String mp3FileName, float volume) {
		if (MyGame.isMusicPlay) {
			Music music = getMusic(assetsPath, mp3FileName);
			music.setVolume(volume);
			music.setLooping(true);
			music.play();
		}
	}

	public static Sound getSound(String assetsPath, String mp3FileName) {
		return getAsset(assetsPath, mp3FileName, Sound.class);
	}

	public static void playSound(String assetsPath, String mp3FileName) {
		playSound(assetsPath, mp3FileName, MyGame.soundVolume);
	}

	public static void playSound(String assetsPath, String mp3FileName, float volume) {
		mp3FileName = getAssetsPath(assetsPath) + "sound/" + mp3FileName + ".mp3";
		if (Gdx.files.getFileHandle(mp3FileName, FileType.Internal).exists()) {
			if (MyGame.assetManager.isLoaded(mp3FileName, Sound.class)) {
				if (MyGame.isSoundPlay) {
					MyGame.assetManager.get(mp3FileName, Sound.class).play(volume);
				}
			} else {
				MyGame.assetManager.load(mp3FileName, Sound.class);
				MyGame.assetManager.finishLoadingAsset(mp3FileName);
				if (Gdx.app.getType() == ApplicationType.Android) {
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (MyGame.isSoundPlay) {
					MyGame.assetManager.get(mp3FileName, Sound.class).play(volume);
				}
			}
		} else {
			mp3FileName = getAssetsPath("common") + "sound/" + mp3FileName + ".mp3";
			if (MyGame.commonAssets.assetManager.isLoaded(mp3FileName, Sound.class)) {
				if (MyGame.isSoundPlay) {
					MyGame.commonAssets.assetManager.get(mp3FileName, Sound.class).play(volume);
				}
			} else {
				MyGame.commonAssets.assetManager.load(mp3FileName, Sound.class);
				MyGame.commonAssets.assetManager.finishLoadingAsset(mp3FileName);
				if (Gdx.app.getType() == ApplicationType.Android) {
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (MyGame.isSoundPlay) {
					MyGame.commonAssets.assetManager.get(mp3FileName, Sound.class).play(volume);
				}
			}
		}
	}

	public static Music getMusic(String assetsPath, String mp3FileName) {
		return getAsset(assetsPath, mp3FileName, Music.class);
	}

	public static TextureAtlas getTextureAtlas(String assetsPath, String atlasFileName) {
		return getAsset(assetsPath, atlasFileName, TextureAtlas.class);
	}

	public static TextureAtlas getTextureAtlas(String assetsPath) {
		return getAsset(assetsPath, assetsPath, TextureAtlas.class);
	}

	public static BitmapFont getBitmapFont(String assetsPath, String fntFileName) {
		return getAsset(assetsPath, fntFileName, BitmapFont.class);
	}

	public static BitmapFont getBitmapFont(TextureAtlas atlas, String assetsPath, String fntFileName) {
		return new BitmapFont(Gdx.files.internal(getAssetsPath(assetsPath) + fntFileName + ".fnt"),
				atlas.findRegion(fntFileName));
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static Texture getTexture(String assetsPath, String textureFileName) {
		return getAsset(assetsPath, textureFileName, Texture.class);
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static TextureRegion getTextureRegion(String assetsPath, String textureFileName) {
		try {
			return new TextureRegion(getTexture(assetsPath, textureFileName));
		} catch (Exception e) {
			if (textureFileName.contains(".png")) {
				textureFileName = textureFileName.substring(0, textureFileName.length() - 4);
			}
			return getTextureAtlas(assetsPath).findRegion(textureFileName);
		}
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static Drawable getDrawable(String assetsPath, String textureFileName) {
		return new TextureRegionDrawable(getTextureRegion(assetsPath, textureFileName));
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static Image getImage(String assetsPath, String textureFileName) {
		return new Image(getTextureRegion(assetsPath, textureFileName));
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static TImage getTImage(String assetsPath, String textureFileName) {
		return new TImage(getTextureRegion(assetsPath, textureFileName));
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static Image getImage(String assetsPath, String atlasFileName, String textureFileName) {
		if (textureFileName.contains(".png")) {
			textureFileName = textureFileName.substring(0, textureFileName.length() - 4);
		}
		return new Image(getTextureAtlas(assetsPath, atlasFileName).findRegion(textureFileName));
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public static TImage getTImage(String assetsPath, String atlasFileName, String textureFileName) {
		if (textureFileName.contains(".png")) {
			textureFileName = textureFileName.substring(0, textureFileName.length() - 4);
		}
		return new TImage(getTextureAtlas(assetsPath, atlasFileName).findRegion(textureFileName));
	}
}