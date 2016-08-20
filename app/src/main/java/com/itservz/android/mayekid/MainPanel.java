package com.itservz.android.mayekid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.itservz.android.mayekid.mayek.MayekActivity;
import com.itservz.android.mayekid.mayek.MayekBoard;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MainPanel extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap mayekBoardBitmap;

    public MainPanel(Context context) {
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
        mayekBoardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.board);
        //scaledBitmap = Bitmap.createScaledBitmap(mayekBoardBitmap, 100, 100, true);
        MayekBoard mayekBoard = new MayekBoard(mayekBoardBitmap);
        Canvas canvas = surfaceHolder.lockCanvas();
        mainBackground.draw(canvas);
        mayekBoard.draw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("action down" + x + "," + y);
                int right = MayekBoard.left + mayekBoardBitmap.getWidth();
                int bottom =  MayekBoard.top + mayekBoardBitmap.getHeight();
                System.out.println("left top right bottom - 250, 100, " + right + ", " + bottom);
                if (x >= MayekBoard.left && x <= right && y >= MayekBoard.top && y <= bottom) {
                    Intent intent = new Intent(getContext(), MayekActivity.class);
                    getContext().startActivity(intent);
                }
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
