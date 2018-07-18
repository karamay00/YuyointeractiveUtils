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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.yuyointeractive.utils.MyActor.MyButton;

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
  
  
  public static class MyButtonGroup <T extends MyButton> {
	  private final Array<T> buttons = new Array();
		private Array<T> checkedButtons = new Array(1);
		private int minCheckCount, maxCheckCount = 1;
		private boolean uncheckLast = true;
		private T lastChecked;

		public MyButtonGroup () {
			minCheckCount = 1;
		}

		public MyButtonGroup (T... buttons) {
			minCheckCount = 0;
			add(buttons);
			minCheckCount = 1;
		}

		public void add (T button) {
			if (button == null) throw new IllegalArgumentException("button cannot be null.");
			button.buttonGroup = null;
			boolean shouldCheck = button.isChecked() || buttons.size < minCheckCount;
			button.setChecked(false);
			button.buttonGroup = this;
			buttons.add(button);
			button.setChecked(shouldCheck);
		}

		public void add (T... buttons) {
			if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
			for (int i = 0, n = buttons.length; i < n; i++)
				add(buttons[i]);
		}

		public void remove (T button) {
			if (button == null) throw new IllegalArgumentException("button cannot be null.");
			button.buttonGroup = null;
			buttons.removeValue(button, true);
			checkedButtons.removeValue(button, true);
		}

		public void remove (T... buttons) {
			if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
			for (int i = 0, n = buttons.length; i < n; i++)
				remove(buttons[i]);
		}

		public void clear () {
			buttons.clear();
			checkedButtons.clear();
		}

		/** Sets the first {@link TextButton} with the specified text to checked. */
		public void setChecked (String text) {
			if (text == null) throw new IllegalArgumentException("text cannot be null.");
			for (int i = 0, n = buttons.size; i < n; i++) {
				T button = buttons.get(i);
				//if (button instanceof TextButton && text.contentEquals(((TextButton)button).getText())) {
				//	button.setChecked(true);
				//	return;
				//}
			}
		}

		/** Called when a button is checked or unchecked. If overridden, generally changing button checked states should not be done
		 * from within this method.
		 * @return True if the new state should be allowed. */
		protected boolean canCheck (T button, boolean newState) {
			if (button.isChecked == newState) return false;

			if (!newState) {
				// Keep button checked to enforce minCheckCount.
				if (checkedButtons.size <= minCheckCount) return false;
				checkedButtons.removeValue(button, true);
			} else {
				// Keep button unchecked to enforce maxCheckCount.
				if (maxCheckCount != -1 && checkedButtons.size >= maxCheckCount) {
					if (uncheckLast) {
						int old = minCheckCount;
						minCheckCount = 0;
						lastChecked.setChecked(false);
						minCheckCount = old;
					} else
						return false;
				}
				checkedButtons.add(button);
				lastChecked = button;
			}

			return true;
		}

		/** Sets all buttons' {@link Button#isChecked()} to false, regardless of {@link #setMinCheckCount(int)}. */
		public void uncheckAll () {
			int old = minCheckCount;
			minCheckCount = 0;
			for (int i = 0, n = buttons.size; i < n; i++) {
				T button = buttons.get(i);
				button.setChecked(false);
			}
			minCheckCount = old;
		}

		/** @return The first checked button, or null. */
		public T getChecked () {
			if (checkedButtons.size > 0) return checkedButtons.get(0);
			return null;
		}

		/** @return The first checked button index, or -1. */
		public int getCheckedIndex () {
			if (checkedButtons.size > 0) return buttons.indexOf(checkedButtons.get(0), true);
			return -1;
		}

		public Array<T> getAllChecked () {
			return checkedButtons;
		}

		public Array<T> getButtons () {
			return buttons;
		}

		/** Sets the minimum number of buttons that must be checked. Default is 1. */
		public void setMinCheckCount (int minCheckCount) {
			this.minCheckCount = minCheckCount;
		}

		/** Sets the maximum number of buttons that can be checked. Set to -1 for no maximum. Default is 1. */
		public void setMaxCheckCount (int maxCheckCount) {
			if (maxCheckCount == 0) maxCheckCount = -1;
			this.maxCheckCount = maxCheckCount;
		}

		/** If true, when the maximum number of buttons are checked and an additional button is checked, the last button to be checked
		 * is unchecked so that the maximum is not exceeded. If false, additional buttons beyond the maximum are not allowed to be
		 * checked. Default is true. */
		public void setUncheckLast (boolean uncheckLast) {
			this.uncheckLast = uncheckLast;
		}
	}
  
  public static void loadTextureFromUrl(final String url, final LoadTextureFromUrlFinished<Texture> loadTextureFromUrlFinished) {
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
              Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
              texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
              loadTextureFromUrlFinished.toDo(texture);
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
  public static interface LoadTextureFromUrlFinished <Texture>{
    public void toDo(Texture texture);
  }
}


