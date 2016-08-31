package com.itservz.android.mayekid.mayek;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itservz.android.mayekid.R;


public class MayekDrawView extends View {
	private Context context;
	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private int paintColor = 0xFFFF0000, paintAlpha = 230;
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private float brushSize, lastBrushSize;
	private boolean erase=false;
	private boolean animate = false;
	private float radius = 50;
	private float touchX, touchY;
	private TextPaint textPaint;
	private String mayekName;

	public void setMayekName(String mayekName){
		this.mayekName = mayekName;
	}

	public MayekDrawView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		setupDrawing();
	}

	public MayekDrawView(Context context){
		super(context);
		this.context = context;
		setupDrawing();
	}

	private void setupDrawing(){
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

		textPaint = new TextPaint();
		textPaint.setColor(Color.YELLOW);
		textPaint.setShadowLayer(2.0f, 2.0f, 2.0f, Color.GREEN);
		textPaint.setTextSize(128);
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
		canvas.drawText(mayekName, ((canvas.getWidth()-224)/2), 112, textPaint);
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
			animate = true;
			drawPath.reset();
			break;
		default:
			return false;
		}
		invalidate();
		return true;

	}

	public void setColor(String newColor){
		invalidate();
		if(newColor.startsWith("#")){
			paintColor = Color.parseColor(newColor);
			drawPaint.setColor(paintColor);
			drawPaint.setShader(null);
		}
		else{
			int patternID = getResources().getIdentifier(newColor, "drawable", "com.itservz.android.mayekid");
			Bitmap patternBMP = BitmapFactory.decodeResource(getResources(), patternID);
			BitmapShader patternBMPshader = new BitmapShader(patternBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			drawPaint.setColor(0xFFFFFFFF);
			drawPaint.setShader(patternBMPshader);
		}
	}

	public void setBrushSize(float newSize){
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				newSize, getResources().getDisplayMetrics());
		brushSize=pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	public void setLastBrushSize(float lastSize){
		lastBrushSize=lastSize;
	}

	public float getLastBrushSize(){
		return lastBrushSize;
	}

	public void setErase(boolean isErase){
		erase=isErase;
		if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}

	public void startNew(){
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

	public int getPaintAlpha(){
		return Math.round((float)paintAlpha/255*100);
	}

	public void setPaintAlpha(int newAlpha){
		paintAlpha= Math.round((float)newAlpha/100*255);
		drawPaint.setColor(paintColor);
		drawPaint.setAlpha(paintAlpha);
	}

	@Override
	public boolean hasOverlappingRendering(){
		return false;
	}

}

