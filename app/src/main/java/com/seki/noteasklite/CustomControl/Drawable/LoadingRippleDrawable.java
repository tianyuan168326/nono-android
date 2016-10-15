package com.seki.noteasklite.CustomControl.Drawable;
import android.graphics.drawable.Drawable;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.graphics.LinearGradient;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
public class LoadingRippleDrawable extends Drawable {
private static final float[] VIEW_BOX = { 0.000000f, 0.000000f, 100.000000f, 100.000000f };
private RectF rect = new RectF();
private Matrix matrix = new Matrix();
private Shader shader;
private Path p = new Path();
private Paint paint = new Paint();
@Override
public void draw(Canvas canvas) {
paint.setAntiAlias(true);
float viewBoxWidth = VIEW_BOX[2];
float viewBoxHeight = VIEW_BOX[3];
Rect bounds = getBounds();
if (viewBoxHeight <= 0 || viewBoxWidth <= 0 || bounds.width() <= 0 || bounds.height() <= 0) {
return;
}
canvas.save();
float viewBoxRatio = viewBoxWidth / (float) viewBoxHeight;
float boundsRatio = bounds.width() / (float) bounds.height();
float factorScale;
if (boundsRatio > viewBoxRatio) {
 // canvas larger than viewbox
 factorScale = bounds.height() / (float) viewBoxHeight;
} else {
 // canvas higher (or equals) than viewbox
 factorScale = bounds.width() / (float) viewBoxWidth;
}
int newViewBoxHeight = Math.round(factorScale * viewBoxHeight);
int newViewBoxWidth = Math.round(factorScale * viewBoxWidth);
int marginX = bounds.width() - newViewBoxWidth;
int marginY = bounds.height() - newViewBoxHeight;
canvas.translate(bounds.left, bounds.top);
canvas.translate(Math.round(marginX / 2f), Math.round(marginY / 2f));
canvas.clipRect(0, 0, newViewBoxWidth, newViewBoxHeight);
canvas.translate(-Math.round(factorScale * VIEW_BOX[0]), -Math.round(factorScale * VIEW_BOX[1]));
paint.setAlpha(255);
paint.setAlpha(255);
paint.setShader(null);
paint.setAlpha(255);
paint.setAlpha(255);
paint.setAlpha(255);
paint.setShader(null);
paint.setColor(-5263433);
paint.setAlpha(255);
paint.setStrokeWidth(6.000000f);
paint.setStrokeCap(Paint.Cap.ROUND);
paint.setStyle(Paint.Style.STROKE);
canvas.drawCircle(factorScale * 50.000000f, factorScale * 50.000000f, factorScale * 40.000000f, paint);
paint.setAlpha(255);
paint.setAlpha(255);
paint.setAlpha(255);
paint.setAlpha(255);
paint.setShader(null);
paint.setColor(-10682410);
paint.setAlpha(255);
paint.setStrokeWidth(6.000000f);
paint.setStrokeCap(Paint.Cap.ROUND);
paint.setStyle(Paint.Style.STROKE);
canvas.drawCircle(factorScale * 50.000000f, factorScale * 50.000000f, factorScale * 40.000000f, paint);
paint.setAlpha(255);
canvas.restore();
}
@Override public void setAlpha(int alpha) { }
@Override public void setColorFilter(ColorFilter cf) { }
@Override public int getOpacity() { return 0; }
@Override public int getMinimumHeight() { return 10; }
@Override public int getMinimumWidth() { return 10; }
}

