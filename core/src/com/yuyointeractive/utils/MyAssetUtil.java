package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MyAssetUtil {
  public static String getAssetsPath(String assetsPath) {
    return Gdx.app.getType() == ApplicationType.Desktop ? "./bin/" + assetsPath + "/" : assetsPath + "/";
    //return assetsPath + "/";
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
    for (FileHandle file : Gdx.files.internal(assetsPath + "sound/").list()) {
      if (Gdx.app.getType() == ApplicationType.Desktop) {
        if (file.extension().matches("mp3") || file.extension().matches("ogg") || file.extension().matches("wav")) {
          assetManager.load("" + file, Sound.class);
        }
      } else {
        assetManager.load("" + file, Sound.class);
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
  public static void playSound(AssetManager assetManager, String assetsPath, String fileName) {
    if (MyGame.isSoundPlay) {
      assetManager.get(getAssetsPath(assetsPath) + "sound/" + fileName + ".mp3", Sound.class).play();
    }
  }
  public static void playSound(AssetManager assetManager, String assetsPath, String fileName, float volume) {
    if (MyGame.isSoundPlay) {
      assetManager.get(getAssetsPath(assetsPath) + "sound/" + fileName + ".mp3", Sound.class).play(volume);
    }
  }
  public static void playMusic(AssetManager assetManager, String assetsPath, String fileName) {
    if (MyGame.isMusicPlay) {
      assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class).setLooping(true);
      assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class).play();
    }
  }
  public static void playMusic(AssetManager assetManager, String assetsPath, String fileName, float volume) {
    if (MyGame.isMusicPlay) {
      assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class).setVolume(volume);
      assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class).setLooping(true);
      assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class).play();
    }
  }
  public static Sound getSound(AssetManager assetManager, String assetsPath, String fileName) {
    return assetManager.get(getAssetsPath(assetsPath) + "sound/" + fileName + ".mp3", Sound.class);
  }
  public static Music getMusic(AssetManager assetManager, String assetsPath, String fileName) {
    return assetManager.get(getAssetsPath(assetsPath) + "music/" + fileName + ".mp3", Music.class);
  }
  public static TextureAtlas getTextureAtlas(AssetManager assetManager, String assetsPath, String fileName) {
    return assetManager.get(getAssetsPath(assetsPath) + "atlas/" + fileName + ".atlas", TextureAtlas.class);
  }
  public static BitmapFont getBitmapFont(AssetManager assetManager, String assetsPath, String fileName) {
    return assetManager.get(getAssetsPath(assetsPath) + "fnt/" + fileName + ".fnt", BitmapFont.class);
  }
  public static BitmapFont getBitmapFont(TextureAtlas atlas, String assetsPath, String fileName) {
    return new BitmapFont(Gdx.files.internal(getAssetsPath(assetsPath) + fileName + ".fnt"), atlas.findRegion(fileName));
  }
  /** fileName要带上.png或者.jpg之类的后缀 */
  public static Texture getTexture(AssetManager assetManager, String assetsPath, String fileName) {
    return assetManager.get(getAssetsPath(assetsPath) + "texture/" + fileName, Texture.class);
  }
  /** fileName要带上.png或者.jpg之类的后缀 */
  public static Image getImage(AssetManager assetManager, String assetsPath, String fileName) {
    return new Image(assetManager.get(getAssetsPath(assetsPath) + "texture/" + fileName, Texture.class));
  }
  /** fileName要带上.png或者.jpg之类的后缀 */
  public static Image getImage(AssetManager assetManager, String assetsPath, String fileName, boolean flipX, boolean flipY) {
    TextureRegion textureRegion = new TextureRegion(
        assetManager.get(getAssetsPath(assetsPath) + "texture/" + fileName, Texture.class));
    textureRegion.flip(flipX, flipY);
    return new Image(textureRegion);
  }
}
