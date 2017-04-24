package com.yuyointeractive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public abstract class MyShapeActor extends Actor {
   protected ShapeRenderer shapeRenderer;//将来考虑在这里写成静态的参数，然后用Disposable来做释放。
   public MyShapeActor(ShapeRenderer shapeRenderer) {
      this.shapeRenderer = shapeRenderer;
      setTouchable(Touchable.disabled);
   }
   protected abstract void draw(Batch batch);
   @Override
   public void draw(Batch batch, float parentAlpha) {
      batch.end();
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // Gdx.gl.glBlendFunc(GL20.GL_ONE,
                                                                          // GL20.GL_ONE);
      // batch.enableBlending();
      // batch.setBlendFunction(GL20.GL_SRC_ALPHA,
      // GL20.GL_ONE_MINUS_SRC_ALPHA);
      shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
      shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
      shapeRenderer.translate(getX() + getOriginX(), getY() + getOriginY(), 0);
      shapeRenderer.scale(getScaleX(), getScaleY(), 1);
      shapeRenderer.rotate(0, 0, 1, getRotation());
      shapeRenderer.begin(ShapeType.Filled);
      this.draw(batch);
      shapeRenderer.end();
      Gdx.gl.glDisable(GL20.GL_BLEND);
      // batch.disableBlending();
      batch.begin();
   }
   public static class GradientColorLine extends MyShapeActor {
      private boolean isPortrait;
      private Color light;
      private float radius;
      private float length;
      public GradientColorLine(float x, float y, float length, boolean isPortrait, Color color, ShapeRenderer shapeRenderer) {
         super(shapeRenderer);
         setOrigin(Align.center);
         setPosition(x, y);
         setColor(color);
         this.isPortrait = isPortrait;
         this.length = length;
         radius = 10 / 2;
         if (isPortrait) {
            setSize(radius, length);
         } else {
            setSize(length, radius);
         }
         light = new Color(color.r, color.g, color.b, 0);
      }
      @Override
      protected void draw(Batch batch) {
         if (isPortrait) {
            shapeRenderer.rect(0 - radius, radius - length / 2, radius, length - 2 * radius, light, getColor(), getColor(), light);
            shapeRenderer.rect(radius - radius, radius - length / 2, radius, length - 2 * radius, getColor(), light, light, getColor());
            shapeRenderer.triangle(
               0 - radius,
               radius - length / 2,
               radius - radius,
               radius - length / 2,
               radius - radius,
               0 - length / 2,
               light,
               getColor(),
               light);
            shapeRenderer.triangle(radius - radius, radius - length / 2, 2 * radius - radius, radius - length / 2, radius - radius, 0
               - length / 2, getColor(), light, light);
            shapeRenderer.triangle(
               0 - radius,
               length - radius - length / 2,
               radius - radius,
               length - radius - length / 2,
               radius - radius,
               length - length / 2,
               light,
               getColor(),
               light);
            shapeRenderer.triangle(radius - radius, length - radius - length / 2, 2 * radius - radius, length - radius - length / 2, radius
               - radius, length - length / 2, getColor(), light, light);
         } else {
            shapeRenderer.rect(radius - length / 2, 0 - radius, length - 2 * radius, radius, light, light, getColor(), getColor());
            shapeRenderer.rect(radius - length / 2, radius - radius, length - 2 * radius, radius, getColor(), getColor(), light, light);
            shapeRenderer.triangle(0 - length / 2, radius - radius, radius - length / 2, radius - radius, radius - length / 2, 2 * radius
               - radius, light, getColor(), light);
            shapeRenderer.triangle(
               0 - length / 2,
               radius - radius,
               radius - length / 2,
               radius - radius,
               radius - length / 2,
               0 - radius,
               light,
               getColor(),
               light);
            shapeRenderer.triangle(length - radius - length / 2, radius - radius, length - length / 2, radius - radius, length
               - radius
               - length / 2, radius * 2 - radius, getColor(), light, light);
            shapeRenderer.triangle(length - radius - length / 2, radius - radius, length - length / 2, radius - radius, length
               - radius
               - length / 2, 0 - radius, getColor(), light, light);
         }
      }
   }
   public static class Pentagram extends MyShapeActor {
      private float radius;
      private Color light;
      public Pentagram(float x, float y, float radius, Color color, ShapeRenderer shapeRenderer) {
         super(shapeRenderer);
         this.radius = radius / (4 * 0.9510565f * 0.9510565f - 1);
         setColor(color);
         this.light = new Color(color.r, color.g, color.b, 0);
         setOrigin(Align.center);
         setTouchable(Touchable.disabled);
         setPosition(x, y, Align.center);
      }
      @Override
      protected void draw(Batch batch) {
         for (int i = 0; i < 5; i++) {
            shapeRenderer.rotate(0, 0, 1, 72);
            shapeRenderer.triangle(
               -radius * 0.5877852f,
               radius * 0.809017f,
               0,
               0,
               radius * 0.5877852f,
               radius * 0.809017f,
               getColor(),
               getColor(),
               getColor());
            shapeRenderer.triangle(
               -radius * 0.5877852f,
               radius * 0.809017f,
               0,
               radius * 0.809017f + 2 * radius * 0.9510565f * 0.9510565f,
               radius * 0.5877852f,
               radius * 0.809017f,
               getColor(),
               light,
               getColor());
         }
      }
   }
   public static class SunLight extends MyShapeActor {
      private Color color1, color2, color3, color4, color5, color6, color7, color8;
      public SunLight(ShapeRenderer shapeRenderer) {
         super(shapeRenderer);
         setSize(320, 320);
         setPosition(MyGame.worldWidth / 2, MyGame.worldHeight / 2, Align.center);
         color1 = new Color(1, 0.98f, 0.773f, 1);
         color2 = new Color(1, 0.98f, 0.773f, 0);
         color3 = new Color(1, 0.494f, 0, 1);
         color4 = new Color(1, 0.494f, 0, 0);
         color5 = new Color(1, 1, 0, 1);
         color6 = new Color(1, 1, 0, 0);
         color7 = new Color(1, 1, 1, 1);
         color8 = new Color(1, 1, 1, 0);
         setOrigin(Align.center);
         setName("sunLight");
         setVisible(false);
         setTouchable(Touchable.disabled);
      }
      @Override
      public void setVisible(boolean visible) {
         super.setVisible(visible);
         if (visible) {
            setColor(1, 1, 1, 1);
            addAction(Actions.parallel(Actions.forever(Actions.rotateBy(15, 0.3f)), Actions.fadeOut(0.3f)));
         } else {
            clearActions();
         }
      }
      @Override
      protected void draw(Batch batch) {
         color1 = color1.set(1, 0.98f, 0.773f, getColor().a);
         color3 = color3.set(1, 0.494f, 0, getColor().a * 2);
         color5 = color5.set(1, 1, 0, getColor().a * 2);
         color7 = color7.set(1, 1, 1, getColor().a * 2);
         for (int i = 0; i < 16; i++) {
            if (i % 2 == 0) {
               shapeRenderer.rotate(0, 0, 1, 45);
               shapeRenderer.triangle(0, 0, -getWidth() * 0.19891f, getHeight(), getWidth() * 0.19891f, getHeight(), color1, color2, color2);
            } else {
               shapeRenderer.rotate(0, 0, 1, 40);
               shapeRenderer.triangle(0, 0, -getWidth() * 0.19891f, getHeight(), getWidth() * 0.19891f, getHeight(), color1, color2, color2);
               shapeRenderer.rotate(0, 0, 1, -40);
            }
         }
         for (int i = 0; i < 30; i++) {
            shapeRenderer.rotate(0, 0, 1, 12);
            shapeRenderer.triangle(0, 0, -250 * 0.10510423345f, 250, 250 * 0.10510423345f, 250, color3, color4, color4);
            shapeRenderer.triangle(0, 0, -170 * 0.10510423345f, 170, 170 * 0.10510423345f, 170, color5, color6, color6);
            shapeRenderer.triangle(0, 0, -150 * 0.10510423345f, 150, 150 * 0.10510423345f, 150, color7, color8, color8);
         }
      }
   }
   public static class ColorBackground extends MyShapeActor {
      public ColorBackground(ShapeRenderer shapeRenderer) {
         super(shapeRenderer);
         setBounds(MyGame.offX, MyGame.offY, MyGame.worldWidth - 2 * MyGame.offX, MyGame.worldHeight - 2 * MyGame.offY);
         setColor(new Color(0, 0, 0, 0.5f));
      }
      public ColorBackground(ShapeRenderer shapeRenderer, Color color) {
         super(shapeRenderer);
         setBounds(MyGame.offX, MyGame.offY, MyGame.worldWidth - 2 * MyGame.offX, MyGame.worldHeight - 2 * MyGame.offY);
         setColor(color);
      }
      @Override
      protected void draw(Batch batch) {
         shapeRenderer.setColor(getColor());
         shapeRenderer.rect(0, 0, getWidth(), getHeight());
      }
   }
   public static class AroundLight extends MyShapeActor {
      private Color light;
      private float widthVer, heightHor, radius;
      public AroundLight(ShapeRenderer shapeRenderer) {
         super(shapeRenderer);
         setColor(new Color(1, 0.745f, 0.133f, 1f));
         light = new Color(1, 0.745f, 0.133f, 0f);
         radius = 128;
         widthVer = MyGame.worldWidth - 2 * MyGame.offX - 2 * radius;
         heightHor = MyGame.worldHeight - 2 * MyGame.offY - 2 * radius;
         addAction(Actions.forever(Actions.sequence(Actions.fadeOut(0.2f), Actions.fadeIn(0.2f))));
      }
      @Override
      protected void draw(Batch batch) {
         shapeRenderer.triangle(
            MyGame.offX,
            MyGame.offY,
            MyGame.offX + radius,
            MyGame.offY,
            MyGame.offX + radius,
            MyGame.offY + radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.offX,
            MyGame.offY,
            MyGame.offX,
            MyGame.offY + radius,
            MyGame.offX + radius,
            MyGame.offY + radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.offX,
            MyGame.worldHeight - MyGame.offY - radius,
            MyGame.offX,
            MyGame.worldHeight - MyGame.offY,
            MyGame.offX + radius,
            MyGame.worldHeight - MyGame.offY - radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.offX,
            MyGame.worldHeight - MyGame.offY,
            MyGame.offX + radius,
            MyGame.worldHeight - MyGame.offY,
            MyGame.offX + radius,
            MyGame.worldHeight - MyGame.offY - radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.worldHeight - MyGame.offY,
            MyGame.worldWidth - MyGame.offX,
            MyGame.worldHeight - MyGame.offY,
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.worldHeight - MyGame.offY - radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.worldWidth - MyGame.offX,
            MyGame.worldHeight - MyGame.offY,
            MyGame.worldWidth - MyGame.offX,
            MyGame.worldHeight - MyGame.offY - radius,
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.worldHeight - MyGame.offY - radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.offY,
            MyGame.worldWidth - MyGame.offX,
            MyGame.offY,
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.offY + radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.triangle(
            MyGame.worldWidth - MyGame.offX,
            MyGame.offY,
            MyGame.worldWidth - MyGame.offX,
            MyGame.offY + radius,
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.offY + radius,
            getColor(),
            getColor(),
            light);
         shapeRenderer.rect(MyGame.offX, MyGame.offY + radius, radius, heightHor, getColor(), light, light, getColor());
         shapeRenderer.rect(MyGame.offX + radius, MyGame.offY, widthVer, radius, getColor(), getColor(), light, light);
         shapeRenderer.rect(
            MyGame.offX + radius,
            MyGame.worldHeight - MyGame.offY - radius,
            widthVer,
            radius,
            light,
            light,
            getColor(),
            getColor());
         shapeRenderer.rect(
            MyGame.worldWidth - MyGame.offX - radius,
            MyGame.offY + radius,
            radius,
            heightHor,
            light,
            getColor(),
            getColor(),
            light);
    }
  }
}