package com.yuyointeractive.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Administrator on 2017/1/4.
 */
public class T {
  Actor actor;
  private static T instance = null;
  public T() {
  }
  public static T at(Actor actor) {
    if (instance == null) {
      instance = new T();
    }
    instance.actor = actor;
    return instance;
  }
  public T addTo(Group parent) {
    parent.addActor(actor);
    return this;
  }
  public T addTo(Stage stage) {
    stage.addActor(actor);
    return this;
  }
  public T size(float w, float h) {
    actor.setSize(w, h);
    return this;
  }
  public T size(Actor like) {
    actor.setSize(like.getWidth(), like.getHeight());
    return this;
  }
  public T pos(float x, float y) {
    actor.setPosition(x, y);
    return this;
  }
  public T posCenter(float x, float y) {
    actor.setPosition(x, y, Align.center);
    return this;
  }
  public T pos(float x, float y, int align) {
    actor.setPosition(x, y, align);
    return this;
  }
  public T toStageCenter(Stage stage) {
    actor.setPosition(stage.getWidth() / 2f, stage.getHeight() / 2f, Align.center);
    return this;
  }
  public T toStageXCenter(Stage stage) {
    // actor.setX(stage.getWidth() / 2f - actor.getWidth() / 2f);
    x(stage.getWidth() / 2f - actor.getWidth() / 2f);
    return this;
  }
  public T x(float x) {
    actor.setX(x);
    return this;
  }
  public T y(float y) {
    actor.setY(y);
    return this;
  }
  public T debug() {
    if (actor instanceof Group) {
      ((Group) actor).debugAll();
    } else {
      actor.debug();
    }
    return this;
  }
  public T drag() {
    MyWidget.setTouchTrack(actor);
    return this;
  }
  public T offsetY(float offset) {
    actor.setY(actor.getY() + offset);
    return this;
  }
  public T offsetX(float offset) {
    actor.setX(actor.getX() + offset);
    return this;
  }
  public T hide() {
    actor.setVisible(false);
    return this;
  }
  public T visiable() {
    actor.setVisible(true);
    return this;
  }
  public T pos(Actor ac) {
    actor.setPosition(ac.getX(), ac.getY());
    return this;
  }
  public T name(Object name) {
    actor.setName(name.toString());
    return this;
  }
  public T print() {
    LogHelper.log(actor.getX() + " " + actor.getY());
    return this;
  }
  public T pos(Vector2 mMajPos) {
    actor.setPosition(mMajPos.x, mMajPos.y);
    return this;
  }
  public float centerX() {
    return actor.getX(Align.center);
  }
  public float centerY() {
    return actor.getY(Align.center);
  }
  public T originCenter() {
    actor.setOrigin(Align.center);
    return this;
  }
  public T scale(float v) {
    actor.setScale(v);
    return this;
  }
  public T size(float v) {
    actor.setSize(actor.getWidth() * v, actor.getHeight() * v);
    return this;
  }
}
