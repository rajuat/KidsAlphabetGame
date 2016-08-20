package com.itservz.android.mayekid.mayek;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.itservz.android.mayekid.MainBackground;
import com.itservz.android.mayekid.R;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MayekPanel extends SurfaceView implements SurfaceHolder.Callback{

    public MayekPanel(Context context) {
        super(context);
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        System.out.println("surface created");
        MainBackground mainBackground = new MainBackground(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        Canvas canvas = surfaceHolder.lockCanvas();
        mainBackground.draw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
