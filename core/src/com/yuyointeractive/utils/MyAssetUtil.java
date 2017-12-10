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
					// ogg和wav等其他音效格式先不做读取
					// ||file.extension().matches("ogg")||file.extension().matches("wav"))
					if (file.extension().matches("mp3")) {
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

	public static <T> T getAsset(String assetsPat, String fileName, Class<T> type) {
		if (assetsPat.equals("common")
				|| Gdx.files.getFileHandle(getAssetsPath("common") + fileName, FileType.Internal).exists()) {
			fileName = getAssetsPath("common") + fileName;
			if (MyGame.commonAssets.assetManager.isLoaded(fileName, type)) {
				return MyGame.commonAssets.assetManager.get(fileName, type);
			} else {
				MyGame.commonAssets.assetManager.load(fileName, type);
				MyGame.commonAssets.assetManager.finishLoadingAsset(fileName);
				return MyGame.commonAssets.assetManager.get(fileName, type);
			}
		} else {
			fileName = getAssetsPath(assetsPat) + fileName;
			if (MyGame.assetManager.isLoaded(fileName, type)) {
				return MyGame.assetManager.get(fileName, type);
			} else {
				MyGame.assetManager.load(fileName, type);
				MyGame.assetManager.finishLoadingAsset(fileName);
				return MyGame.assetManager.get(fileName, type);
			}
		}
	}

	public static void playMusic(String assetsPath, String mp3FileName) {
		if (MyGame.isMusicPlay) {
			Music music = getMusic(assetsPath, mp3FileName);
			music.setVolume(MyGame.musicVolume);
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
		return getAsset(assetsPath, "sound/" + mp3FileName + ".mp3", Sound.class);
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
		return getAsset(assetsPath, "music/" + mp3FileName + ".mp3", Music.class);
	}

	public static TextureAtlas getTextureAtlas(String assetsPath, String atlasFileName) {
		return getAsset(assetsPath, "atlas/" + atlasFileName + ".atlas", TextureAtlas.class);
	}

	public static TextureAtlas getTextureAtlas(String assetsPath) {
		return getAsset(assetsPath, "atlas/" + assetsPath + ".atlas", TextureAtlas.class);
	}

	public static BitmapFont getBitmapFont(String assetsPath, String fntFileName) {
		return getAsset(assetsPath, "fnt/" + fntFileName + ".fnt", BitmapFont.class);
	}

	public static BitmapFont getBitmapFont(TextureAtlas atlas, String assetsPath, String fntFileName) {
		return new BitmapFont(Gdx.files.internal(getAssetsPath(assetsPath) + fntFileName + ".fnt"),
				atlas.findRegion(fntFileName));
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static Texture getTexture(String assetsPath, String textureFileName) {
		if (textureFileName.contains(".")) {
			return getAsset(assetsPath, "texture/" + textureFileName, Texture.class);
		} else {
			return getAsset(assetsPath, "texture/" + textureFileName + ".png", Texture.class);
		}
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static TextureRegion getTextureRegion(String assetsPath, String textureFileName) {
		if (Gdx.files.getFileHandle(getAssetsPath(assetsPath) + "texture/" + textureFileName
				+ (textureFileName.contains(".") ? "" : ".png"), FileType.Internal).exists()) {
			return new TextureRegion(getTexture(assetsPath, textureFileName));
		} else if (Gdx.files.getFileHandle(
				getAssetsPath("common") + "texture/" + textureFileName + (textureFileName.contains(".") ? "" : ".png"),
				FileType.Internal).exists()) {
			return new TextureRegion(getTexture("common", textureFileName));
		} else {
			TextureRegion textureRegion = getTextureAtlas(assetsPath).findRegion(textureFileName);
			if (textureRegion != null) {
				return textureRegion;
			} else {
				return getTextureAtlas("common").findRegion(textureFileName);
			}
		}
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static Drawable getDrawable(String assetsPath, String textureFileName) {
		return new TextureRegionDrawable(getTextureRegion(assetsPath, textureFileName));
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static Image getImage(String assetsPath, String textureFileName) {
		return new Image(getTextureRegion(assetsPath, textureFileName));
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static TImage getTImage(String assetsPath, String textureFileName) {
		return new TImage(getTextureRegion(assetsPath, textureFileName));
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static Image getImage(String assetsPath, String atlasFileName, String textureFileName) {
		return new Image(getTextureAtlas(assetsPath, atlasFileName).findRegion(textureFileName));
	}

	/** 单张texture时如果是png不用加后缀名，其他要加.jpg之类的后缀 */
	public static TImage getTImage(String assetsPath, String atlasFileName, String textureFileName) {
		return new TImage(getTextureAtlas(assetsPath, atlasFileName).findRegion(textureFileName));
	}
}