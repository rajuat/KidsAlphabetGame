package com.itservz.android.mayekid.mayek;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
@Deprecated
public class MayekBoard {
    public final static int left = 250;
    public final static int top = 100;
    private Bitmap image;
    public MayekBoard(Bitmap res){
        this.image = res;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, left, top, null);
    }
}
