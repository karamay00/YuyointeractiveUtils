package com.yuyointeractive.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Administrator on 2016/11/21.
 * 进度条 传入底图 上面一层 字体
 */

public class ProgressActor extends Actor {
    private TextureRegion bg, progress;
    private float value = 0;
    private float max = 100f;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    public ProgressActor(Texture bg, Texture progress, BitmapFont font) {
      this(new TextureRegion(bg), new TextureRegion(progress), font);
  }
    
    public ProgressActor(TextureRegion bg, TextureRegion progress, BitmapFont font) {
        this.bg = bg;
        this.progress = progress;
        setSize(bg.getRegionWidth(), bg.getRegionHeight());
        bitmapFont = font;
        glyphLayout = new GlyphLayout();
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(bg, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.flush();
        if (clipBegin(getX() + 2, getY() + 1, getWidth() * value / max, getHeight())) {
            batch.draw(progress, getX() + 2, getY() + 1 + getHeight() / 2f - progress.getRegionHeight() / 2f, getOriginX(), getOriginY(), progress.getRegionWidth(), progress.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
            batch.flush();
            clipEnd();
        }

        if (bitmapFont != null) {
            glyphLayout.setText(bitmapFont, (int) value + "/" + (int) max);
            bitmapFont.draw(batch, glyphLayout, getX() + getWidth() / 2f - glyphLayout.width / 2f, getY() + getHeight() - 3);
        }
    }
}
