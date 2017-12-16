package com.yuyointeractive.view;

import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.yuyointeractive.utils.LogHelper;

/**
 * Created by Administrator on 2017/8/1.
 * 斜切
 */

public class SkewGroup extends Group {
    float shearX, shearY;
    public SkewGroup(float shearX, float shearY){
        super();
        this.shearX = shearX;
        this.shearY = shearY;
    }

    @Override protected Matrix4 computeTransform() {
        Matrix4 matrix4 = super.computeTransform();
        Affine2 affine2 = new Affine2();
        affine2.setToTranslation(getX(), getY());
        affine2.shear(shearX, shearY);
        matrix4.setAsAffine(affine2);
        return matrix4;
    }
}
