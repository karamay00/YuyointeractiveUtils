package com.yuyointeractive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by tian on 2016/10/10.
 */

public class RoundCircleActor extends TImage {
    //our grass texture
    private Texture tex0;

    //our dirt texture
    private Texture tex1;

    //our mask texture
    private Texture mask;

    //our program
    private ShaderProgram program;

    private DrawablePixmap drawblePixmap;
    private int r = 10;

    private static final String vertext = "uniform mat4 u_projTrans;\n" +
        "attribute vec4 a_position;\n" +
        "attribute vec2 a_texCoord0;\n" +
        "attribute vec4 a_color;\n" +
        "\n" +
        "varying vec4 v_color;\n" +
        "varying vec2 v_texCoords;\n" +
        "\n" +
        "void main() {\n" +
        "    v_color = a_color;\n" +
        "    v_texCoords = a_texCoord0;\n" +
        "    gl_Position = u_projTrans * a_position;\n" +
        "}";

    private static final String fragment = "#ifdef GL_ES\n" +
        "#define LOWP lowp\n" +
        "precision mediump float;\n" +
        "#else\n" +
        "#define LOWP\n" +
        "#endif\n" +
        "\n" +
        "varying LOWP vec4 v_color;\n" +
        "varying vec2 v_texCoords;\n" +
        "uniform sampler2D u_texture;\n" +
        "uniform sampler2D u_texture1;\n" +
        "uniform sampler2D u_mask;\n" +
        "\n" +
        "void main(void) {\n" +
        "     LOWP vec4 texColor0 = texture2D(u_texture, v_texCoords);\n" +
        "     LOWP vec4 texColor1 = texture2D(u_texture1, v_texCoords);\n" +
        "     LOWP vec4 maskColor = texture2D(u_mask, v_texCoords);\n" +
        "\n" +
        "     if(maskColor.a > 0.0){\n" +
        "           gl_FragColor = v_color * vec4(texColor1.r, texColor1.g, texColor1.b, texColor1.a);\n" +
        "     } else {\n" +
        "           gl_FragColor = vec4(0.0,0.0,0.0,0.0) + (texColor0 + texColor1 + maskColor) - (texColor0 + texColor1 + maskColor);\n" +
        "     }\n" +
        "}\n" +
        "\n";

    public RoundCircleActor(Texture hair) {
        this.tex1 = hair;
        setSize(tex1.getWidth(), tex1.getHeight());

        Pixmap pixmap = new Pixmap(tex1.getWidth(), tex1.getHeight(), Pixmap.Format.Alpha);
        pixmap.fill();

        tex0 = new Texture(pixmap);
        mask = new Texture(pixmap);

        pixmap.dispose();

        program = new ShaderProgram(vertext, fragment);
        if (!program.isCompiled()) {
            Gdx.app.error("compile error:", program.getLog());
        }

        program.begin();
        program.setUniformi("u_texture1", 1);
        program.setUniformi("u_mask", 2);
        program.end();

        drawblePixmap = new DrawablePixmap(mask);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawblePixmap.update();

        tex1.bind(1);
        mask.bind(2);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        ShaderProgram tmp = batch.getShader();
        batch.setShader(program);
        batch.draw(tex0, getX(), getY(), getWidth(), getHeight());
        batch.setShader(tmp);
    }

    private class DrawablePixmap implements Disposable {
        private Color drawColor = new Color(1f, 1f, 1f, 1f);

        private Pixmap pixmap;
        private boolean dirty = false;
        Texture texture;


        public DrawablePixmap(Texture texture) {
            this.texture = texture;
            this.pixmap = getPixmapRoundedRectangle((int) getWidth(), (int) getHeight(), r, drawColor.toIntBits());
            this.dirty = true;
        }


        public void update() {
            if (dirty) {
                texture.draw(pixmap, 0, 0);
                dirty = false;
            }
        }


        @Override
        public void dispose() {
            texture.dispose();
            pixmap.dispose();
        }


        public Pixmap getPixmapRoundedRectangle(int width, int height, int radius, int color) {
            pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);

            pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - 2 * radius);
            pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2 * radius, pixmap.getHeight());
            pixmap.fillCircle(radius, radius, radius);
            pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);
            pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);
            pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
            return pixmap;
        }
    }
}
