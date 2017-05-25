package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.AnimationState.TrackEntry;

public class MyActor {
  private static void drawFlashBefore(FrameBuffer fbo, Batch batch) {
    batch.end();
    fbo.begin();
    Gdx.gl20.glViewport(0, 0, MyGame.worldWidth, MyGame.worldHeight);
    Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
    Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
  }
  private static void drawFlashAfter(FrameBuffer fbo, TextureRegion fboRegion, TextureRegion flashRegion, Actor actor,
      Actor flashActor, Batch batch, Viewport viewport) {
    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    batch.draw(flashRegion, flashActor.getX(), 0, flashRegion.getRegionWidth(), fbo.getHeight());
    batch.end();
    fbo.end();
    Gdx.gl20.glViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    batch.begin();
    batch.draw(fboRegion, actor.getX(), actor.getY(), actor.getOriginX(), actor.getOriginY(), actor.getWidth(), actor.getHeight(),
        actor.getScaleX(), actor.getScaleY(), actor.getRotation());
  }
  private static void startFlash(Actor actor, Actor flashActor, float duration) {
    if (flashActor.getActions().size > 0) {
      return;
    }
    flashActor.setX(0);
    flashActor.clearActions();
    flashActor.addAction(Actions.sequence(Actions.moveBy(actor.getWidth(), 0, duration)));
  }
  private static void startFlash(Actor actor, Actor flashActor, float duration, boolean isLoop) {
    if (flashActor.getActions().size > 0) {
      return;
    }
    flashActor.setX(0);
    flashActor.clearActions();
    if (isLoop) {
      flashActor.addAction(Actions.forever(Actions.sequence(Actions.moveBy(actor.getWidth() * actor.getScaleX(), 0, duration),
          Actions.moveBy(-actor.getWidth() * actor.getScaleX(), 0))));
    } else {
      flashActor.addAction(Actions.sequence(Actions.moveBy(actor.getWidth(), 0, duration)));
    }
  }
  public static class FlashImage extends Image implements Disposable {
    private FrameBuffer fbo = null;
    private Texture fboTexture = null;
    private TextureRegion fboRegion = null;
    private TextureRegion flashRegion = null;
    private Actor flashActor;
    public FlashImage(TextureRegion textureRegion, TextureRegion flashRegion) {
      super(textureRegion);
      this.flashRegion = flashRegion;
      flashActor = new Actor();
      flashActor.setSize(flashRegion.getRegionWidth(), flashRegion.getRegionHeight());
      fbo = new FrameBuffer(Format.RGBA8888, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), true);
      fboTexture = fbo.getColorBufferTexture();
      fboTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      fboRegion = new TextureRegion(fboTexture);
      fboRegion.flip(false, true);
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      flashActor.act(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      if (flashActor.getActions().size > 0) {
        drawFlashBefore(fbo, batch);
        getDrawable().draw(batch, 0, 0, fbo.getWidth(), fbo.getHeight());
        drawFlashAfter(fbo, fboRegion, flashRegion, this, flashActor, batch, getStage().getViewport());
      } else {
        super.draw(batch, parentAlpha);
      }
    }
    public void startFlashAction(float duration) {
      MyActor.startFlash(this, flashActor, duration);
    }
    public void startFlash(float duration, boolean isLoop) {
      MyActor.startFlash(this, flashActor, duration, isLoop);
    }
    @Override
    public void dispose() {
      fbo.dispose();
      fboTexture.dispose();
    }
  }
  public static class FlashGroup extends Group implements Disposable {
    private FrameBuffer fbo = null;
    private Texture fboTexture = null;
    private TextureRegion fboRegion = null;
    private TextureRegion flashRegion = null;
    private Actor flashActor;
    public FlashGroup(TextureRegion flashRegion, int width, int height) {
      super();
      setSize(width, height);
      this.flashRegion = flashRegion;
      flashActor = new Actor();
      flashActor.setSize(flashRegion.getRegionWidth(), flashRegion.getRegionHeight());
      fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
      fboTexture = fbo.getColorBufferTexture();
      fboTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      fboRegion = new TextureRegion(fboTexture);
      fboRegion.flip(false, true);
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      flashActor.act(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      if (flashActor.getActions().size > 0) {
        drawFlashBefore(fbo, batch);
        drawChildren(batch, parentAlpha);
        drawFlashAfter(fbo, fboRegion, flashRegion, this, flashActor, batch, getStage().getViewport());
      } else {
        super.draw(batch, parentAlpha);
      }
    }
    public void startFlashAction(float duration) {
      MyActor.startFlash(this, flashActor, duration);
    }
    public void startFlash(float duration, boolean isLoop) {
      MyActor.startFlash(this, flashActor, duration, isLoop);
    }
    @Override
    public void dispose() {
      fbo.dispose();
      fboTexture.dispose();
    }
  }
  public static class FlashImageButton extends MyImageButton implements Disposable {
    private FrameBuffer fbo = null;
    private Texture fboTexture = null;
    private TextureRegion fboRegion = null;
    private TextureRegion flashRegion = null;
    private Actor flashActor;
    public FlashImageButton(TextureRegion imageUp, TextureRegion imageDown, TextureRegion imageChecked, TextureRegion flashRegion) {
      super(imageUp, imageDown, imageChecked);
      this.flashRegion = flashRegion;
      flashActor = new Actor();
      flashActor.setSize(flashRegion.getRegionWidth(), flashRegion.getRegionHeight());
      fbo = new FrameBuffer(Format.RGBA8888, imageUp.getRegionWidth(), imageUp.getRegionHeight(), false);
      fboTexture = fbo.getColorBufferTexture();
      fboTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      fboRegion = new TextureRegion(fboTexture);
      fboRegion.flip(false, true);
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      flashActor.act(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      if (flashActor.getActions().size > 0) {
        drawFlashBefore(fbo, batch);
        getDrawable().draw(batch, 0, 0, fbo.getWidth(), fbo.getHeight());
        drawFlashAfter(fbo, fboRegion, flashRegion, this, flashActor, batch, getStage().getViewport());
      } else {
        super.draw(batch, parentAlpha);
      }
    }
    public void startFlashAction(float duration) {
      MyActor.startFlash(this, flashActor, duration);
    }
    public void startFlash(float duration, boolean isLoop) {
      MyActor.startFlash(this, flashActor, duration, isLoop);
    }
    @Override
    public void dispose() {
      fbo.dispose();
      fboTexture.dispose();
    }
  }
  public static class CutoutRoundRect extends Actor implements Disposable {
    public boolean isSolid;
    private FrameBuffer fbo = null;
    private Texture fboTexture = null;
    private TextureRegion fboRegion = null;
    private float radius;
    public CutoutRoundRect(float x, float y, float width, float height, float radius) {
      super();
      isSolid = false;
      if (MyGame.shapeRenderer == null) {
        MyGame.shapeRenderer = new ShapeRenderer();
      }
      setColor(new Color(0, 0, 0, 0f));
      setBounds(x, y, width, height);
      this.radius = radius;
      setTouchable(Touchable.disabled);
      fbo = new FrameBuffer(Format.RGBA8888, MyGame.worldWidth, MyGame.worldHeight, false);
      fboTexture = fbo.getColorBufferTexture();
      fboTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      fboRegion = new TextureRegion(fboTexture);
      fboRegion.flip(false, true);
    }
    public CutoutRoundRect(float x, float y, float width, float height) {
      this(x, y, width, height, width < height ? width / 8 : height / 8);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      if (isSolid) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        MyGame.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        MyGame.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        MyGame.shapeRenderer.begin(ShapeType.Filled);
        MyGame.shapeRenderer.setColor(new Color(0, 0, 0, 0.6f));
        MyGame.shapeRenderer.rect(0, 0, MyGame.worldWidth, MyGame.worldHeight);
        MyGame.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
      } else {
        batch.end();
        fbo.begin();
        Gdx.gl20.glClearColor(0f, 0f, 0f, 0.6f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        MyGame.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        MyGame.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        MyGame.shapeRenderer.translate(getX() + getOriginX(), getY() + getOriginY(), 0);
        MyGame.shapeRenderer.scale(getScaleX(), getScaleY(), 1);
        MyGame.shapeRenderer.rotate(0, 0, 1, getRotation());
        MyGame.shapeRenderer.begin(ShapeType.Filled);
        MyGame.shapeRenderer.setColor(getColor());
        MyGame.shapeRenderer.rect(0, radius, radius, getHeight() - 2 * radius);
        MyGame.shapeRenderer.rect(radius, 0, getWidth() - 2 * radius, getHeight());
        MyGame.shapeRenderer.rect(getWidth() - radius, radius, radius, getHeight() - 2 * radius);
        MyGame.shapeRenderer.arc(radius, radius, radius, 180 - 10, 90 + 10, 25);
        MyGame.shapeRenderer.arc(radius, getHeight() - radius, radius, 90 - 10, 90 + 10, 25);
        MyGame.shapeRenderer.arc(getWidth() - radius, radius, radius, 270 - 10, 90 + 10, 25);
        MyGame.shapeRenderer.arc(getWidth() - radius, getHeight() - radius, radius, 0 - 10, 90 + 10, 25);
        MyGame.shapeRenderer.end();
        batch.end();
        fbo.end();
        Gdx.gl20.glViewport(getStage().getViewport().getScreenX(), getStage().getViewport().getScreenY(),
            getStage().getViewport().getScreenWidth(), getStage().getViewport().getScreenHeight());
        batch.begin();
        batch.draw(fboRegion, 0, 0);
      }
    }
    @Override
    public void dispose() {
      fbo.dispose();
      fboTexture.dispose();
    }
  }
  public static class TimeBar extends Actor {
    public float value;
    public float duration;
    private TextureRegion knob;
    private float backwardAmount;
    private float backwardDuration;
    private float backwardTime;
    private Array<InsertEvent> insertEvents;
    public TimeBar(float duration, TextureRegion knob) {
      this.duration = duration;
      this.knob = knob;
      setSize(knob.getRegionWidth(), knob.getRegionHeight());
      value = getWidth();
      insertEvents = new Array<InsertEvent>();
    }
    @Override
    public void draw(Batch batch, float arg1) {
      batch.draw(knob, getX(), getY(), knob.getRegionWidth(), knob.getRegionHeight());
    }
    @Override
    public void act(float delta) {
      for (InsertEvent insertEvent : insertEvents) {
        if (!insertEvent.complete) {
          if (insertEvent.start >= duration * (value / getWidth())) {
            insertEvent.run();
            insertEvent.complete = true;
          }
        }
      }
      super.act(delta);
      if (backwardTime > 0) {
        if (backwardTime <= delta) {
          delta = backwardTime;
          backwardTime = 0;
        } else {
          backwardTime = backwardTime - delta;
        }
        value = value + (getWidth() * (backwardAmount / duration) * (delta / backwardDuration));
        if (value >= getWidth()) {
          value = getWidth();
          backwardDuration = 0;
          backwardTime = 0;
        }
      } else {
        value = value - getWidth() * (delta / duration);
        if (value <= 0) {
          value = 0;
        }
      }
      knob.setRegionWidth((int) value);
    }
    public void setBackward(float backwardAmount, float backwardDuration) {
      this.backwardAmount = backwardAmount;
      this.backwardDuration = backwardDuration;
      backwardTime = backwardDuration;
      // 如果某插入事件已完成，而后退后回到了该事件未完成的时刻，那么就要重新执行该事件，所以把所有事件的完成判断设置为false
      for (InsertEvent insertEvent : insertEvents) {
        insertEvent.complete = false;
      }
    }
    public void addInsertEvent(InsertEvent insertEvent) {
      insertEvents.add(insertEvent);
    }
    public void restart() {
      value = getWidth();
      knob.setRegionWidth((int) value);
      for (InsertEvent insertEvent : insertEvents) {
        insertEvent.complete = false;
      }
    }
  }
  public static abstract class InsertEvent {
    public float start;
    public boolean complete;// 主要是在TimeBar中完成时间不是确数，所以要加一个判断变量，在CountdownLabel中可以不用
    public InsertEvent(float startTimeByRemainTime) {
      this.start = startTimeByRemainTime;
      complete = false;
    }
    abstract protected void run();
  }
  public static class CountdownLabel extends Label {
    private int counter = 0;
    private Array<InsertEvent> insertEvents;
    public CountdownLabel(BitmapFont bitmapfont) {
      this(bitmapfont, Color.WHITE);
    }
    public CountdownLabel(BitmapFont bitmapfont, Color color) {
      super("", new LabelStyle(bitmapfont, color));
      addAction(Actions.forever(Actions.delay(1, Actions.run(new Runnable() {
        @Override
        public void run() {
          for (InsertEvent insertEvent : insertEvents) {
            if (!insertEvent.complete) {
              if (insertEvent.start == counter) {
                insertEvent.run();
                insertEvent.complete = true;
              }
            }
          }
          if (counter > 0) {
            counter--;
          }
          setText("" + counter);
        }
      }))));
      insertEvents = new Array<InsertEvent>();
    }
    public void start(int countdownTime) {
      counter = countdownTime;
      setText("" + counter);
      for (InsertEvent insertEvent : insertEvents) {
        insertEvent.complete = false;
      }
    }
    public void addInsertEvent(InsertEvent insertEvent) {
      insertEvents.add(insertEvent);
    }
  }
  public static class SpineActor extends Actor {
    public static SkeletonMeshRenderer renderer;
    public Skeleton skeleton;
    public AnimationStateData animationStateData;
    public AnimationState animationState;
    public boolean isLoop;
    public SpineActor() {
      super();
    }
    public SpineActor(SkeletonData skeletonData, boolean isLoop) {
      super();
      renderer = new SkeletonMeshRenderer();
      renderer.setPremultipliedAlpha(true);
      this.isLoop = isLoop;
      init(skeletonData);
    }
    public SpineActor(TextureAtlas atlas, String filePath) {
      this(new SkeletonBinary(atlas).readSkeletonData(Gdx.files.internal(filePath + ".skel")), true);
    }
    public SpineActor(AssetManager assetManager, String filePath, boolean isLoop) {
      this(new SkeletonJson(assetManager.get(filePath + ".atlas", TextureAtlas.class))
          .readSkeletonData(Gdx.files.internal(filePath + ".json")), isLoop);
    }
    public SpineActor(String filePath, boolean isLoop) {
      this(new SkeletonJson(MyGame.assetManager.get(filePath + ".atlas", TextureAtlas.class))
          .readSkeletonData(Gdx.files.internal(filePath + ".json")), isLoop);
    }
    public SpineActor(TextureAtlas atlas, String filePath, boolean isLoop) {
      this(new SkeletonJson(atlas).readSkeletonData(Gdx.files.internal(filePath + ".json")), isLoop);
    }
    public void init(SkeletonData skeletonData) {
      float x = 0, y = 0;
      if (skeleton != null) {
        x = skeleton.getX();
        y = skeleton.getY();
        skeleton = null;
      }
      animationState = null;
      animationStateData = null;
      skeleton = new Skeleton(skeletonData);
      animationStateData = new AnimationStateData(skeletonData);
      animationState = new AnimationState(animationStateData);
      animationState.setAnimation(0, animationState.getData().getSkeletonData().getAnimations().get(0), this.isLoop);
      skeleton.setToSetupPose();
      /*
       * 因为setPosition时不是直接对skeleton设定，而是通过改变actor位置时才会调用的positionChanged( )
       * 来设定skeleton的位置，所以若是重设skeleton时，skeleton所有信息被清空， 而重新设定的actor的位置跟原来一样
       * ，则不会调用positionChanged()，
       * 那么skeleton的位置就会是0，因此需要在重新设定skeleton以后，再把skeleton的位置恢复到原来的状态
       */
      skeleton.setPosition(x, y);
      setWidth(animationState.getData().getSkeletonData().getWidth());
      setHeight(animationState.getData().getSkeletonData().getHeight());
    }
    public void setPlayOnce() {
      animationState.addListener(new AnimationStateListener() {
        @Override
        public void start(TrackEntry entry) {
        }
        @Override
        public void interrupt(TrackEntry entry) {
        }
        @Override
        public void event(TrackEntry entry, Event event) {
        }
        @Override
        public void end(TrackEntry entry) {
        }
        @Override
        public void dispose(TrackEntry entry) {
        }
        @Override
        public void complete(TrackEntry entry) {
          setVisible(false);
        }
      });
    }
    public void setMix(AnimationStateData data, String from, String to, float mix) {
      com.esotericsoftware.spine.Animation fromAnimation = data.getSkeletonData().findAnimation(from);
      com.esotericsoftware.spine.Animation toAnimation = data.getSkeletonData().findAnimation(to);
      if (fromAnimation == null || toAnimation == null)
        return;
      data.setMix(fromAnimation, toAnimation, mix);
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      if (isVisible()) {
        animationState.update(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));// Gdx.graphics.getDeltaTime()
        animationState.apply(skeleton);
        skeleton.updateWorldTransform();
      }
      // skeleton.getRootBone().setScale(getScaleX(), getScaleY());
      // skeleton.getRootBone().setRotation(getRotation());不能直接这么写，会抵消动画编辑器中已经设定好的Rotation
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      // skeleton.updateWorldTransform();
      Color color = getColor();
      batch.setColor(color);
      for (Slot slot : skeleton.getSlots()) {
        slot.getColor().set(color.r, color.g, color.b, color.a * parentAlpha);
      }
      // System.out.println("MyActor.SpineActor.draw()========="+skeleton.toString()+"==="+skeleton.getRootBone().getScaleX());
      renderer.draw((PolygonSpriteBatch) batch, skeleton);
      batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    @Override
    protected void positionChanged() {
      skeleton.setX(getX());
      skeleton.setY(getY());
    }
    @Override
    public void setColor(Color color) {
      super.setColor(color);
      for (Slot slot : skeleton.getSlots()) {
        slot.getColor().set(color);
      }
    }
    @Override
    public void setColor(float r, float g, float b, float a) {
      super.setColor(r, g, b, a);
      for (Slot slot : skeleton.getSlots()) {
        slot.getColor().set(r, g, b, a);
      }
    }
    @Override
    public void setScale(float scaleXY) {
      skeleton.getRootBone().setScale(scaleXY);
      super.setScale(scaleXY);
    }
    @Override
    public void setScale(float scaleX, float scaleY) {
      skeleton.getRootBone().setScale(scaleX, scaleY);
      super.setScale(scaleX, scaleY);
    }
    @Override
    public void setScaleX(float scaleX) {
      skeleton.getRootBone().setScaleX(scaleX);
      super.setScaleX(scaleX);
    }
    @Override
    public void setScaleY(float scaleY) {
      skeleton.getRootBone().setScaleY(scaleY);
      super.setScaleY(scaleY);
    }
  }
  public static class ParticleEffectActor extends Actor implements Disposable {
    public ParticleEffect particle;
    private void init() {
      particle = new ParticleEffect();
      setVisible(false);
      setTouchable(Touchable.disabled);
    }
    public ParticleEffectActor(String effectFilePath, TextureAtlas atlas) {
      init();
      particle.load(Gdx.files.internal(effectFilePath), atlas);
    }
    public ParticleEffectActor(String effectFilePath, String imagePath) {
      init();
      particle.load(Gdx.files.internal(effectFilePath), Gdx.files.internal(imagePath));
    }
    @Override
    protected void positionChanged() {
      particle.setPosition(getX(), getY());
    }
    @Override
    public void setVisible(boolean visible) {
      if (visible) {
        particle.reset();
      }
      super.setVisible(visible);
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      particle.update(delta);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      particle.draw(batch);
      if (isComplete()) {
        setVisible(false);
      }
    }
    public boolean isComplete() {
      return particle.isComplete();
    }
    @Override
    public void dispose() {
      particle.dispose();
    }
  }
  public static class MyImageButton extends Image {
    private boolean isChecked;
    private ClickListener clickListener;
    private Drawable imageUp, imageDown, imageChecked;
    private Sound sound;
    public MyImageButton(Sound buttonSound, TextureRegion imageUp, TextureRegion imageDown, TextureRegion imageChecked) {
      super();
      this.sound = buttonSound;
      this.imageUp = new TextureRegionDrawable(imageUp);
      this.imageDown = new TextureRegionDrawable(imageDown);
      this.imageChecked = (imageChecked == null) ? null : new TextureRegionDrawable(imageChecked);
      setSize(imageUp.getRegionWidth(), imageUp.getRegionHeight());
      setDrawable(this.imageUp);
      setTouchable(Touchable.enabled);
      addListener(clickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          setChecked(!isChecked);
        }
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          if (sound != null & MyGame.isSoundPlay) {
            sound.play();
          }
          return super.touchDown(event, x, y, pointer, button);
        }
      });
    }
    public MyImageButton(Sound buttonSound, TextureRegion imageUp, TextureRegion imageDown) {
      this(buttonSound, imageUp, imageDown, null);
    }
    public MyImageButton(TextureRegion imageUp, TextureRegion imageDown, TextureRegion imageChecked) {
      this(null, imageUp, imageDown, imageChecked);
    }
    public MyImageButton(TextureRegion imageUp, TextureRegion imageDown) {
      this(null, imageUp, imageDown, null);
    }
    public void setChecked(boolean isChecked) {
      if (this.isChecked == isChecked)
        return;
      this.isChecked = isChecked;
      ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
      if (fire(changeEvent))
        this.isChecked = !isChecked;
      Pools.free(changeEvent);
    }
    public boolean isChecked() {
      return isChecked;
    }
    public boolean isPressed() {
      return clickListener.isVisualPressed();
    }
    @Override
    public void act(float delta) {
      super.act(delta);
      updateImage();
    }
    private void updateImage() {
      Drawable drawable = null;
      if (isPressed() && imageDown != null)
        drawable = imageDown;
      else if (isChecked && imageChecked != null)
        drawable = imageChecked;
      else if (imageUp != null)
        drawable = imageUp;
      setDrawable(drawable);
    }
  }
  public static class DiscolorButton extends Image {
    public TouchUpEvent touchUpEvent;
    public DiscolorButton(NinePatch patch, TouchUpEvent touchUpEvent) {
      this(new NinePatchDrawable(patch), Scaling.stretch, Align.center, touchUpEvent);
    }
    public DiscolorButton(TextureRegion region, TouchUpEvent touchUpEvent) {
      this(new TextureRegionDrawable(region), Scaling.stretch, Align.center, touchUpEvent);
    }
    public DiscolorButton(Texture texture, TouchUpEvent touchUpEvent) {
      this(new TextureRegionDrawable(new TextureRegion(texture)), touchUpEvent);
    }
    public DiscolorButton(Skin skin, String drawableName, TouchUpEvent touchUpEvent) {
      this(skin.getDrawable(drawableName), Scaling.stretch, Align.center, touchUpEvent);
    }
    public DiscolorButton(Drawable drawable, TouchUpEvent touchUpEvent) {
      this(drawable, Scaling.stretch, Align.center, touchUpEvent);
    }
    public DiscolorButton(Drawable drawable, Scaling scaling, TouchUpEvent touchUpEvent) {
      this(drawable, scaling, Align.center, touchUpEvent);
    }
    public DiscolorButton(Drawable drawable, Scaling scaling, int align, TouchUpEvent touchUpEvent) {
      super(drawable, scaling, align);
      this.touchUpEvent = touchUpEvent;
      this.addListener(new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          event.getTarget().setColor(0.5f, 0.5f, 0.5f, 1f);
          return true;
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          event.getTarget().setColor(Color.WHITE);
          if (event.getTarget().hit(x, y, true) == null) {
            return;
          }
          DiscolorButton.this.touchUpEvent.run(event);
        }
      });
    }
    public static interface TouchUpEvent {
      public void run(InputEvent event);
    }
  }
  public static class MySlider extends Slider {
    public MySlider(float min, float max, float stepSize, boolean vertical, TextureRegion background, TextureRegion knob,
        TextureRegion knobBefore) {
      super(min, max, stepSize, vertical, new SliderStyle(new TextureRegionDrawable(background), new TextureRegionDrawable(knob)));
      this.getStyle().knobBefore = new TextureRegionDrawable(knobBefore);
    }
    public MySlider(float min, float max, float stepSize, boolean vertical, Texture background, Texture knob, Texture knobBefore) {
      this(min, max, stepSize, vertical, new TextureRegion(background), new TextureRegion(knob), new TextureRegion(knobBefore));
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
      float position = 0;// 现在只写了横着的
      if (getMinValue() != getMaxValue()) {
        float knobWidthHalf = 0;
        float positionWidth = getWidth();
        float knobWidth = getStyle().knob == null ? 0 : getStyle().knob.getMinWidth();
        float bgLeftWidth = 0;
        if (getStyle().background != null) {
          bgLeftWidth = getStyle().background.getLeftWidth();
          positionWidth -= bgLeftWidth + getStyle().background.getRightWidth();
        }
        if (getStyle().knob == null) {
          knobWidthHalf = getStyle().knobBefore == null ? 0 : getStyle().knobBefore.getMinWidth() * 0.5f;
          position = (positionWidth - knobWidthHalf) * getVisualPercent();
          position = Math.min(positionWidth - knobWidthHalf, position);
        } else {
          knobWidthHalf = knobWidth * 0.5f;
          position = (positionWidth - knobWidth) * getVisualPercent();
          position = Math.min(positionWidth - knobWidth, position) + bgLeftWidth;
        }
        position = Math.max(0, position);
      }
      ((TextureRegionDrawable) getStyle().knobBefore).getRegion()
          .setRegionWidth((int) (position + (getKnobDrawable() == null ? 0 : getKnobDrawable().getMinWidth()) * 0.5f));
      super.draw(batch, parentAlpha);
    }
  }
}