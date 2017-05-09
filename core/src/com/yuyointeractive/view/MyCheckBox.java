package com.yuyointeractive.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.yuyointeractive.utils.MyGame;

import net.mwplay.nativefont.NativeLabel;

/**
 * Created by tian on 2016/11/7.
 */

public class MyCheckBox extends TImage {
    private boolean checked = false;
    private Texture gou;
    private Array<TOnCheckedListener> checkedListeners;
    private NativeLabel nativeLabel;
    private TImage right;
    private boolean clickChange = true;
    private float iconpadding = 0;

    public MyCheckBoxGroup buttonGroup;

    public MyCheckBox(Texture di, Texture gou) {
        super(di);
        this.gou = gou;
        checkedListeners = new Array<>();

        nativeLabel = new NativeLabel("", MyGame.getDefaultFont());

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (clickChange) {
                    //checked = !checked;
                    setChecked(!isChecked());
                    if (checkedListeners != null) {
                        for (TOnCheckedListener checkedListener : checkedListeners) {
                            checkedListener.onChecked(isChecked());
                        }
                    }
                }
            }
        });

        nativeLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (clickChange) {
                    setChecked(!isChecked());
                    if (checkedListeners != null) {
                        for (TOnCheckedListener checkedListener : checkedListeners) {
                            checkedListener.onChecked(isChecked());
                        }
                    }
                }
            }
        });

        right = new TImage();
        this.right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (clickChange) {
                    setChecked(!isChecked());
                    if (checkedListeners != null) {
                        for (TOnCheckedListener checkedListener : checkedListeners) {
                            checkedListener.onChecked(isChecked());
                        }
                    }
                }
            }
        });

        isColorButton(new TClickListener() {
            @Override
            public void onClicked(TImage image) {

            }
        });
    }

    public void setClickChange(boolean clickChange) {
        this.clickChange = clickChange;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor actor = super.hit(x, y, touchable);
        if (actor != null) {
            return actor;
        }

        if ((x >= getWidth() && x < getWidth() + nativeLabel.getWidth())
                && (y >= getHeight() / 2f - nativeLabel.getHeight() / 2f && y <= getHeight() / 2f + nativeLabel.getHeight() / 2f)) {
            return this;
        }

        if ((x >= getWidth() && x < getWidth() + right.getWidth() + iconpadding)
                && (y >= getHeight() / 2f - right.getHeight() / 2f && y <= getHeight() / 2f + right.getHeight() / 2f)) {
            return this;
        }

        return null;
    }

    @Override
    public TImage debug() {
        if (nativeLabel != null)
            nativeLabel.debug();

        if (right != null)
            right.debug();

        return super.debug();
    }

    public void setText(String text) {
        nativeLabel.setText(text);
        nativeLabel.size(nativeLabel.getPrefWidth(), nativeLabel.getPrefHeight());
    }

    public void setIcon(TImage image) {
        //this.right = image;
        this.right.setDrawable(image.getDrawable());
        this.right.setSize(image.width(), image.height());
    }

    TImage pressed, normal;

    public void setIcon(TImage normal, TImage pressed) {
        this.right.setDrawable(normal.getDrawable());
        this.right.setSize(normal.width(), normal.height());

        this.pressed = pressed;
        this.normal = normal;
    }

    public void setIconpadding(float iconpadding) {
        this.iconpadding = iconpadding;
    }

    public String getText() {
        return nativeLabel.getText().toString();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        nativeLabel.act(delta);
        right.act(delta);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        if (nativeLabel != null)
            shapes.rect(nativeLabel.getX(), nativeLabel.getY(), nativeLabel.getWidth(), nativeLabel.getHeight());

        if (right != null) {
            shapes.rect(right.getX() + iconpadding, right.getY(), right.getWidth(), right.getHeight());
        }
    }

    public static Vector2 gouOffset = Vector2.Zero;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isChecked()) {
            batch.draw(gou, centerX() - gou.getWidth() / 2f + gouOffset.x, centerY() - gou.getHeight() / 2f + gouOffset.y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0
                    , 0, (int) getWidth(), (int) getHeight(), false, false);
        }

        if (nativeLabel.getText().length() > 0) {
            nativeLabel.pos(getRight() + 1, getY() + getHeight() / 2f - nativeLabel.getHeight() / 2f);
            nativeLabel.draw(batch, parentAlpha);
        }

        if (right != null) {
            right.pos(getRight() + 1 + iconpadding, getY() + getHeight() / 2f - right.getHeight() / 2f);
            right.draw(batch, parentAlpha);
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (this.checked == checked) return;
        if (buttonGroup != null && !buttonGroup.canCheck(this, checked)) return;

        this.checked = checked;


        if (pressed != null && normal != null) {
            if (checked) {
                right.setDrawable(pressed.getDrawable());
            } else {
                right.setDrawable(normal.getDrawable());
            }
        }
    }

    public TImage getIcon() {
        return right;
    }

    public MyCheckBoxGroup getButtonGroup() {
        return buttonGroup;
    }

    public void addOnCheckedListener(TOnCheckedListener checkedListener) {
        this.checkedListeners.add(checkedListener);
    }

    public void setTextColor(Color color) {
        nativeLabel.setColor(color);
    }

    public interface TOnCheckedListener {
        public void onChecked(boolean checked);
    }
}
