package com.itservz.android.mayekid;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MainBackground {
    private Bitmap image;
    public MainBackground(Bitmap res){
        this.image = res;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, 0, 0, null);
    }
}
