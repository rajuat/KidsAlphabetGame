package com.itservz.android.mayekid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.itservz.android.mayekid.mayek.MayekDrawActivity;
import com.itservz.android.mayekid.mayek.MayekDrawView;
import com.itservz.android.mayekid.picture.PictureDrawView;

/**
 * Created by raju.athokpam on 08-09-2016.
 */
public class BaseDrawView extends View {

    protected Canvas drawCanvas;
    protected Paint drawPaint;
    protected Path drawPath;
    protected float brushSize, lastBrushSize;
    protected int paintColor = 0xFFFF0000;
    protected int paintAlpha = 230;
    protected boolean erase = false;
    private float touchX;
    private float touchY;
    private float radius = 50;

    public BaseDrawView(Context context) {
        super(context);
    }

    public BaseDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void brushSizeAction(Activity activity) {
        final float smallBrush = getResources().getInteger(R.integer.small_size);
        final float mediumBrush = getResources().getInteger(R.integer.medium_size);
        final float largeBrush = getResources().getInteger(R.integer.large_size);

        final Dialog brushDialog = new Dialog(activity);
        brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        brushDialog.setContentView(R.layout.brush_chooser);
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErase(false);
                setBrushSize(smallBrush);
                setLastBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErase(false);
                setBrushSize(mediumBrush);
                setLastBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErase(false);
                setBrushSize(largeBrush);
                setLastBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    public void changeOpacity(Activity activity) {
        final Dialog seekDialog = new Dialog(activity);
        seekDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        seekDialog.setContentView(R.layout.opacity_chooser);
        final TextView seekTxt = (TextView) seekDialog.findViewById(R.id.opq_txt);
        //R.color.g1, R.color.g2, R.color.g3, R.color.g4, R.color.g5, R.color.g6, R.color.g7, R.color.g8, R.color.g9, R.color.g10
        LinearGradient test = new LinearGradient(0.f, 0.f, 700.f, 0.0f,
                new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        final SeekBar seekOpq = (SeekBar) seekDialog.findViewById(R.id.opacity_seek);
        seekOpq.setProgressDrawable((Drawable) shape);
        seekOpq.setMax(100);
        int currLevel = getPaintAlpha();
        seekTxt.setText(currLevel + "%");
        seekOpq.setProgress(currLevel);

        seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekTxt.setText(Integer.toString(progress) + "%");
                setPaintAlpha(seekOpq.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });
        seekDialog.show();
    }

    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize) {
        lastBrushSize = lastSize;
    }

    public int getPaintAlpha() {
        return Math.round((float) paintAlpha / 255 * 100);
    }

    public void setPaintAlpha(int newAlpha) {
        paintAlpha = Math.round((float) newAlpha / 100 * 255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setColor(String newColor) {
        invalidate();
        if (newColor.startsWith("#")) {
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);
            drawPaint.setShader(null);
        } else {
            int patternID = getResources().getIdentifier(
                    newColor, "drawable", "com.itservz.android.mayekid");
            Bitmap patternBMP = BitmapFactory.decodeResource(getResources(), patternID);
            BitmapShader patternBMPshader = new BitmapShader(patternBMP,
                    Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            drawPaint.setColor(0xFFFFFFFF);
            drawPaint.setShader(patternBMPshader);
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

}
