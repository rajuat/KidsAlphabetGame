package com.itservz.android.mayekid.mayek;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.itservz.android.mayekid.BaseDrawView;
import com.itservz.android.mayekid.R;


public class MayekDrawView extends BaseDrawView {
    private Context context;

    private Paint canvasPaint;
    private Bitmap canvasBitmap;
    private TextPaint textPaint;
    private String mayekName;

    public void setMayekName(String mayekName) {
        this.mayekName = mayekName;
    }

    public MayekDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupDrawing();
    }

    public MayekDrawView(Context context) {
        super(context);
        this.context = context;
        setupDrawing();
    }

    private void setupDrawing() {
        brushSize = getResources().getInteger(R.integer.medium_size);
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

        textPaint = new TextPaint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setShadowLayer(6.0f, 2.0f, 2.0f, Color.GREEN);
        textPaint.setTextSize(56);
        textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);

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
        canvas.drawPath(drawPath, drawPaint);
        canvas.drawText(mayekName, (canvas.getWidth() / 2) - 48, 64, textPaint);
    }

}

