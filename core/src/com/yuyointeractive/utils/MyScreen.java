package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yuyointeractive.view.MyCheckBox;
import com.yuyointeractive.view.TImage;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextArea;
import net.mwplay.nativefont.NativeTextField;

public class MyScreen extends Stage implements Screen {// ,GestureListener{
  public MyGame myGame;
  public MyScreen(MyGame myGame, String name) {
    this(myGame, name, new FillViewport(MyGame.worldWidth, MyGame.worldHeight), new PolygonSpriteBatch());
  }
  public MyScreen(MyGame myGame, String name, Batch batch) {
    this(myGame, name, new FillViewport(MyGame.worldWidth, MyGame.worldHeight), batch);
  }
  public MyScreen(MyGame myGame, String name, Viewport viewport) {
    super(viewport, new PolygonSpriteBatch());
    init(myGame, name);
  }
  public MyScreen(MyGame myGame, String name, Viewport viewport, Batch batch) {
    super(viewport, batch);
    init(myGame, name);
  }
  public void init(MyGame myGame, String name) {
    addActor(MyGame.loadingScreen);
    this.myGame = myGame;
    getRoot().setName(name);
    MyAssetUtil.loadAssets(MyGame.assetManager, name);
  }
  public void assign() {
    // InputMultiplexer multiplexer = new InputMultiplexer();
    // multiplexer.addProcessor(this);
    // multiplexer.addProcessor(new GestureDetector(this));
    // Gdx.input.setInputProcessor(multiplexer);
    Gdx.input.setInputProcessor(this);
    Gdx.input.setCatchBackKey(true);
    // Gdx.input.setCatchMenuKey(true);
    MyGame.assetManager.finishLoading();
    MyGame.loadingScreen.remove();
    for (Texture texture : MyGame.assetManager.getAll(Texture.class, new Array<Texture>())) {
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
    for (BitmapFont bitmapFont : MyGame.assetManager.getAll(BitmapFont.class, new Array<BitmapFont>())) {
      bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
  }
  public String getName() {
    return getRoot().getName();
  }
  public void changeScreen(String nextScreenName) {
    MyGame.screens.get(getRoot().getName()).dispose();
    MyGame.screens.remove(getRoot().getName());
    if (MyGame.screens.get(nextScreenName) == null) {
      myGame.createScreen(nextScreenName);
    }
    myGame.setScreen(MyGame.screens.get(nextScreenName));
  }
  public String getAssetsPath() {
    return MyAssetUtil.getAssetsPath(getRoot().getName());
  }
  public void playSound(String fileName) {
    MyAssetUtil.playSound(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public void playSound(String fileName, float volume) {
    MyAssetUtil.playSound(MyGame.assetManager, getRoot().getName(), fileName, volume);
  }
  public void playMusic(String fileName) {
    MyAssetUtil.playMusic(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public void playMusic(String fileName, float volume) {
    MyAssetUtil.playMusic(MyGame.assetManager, getRoot().getName(), fileName, volume);
  }
  public Sound getSound(String fileName) {
    return MyAssetUtil.getSound(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public Music getMusic(String fileName) {
    return MyAssetUtil.getMusic(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public TextureAtlas getTextureAtlas(String fileName) {
    return MyAssetUtil.getTextureAtlas(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public BitmapFont getBitmapFont(String fileName) {
    return MyAssetUtil.getBitmapFont(MyGame.assetManager, getRoot().getName(), fileName);
  }
  public BitmapFont getBitmapFont(TextureAtlas atlas, String fileName) {
    return MyAssetUtil.getBitmapFont(atlas, getRoot().getName(), fileName);
  }
  public Drawable getDrawable(String fileName) {
    return new TextureRegionDrawable(getRegion(fileName));
  }
  /**
   * fileName要带上.png或者.jpg之类的后缀
   */
  public Texture getTexture(String fileName) {
    try {
      return MyAssetUtil.getTexture(MyGame.assetManager, getRoot().getName(), fileName);
    } catch (Exception e) {
      return MyAssetUtil.getTexture(MyGame.commonAssets.assetManager, "common", fileName);
    }
  }
  public TextureRegion getRegion(String fileName) {
    try {
      return new TextureRegion(getTexture(fileName));
    } catch (Exception e) {
      if (fileName.contains(".png")) {
        fileName = fileName.substring(0, fileName.length() - 4);
      }
      try {
        return getTextureAtlas(getRoot().getName()).findRegion(fileName);
      } catch (Exception e1) {
        return MyAssetUtil.getTextureAtlas(MyGame.commonAssets.assetManager, "common", "common").findRegion(fileName);
      }
    }
  }
  /**
   * fileName要带上.png或者.jpg之类的后缀
   */
  public TImage getImage(String fileName) {
    try {
      return new TImage(getTexture(fileName));
    } catch (Exception e) {
      if (fileName.contains(".png")) {
        fileName = fileName.substring(0, fileName.length() - 4);
      }
      try {
        return new TImage(getTextureAtlas(getRoot().getName()).findRegion(fileName));
      } catch (Exception e1) {
        return new TImage(MyAssetUtil.getTextureAtlas(MyGame.commonAssets.assetManager, "common", "common").findRegion(fileName));
      }
    }
  }
  public TImage getImage(String fileName, float angle) {
    TImage image = new TImage(getTexture(fileName));
    image.origonCenter();
    image.setRotation(angle);
    return image;
  }
  public TImage getImage(String atlasFileName, String textureRegionName) {
    return new TImage(getTextureAtlas(atlasFileName).findRegion(textureRegionName));
  }
  public TImage getImage(Texture texture) {
    return new TImage(texture);
  }
  public TImage getImage(NinePatch ninePatch) {
    return new TImage(ninePatch);
  }
  public NinePatch getNinePatch(String name, int left, int right, int up, int down) {
    try {
      return new NinePatch(getTexture(name), left, right, up, down);
    } catch (Exception e) {
      return new NinePatch(getRegion(name), left, right, up, down);
    }
  }
  public TImage getNineImage(String name, int left, int right, int up, int down) {
    return new TImage(getNinePatch(name, left, right, up, down));
  }
  public Label getFntLabel(String fontName, Object defaultStr) {
    Label.LabelStyle labelStyle = new Label.LabelStyle(getBitmapFont(fontName), Color.WHITE);
    Label label = new Label(defaultStr.toString(), labelStyle);
    return label;
  }
  public NativeLabel getNativeLabel(CharSequence defaultStr, NativeFont font) {
    NativeLabel nativeLabel = new NativeLabel(defaultStr, font);
    nativeLabel.setColor(Color.WHITE);
    return nativeLabel;
  }
  public NativeLabel getNativeLabel(Object defaultStr) {
    NativeLabel nativeLabel = new NativeLabel(defaultStr.toString(), MyGame.getDefaultFont());
    nativeLabel.setColor(Color.WHITE);
    return nativeLabel;
  }
  public NativeTextField getNativeTextFiled(Object defaultStr, NinePatch background, Drawable cursor) {
    NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(background);
    TextField.TextFieldStyle style = new TextField.TextFieldStyle(MyGame.getDefaultFont(), Color.WHITE, cursor, null,
        ninePatchDrawable);
    return new NativeTextField(defaultStr.toString(), style);
  }
  public NativeTextArea getNativeTextArea(Object defaultStr, Drawable background, Drawable cursor) {
    TextField.TextFieldStyle style = new TextField.TextFieldStyle(MyGame.getDefaultFont(), Color.BLACK, cursor, null, background);
    return new NativeTextArea(defaultStr.toString(), style);
  }
  public ScrollPane getVSrollPane(float w, float h, Array<Actor> items) {
    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.space(10);
    for (Actor actor : items) {
      verticalGroup.addActor(actor);
    }
    ScrollPane scrollPane = new ScrollPane(verticalGroup);
    scrollPane.setSize(w, h);
    return scrollPane;
  }
  private MyCheckBox getNewCheckBox(String backgroundTextureName, String checkTextureName) {
    try {
      return new MyCheckBox(getRegion(backgroundTextureName), getRegion(checkTextureName));
    } catch (Exception e) {
      return new MyCheckBox(getTexture(backgroundTextureName), getTexture(checkTextureName));
    }
  }
  public MyCheckBox getCheckBox(String iconName, String backgroundTextureName, String checkTextureName) {
    MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
    if (iconName != null && iconName.length() != 0) {
      checkBox.setIcon(getImage(iconName + ".png"));
    }
    return checkBox;
  }
  public MyCheckBox getCheckBox(String iconNameNormal, String iconNamePressed, String backgroundTextureName, String checkTextureName) {
    MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
    if (iconNamePressed != null && iconNamePressed.length() != 0) {
      checkBox.setIcon(getImage(iconNameNormal + ".png"), getImage(iconNamePressed + ".png"));
    } else {
      if (iconNameNormal != null && iconNameNormal.length() != 0) {
        checkBox.setIcon(getImage(iconNameNormal + ".png"));
      }
    }
    return checkBox;
  }
  public MyCheckBox getCheckBox(String iconName, String backgroundTextureName, String checkTextureName, float iconpadding) {
    MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
    if (iconName != null && iconName.length() != 0) {
      checkBox.setIcon(getImage(iconName + ".png"));
    }
    checkBox.setIconpadding(iconpadding);
    return checkBox;
  }
  public MyCheckBox getCheckBox(String iconNameNormal, String iconNamePressed, String backgroundTextureName, String checkTextureName,
      float iconpadding) {
    MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
    if (iconNamePressed != null && iconNamePressed.length() != 0) {
      checkBox.setIcon(getImage(iconNameNormal + ".png"), getImage(iconNamePressed + ".png"));
    } else {
      if (iconNameNormal != null && iconNameNormal.length() != 0) {
        checkBox.setIcon(getImage(iconNameNormal + ".png"));
      }
    }
    checkBox.setIconpadding(iconpadding);
    return checkBox;
  }
  public ScrollPane getVSrollPane(float w, float h, float space, Array<Actor> items) {
    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.space(space);
    for (Actor actor : items) {
      verticalGroup.addActor(actor);
    }
    ScrollPane scrollPane = new ScrollPane(verticalGroup);
    scrollPane.setSize(w, h);
    return scrollPane;
  }
  public ScrollPane getHSrollPane(float w, float h, Array<Actor> items) {
    HorizontalGroup verticalGroup = new HorizontalGroup();
    verticalGroup.space(10);
    for (Actor actor : items) {
      verticalGroup.addActor(actor);
    }
    ScrollPane scrollPane = new ScrollPane(verticalGroup);
    scrollPane.setSize(w, h);
    return scrollPane;
  }
  /**
   * fileName要带上.png或者.jpg之类的后缀
   */
  public TImage getImage(String fileName, boolean flipX, boolean flipY) {
    TextureRegion textureRegion = getRegion(fileName);
    textureRegion.flip(flipX, flipY);
    return new TImage(textureRegion);
  }
  @Override
  public void resize(int width, int height) {
    getViewport().update(width, height, false);
  }
  @Override
  public void render(float delta) {
    Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    act();
    draw();
    if (!MyGame.isLoadingFinish) {
      if (MyGame.assetManager.update()) {
        assign();
        MyGame.isLoadingFinish = true;
      }
    }
  }
  @Override
  public void dispose() {
    for (Actor actor : getActors()) {
      Class<?>[] interfaces = actor.getClass().getInterfaces();
      for (int i = 0, j = interfaces.length; i < j; i++) {
        if (interfaces[i] == Disposable.class) {
          ((Disposable) actor).dispose();
          break;
        }
      }
    }
    MyGame.assetManager.clear();
    super.dispose();
  }
  @Override
  public void show() {
  }
  @Override
  public void hide() {
  }
  @Override
  public void pause() {
  }
  @Override
  public void resume() {
  }
}
