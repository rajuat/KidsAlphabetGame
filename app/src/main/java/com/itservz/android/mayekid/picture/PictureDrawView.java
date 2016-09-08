package com.itservz.android.mayekid.picture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.itservz.android.mayekid.BaseDrawView;
import com.itservz.android.mayekid.R;


public class PictureDrawView extends BaseDrawView {
    private Context context;
    private Paint canvasPaint;
    private Bitmap canvasBitmap;
    private int picture;
    Bitmap pictureBitMap;
    int x, y;

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public PictureDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupDrawing();
    }

    public PictureDrawView(Context context, int res) {
        super(context);
        this.context = context;
        this.picture = res;
        setupDrawing();
    }

    private void setupDrawing() {
        brushSize = getResources().getInteger(R.integer.large_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawBitmap(pictureBitMap, x, y, null);
        canvas.drawPath(drawPath, drawPaint);
    }

}

