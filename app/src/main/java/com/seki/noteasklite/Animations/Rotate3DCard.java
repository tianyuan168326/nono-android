package com.seki.noteasklite.Animations;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by yuan on 2016/1/11.
 */
public class Rotate3DCard extends Animation {
    private float mcenterX = 0.0f;
    private float mcenterY = 0.0f;
    public void setCenter(float mcenterX,float mcenterY){
        this.mcenterX = mcenterX;
        this.mcenterY = mcenterY;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        Camera camera = new Camera();
        camera.save();
        camera.rotateY(-180 * interpolatedTime);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-mcenterX,-mcenterY);
        matrix.postTranslate(mcenterX,mcenterY);
    }
}
