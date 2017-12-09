package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.utils.Align;
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
		if (MyGame.loadingScreen != null) {
			MyGame.loadingScreen.remove();
		}
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

	// public String getAssetsPath() {
	// return MyAssetUtil.getAssetsPath(getRoot().getName());
	// }

	public BitmapFont getBitmapFont(String fntFileName) {
		return MyAssetUtil.getBitmapFont(getName(), fntFileName);
	}

	public BitmapFont getBitmapFont(TextureAtlas atlas, String fileName) {
		return MyAssetUtil.getBitmapFont(atlas, getRoot().getName(), fileName);
	}

	public Drawable getDrawable(String textureFileName) {
		return MyAssetUtil.getDrawable(getName(), textureFileName);
	}

	public TextureAtlas getTextureAtlas() {
		return MyAssetUtil.getTextureAtlas(getName());
	}

	public TextureAtlas getTextureAtlas(String atlasFileName) {
		return MyAssetUtil.getTextureAtlas(getName(), atlasFileName);
	}

	public void playSound(String mp3FileName) {
		MyAssetUtil.playSound(getName(), mp3FileName);
	}

	public Sound getSound(String mp3FileName) {
		return MyAssetUtil.getSound(getName(), mp3FileName);
	}

	public void playMusic(String mp3FileName) {
		MyAssetUtil.playMusic(getName(), mp3FileName);
	}

	public Music getMusic(String mp3FileName) {
		return MyAssetUtil.getMusic(getName(), mp3FileName);
	}

	/**
	 * fileName要带上.png或者.jpg之类的后缀
	 */
	public Texture getTexture(String textureFileName) {
		return MyAssetUtil.getTexture(getName(), textureFileName);
	}

	public TextureRegion getTextureRegion(String textureFileName) {
		return MyAssetUtil.getTextureRegion(getName(), textureFileName);
	}

	/** fileName要带上.png或者.jpg之类的后缀 */
	public TImage getImage(String textureFileName) {
		return MyAssetUtil.getTImage(getName(), textureFileName);
	}

	public TImage getImage(String textureFileName, float angle) {
		TImage image = getImage(textureFileName);
		image.origonCenter();
		image.setRotation(angle);
		return image;
	}

	public TImage getImage(String atlasFileName, String textureFileName) {
		return MyAssetUtil.getTImage(getName(), atlasFileName, textureFileName);
	}

	public TImage getImage(TextureAtlas atlas, String textureFileName) {
		return new TImage(atlas.findRegion(textureFileName));
	}

	public TImage getImage(Texture texture) {
		return new TImage(texture);
	}

	public TImage getImage(NinePatch ninePatch) {
		return new TImage(ninePatch);
	}

	public NinePatch getNinePatch(String name, int left, int right, int up, int down) {
		try {
			return new NinePatch(MyAssetUtil.getTexture(getRoot().getName(), name), left, right, up, down);
		} catch (Exception e) {
			return new NinePatch(MyAssetUtil.getTextureRegion(getRoot().getName(), name), left, right, up, down);
		}
	}

	public TImage getNineImage(String name, int left, int right, int up, int down) {
		return new TImage(getNinePatch(name, left, right, up, down));
	}

	public Label getFntLabel(String fontName, Object defaultStr) {
		Label.LabelStyle labelStyle = new Label.LabelStyle(MyAssetUtil.getBitmapFont(getRoot().getName(), fontName),
				Color.WHITE);
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
		NinePatchDrawable ninePatchDrawable = background != null ? new NinePatchDrawable(background) : null;
		TextField.TextFieldStyle style = new TextField.TextFieldStyle(MyGame.getDefaultFont(), Color.WHITE, cursor,
				null, ninePatchDrawable);
		return new NativeTextField(defaultStr.toString(), style);
	}

	public NativeTextArea getNativeTextArea(Object defaultStr, Drawable background, Drawable cursor) {
		TextField.TextFieldStyle style = new TextField.TextFieldStyle(MyGame.getDefaultFont(), Color.BLACK, cursor,
				null, background);
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

	private MyCheckBox getNewCheckBox(TextureAtlas atlas, String backgroundTextureName, String checkTextureName) {
		return new MyCheckBox(atlas.findRegion(backgroundTextureName), atlas.findRegion(checkTextureName));
	}

	public MyCheckBox getCheckBox(TextureAtlas atlas, String iconName, String backgroundTextureName,
			String checkTextureName) {
		MyCheckBox checkBox = getNewCheckBox(atlas, backgroundTextureName, checkTextureName);
		if (iconName != null && iconName.length() != 0) {
			checkBox.setIcon(getImage(atlas, iconName));
		}
		return checkBox;
	}

	public MyCheckBox getCheckBox(TextureAtlas atlas, String iconNameNormal, String iconNamePressed,
			String backgroundTextureName, String checkTextureName) {
		MyCheckBox checkBox = getNewCheckBox(atlas, backgroundTextureName, checkTextureName);
		if (iconNamePressed != null && iconNamePressed.length() != 0) {
			checkBox.setIcon(getImage(atlas, iconNameNormal), getImage(atlas, iconNamePressed));
		} else {
			if (iconNameNormal != null && iconNameNormal.length() != 0) {
				checkBox.setIcon(getImage(atlas, iconNameNormal));
			}
		}
		return checkBox;
	}

	public MyCheckBox getCheckBox(TextureAtlas atlas, String iconName, String backgroundTextureName,
			String checkTextureName, float iconpadding) {
		MyCheckBox checkBox = getNewCheckBox(atlas, backgroundTextureName, checkTextureName);
		if (iconName != null && iconName.length() != 0) {
			checkBox.setIcon(getImage(atlas, iconName));
		}
		checkBox.setIconpadding(iconpadding);
		return checkBox;
	}

	public MyCheckBox getCheckBox(TextureAtlas atlas, String iconNameNormal, String iconNamePressed,
			String backgroundTextureName, String checkTextureName, float iconpadding) {
		MyCheckBox checkBox = getNewCheckBox(atlas, backgroundTextureName, checkTextureName);
		if (iconNamePressed != null && iconNamePressed.length() != 0) {
			checkBox.setIcon(getImage(atlas, iconNameNormal), getImage(atlas, iconNamePressed));
		} else {
			if (iconNameNormal != null && iconNameNormal.length() != 0) {
				checkBox.setIcon(getImage(atlas, iconNameNormal));
			}
		}
		checkBox.setIconpadding(iconpadding);
		return checkBox;
	}

	private MyCheckBox getNewCheckBox(String backgroundTextureName, String checkTextureName) {
		try {
			return new MyCheckBox(MyAssetUtil.getTextureRegion(getRoot().getName(), backgroundTextureName),
					MyAssetUtil.getTextureRegion(getRoot().getName(), checkTextureName));
		} catch (Exception e) {
			return new MyCheckBox(MyAssetUtil.getTexture(getRoot().getName(), backgroundTextureName),
					MyAssetUtil.getTexture(getRoot().getName(), checkTextureName));
		}
	}

	public MyCheckBox getCheckBox(String iconName, String backgroundTextureName, String checkTextureName) {
		MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
		if (iconName != null && iconName.length() != 0) {
			checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconName + ".png"));
		}
		return checkBox;
	}

	public MyCheckBox getCheckBox(String iconNameNormal, String iconNamePressed, String backgroundTextureName,
			String checkTextureName) {
		MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
		if (iconNamePressed != null && iconNamePressed.length() != 0) {
			checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconNameNormal + ".png"),
					MyAssetUtil.getTImage(getRoot().getName(), iconNamePressed + ".png"));
		} else {
			if (iconNameNormal != null && iconNameNormal.length() != 0) {
				checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconNameNormal + ".png"));
			}
		}
		return checkBox;
	}

	public MyCheckBox getCheckBox(String iconName, String backgroundTextureName, String checkTextureName,
			float iconpadding) {
		MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
		if (iconName != null && iconName.length() != 0) {
			checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconName + ".png"));
		}
		checkBox.setIconpadding(iconpadding);
		return checkBox;
	}

	public MyCheckBox getCheckBox(String iconNameNormal, String iconNamePressed, String backgroundTextureName,
			String checkTextureName, float iconpadding) {
		MyCheckBox checkBox = getNewCheckBox(backgroundTextureName, checkTextureName);
		if (iconNamePressed != null && iconNamePressed.length() != 0) {
			checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconNameNormal + ".png"),
					MyAssetUtil.getTImage(getRoot().getName(), iconNamePressed + ".png"));
		} else {
			if (iconNameNormal != null && iconNameNormal.length() != 0) {
				checkBox.setIcon(MyAssetUtil.getTImage(getRoot().getName(), iconNameNormal + ".png"));
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

	public ScrollPane getHSrollPane(float w, float h, float space, Array<Actor> items) {
		HorizontalGroup verticalGroup = new HorizontalGroup();
		verticalGroup.space(space);
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
		TextureRegion textureRegion = new TextureRegion(MyAssetUtil.getTextureRegion(getRoot().getName(), fileName));
		textureRegion.flip(flipX, flipY);
		return new TImage(textureRegion);
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
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