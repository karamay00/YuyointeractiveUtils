package com.yuyointeractive.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

final public class MyWidget {
  public static void setTouchTrack(final Actor actor) {
    float x = 0;
    float y = 0;
    setTouchTrack(actor, new Vector2(x, y));
  }
  private static void setTouchTrack(final Actor actor, final Vector2 v) {
    final String name = actor.getName() == null ? "null" : actor.getName();
    actor.setTouchable(Touchable.enabled);
    // if (!Settings.TEST_MODE)
    // return;
    actor.clearListeners();
    actor.addListener(new InputListener() {
      float startx, starty;
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        startx = x;
        starty = y;
        Gdx.app.log(name,
            "down: {" + (actor.getX() - v.x /*- MyGame.worldWidth / 2*/) + "," + (actor.getY() - v.y - MyGame.offY) + "}");
        actor.setDebug(true);
        return true;
      }
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        actor.moveBy(x - startx, y - starty);
      }
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log(name,
            "up: { " + (actor.getX() - v.x /*- MyGame.worldWidth / 2*/) + "f," + (actor.getY() - v.y /*- MyGame.offY*/) + "f }");
        Vector2 vv = new Vector2(startx, starty);
        vv.set(actor.localToParentCoordinates(v.cpy()));
        actor.setDebug(false);
      }
    });
  }
  public static class BezierMoveToAction extends MoveToAction {
    public Bezier<Vector2> bezier;
    public Vector2 out;
    public BezierMoveToAction(Vector2 startPoint, Vector2 midPoint, Vector2 endPoint) {
      bezier = new Bezier<Vector2>(startPoint, midPoint, endPoint);
      out = new Vector2();
    }
    public BezierMoveToAction(Vector2 startPoint, Vector2 midPoint, Vector2 endPoint, float duration) {
      this(startPoint, midPoint, endPoint);
      setDuration(duration);
    }
    @Override
    protected void update(float percent) {
      bezier.valueAt(out, percent);
      target.setPosition(out.x, out.y);
    }
  }
  // Check if Polygon intersects Rectangle
  public static boolean isCollision(Polygon p, Rectangle r) {
    Polygon rPoly = new Polygon(new float[]{0, 0, r.width, 0, r.width, r.height, 0, r.height});
    rPoly.setPosition(r.x, r.y);
    if (Intersector.overlapConvexPolygons(rPoly, p))
      return true;
    return false;
  }
  // Check if Polygon intersects Circle
  public static boolean isCollision(Polygon p, Circle c) {
    float[] vertices = p.getTransformedVertices();
    Vector2 center = new Vector2(c.x, c.y);
    float squareRadius = c.radius * c.radius;
    for (int i = 0, j = vertices.length; i < j; i += 2) {
      if (i == 0) {
        if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]),
            new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
          return true;
      } else {
        if (Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]),
            new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
          return true;
      }
    }
    return false;
  }
  public static void loadTextureFromUrl(final String url, final LoadTextureFromUrlFinished loadTextureFromUrlFinished) {
    new Thread() {
      @Override
      public void run() {
        try {
          InputStream is;
          ByteArrayOutputStream out;
          int length = 0;
          byte[] bs = new byte[1024];
          is = new URL(url).openStream();
          out = new ByteArrayOutputStream();
          while ((length = is.read(bs)) != -1) {
            out.write(bs, 0, length);
          }
          is.close();
          out.flush();
          final byte[] bytes = out.toByteArray();
          Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
              Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
              loadTextureFromUrlFinished.toDo(new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true)));
            }
          });
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      };
    }.start();
  }
  public static interface LoadTextureFromUrlFinished {
    public void toDo(Texture texture);
  }
}