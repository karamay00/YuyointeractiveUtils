package com.yuyointeractive.view;

/**
 * Created by tian on 2016/11/6.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by tian on 2016/11/6.
 */

public class CircleMask extends TImage {
    TextureRegion textureRegion;
    ShapeRenderer shapeRenderer;
    
    public CircleMask(Texture texture){
      this(new TextureRegion(texture));
  }

    public CircleMask(TextureRegion textureRegion){
        this.textureRegion = textureRegion;
        shapeRenderer = new ShapeRenderer();
        setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public void setTexture(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        Gdx.gl.glClearDepthf(1f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX() + getWidth() / 2f, getY() + getWidth() / 2f, getWidth() / 2f);
        shapeRenderer.end();


        batch.begin();
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
        batch.end();

        batch.begin();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
}