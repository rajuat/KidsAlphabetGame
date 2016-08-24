package com.itservz.android.mayekid.mayek;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itservz.android.mayekid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MayekDrawActivity extends Activity implements View.OnClickListener {

    //custom drawing view
    private MayekDrawView currentDrawView;
    //buttons
    private ImageView currPaint, drawBtn, eraseBtn, newBtn, saveBtn, opacityBtn, nextBtn, previousBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;
    private int[] imageIds;
    private int imageId;
    Map<Integer, MayekDrawView> views = new HashMap<Integer, MayekDrawView>();
    private Animation animation;
    private View animatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mayek_draw);

        Intent intent = getIntent();
        imageId = intent.getIntExtra("imageId", 0);
        imageIds = intent.getIntArrayExtra("imageIds");

        //get drawing view
        currentDrawView = (MayekDrawView)findViewById(R.id.drawing1);

        float alpha = 0.9f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentDrawView.setAlpha(alpha);
        }
        currentDrawView.setBackgroundResource(imageId);
        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currentDrawView.setColor(currPaint.getTag().toString());
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageView)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        currentDrawView.setBrushSize(mediumBrush);

        //erase button
        eraseBtn = (ImageView)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageView)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageView)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //opacity
        opacityBtn = (ImageView)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        //previous button
        previousBtn = (ImageView)findViewById(R.id.previous_btn);
        previousBtn.setOnClickListener(this);

        //next
        nextBtn = (ImageView)findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);

        SeekBar backSeekBar = (SeekBar)findViewById(R.id.back_seek);
        backSeekBar.setProgress(100);
        //backSeekBar.setVisibility(SeekBar.INVISIBLE);

        animation  = AnimationUtils.loadAnimation(this, R.anim.paint_animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //user clicked paint
    public void paintClicked(View view){
        //use chosen color

        //set erase false
        currentDrawView.setErase(false);
        currentDrawView.setPaintAlpha(100);
        currentDrawView.setBrushSize(currentDrawView.getLastBrushSize());

        if(view != currPaint){
            ImageButton imageButton = (ImageButton)view;
            String color = view.getTag().toString();
            currentDrawView.setColor(color);
            //update ui
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){
        if(animatedView != null) {
            animatedView.clearAnimation();
        }
        if(view.getId()==R.id.draw_btn){
            animatedView = animate(view);
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(smallBrush);
                    currentDrawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(mediumBrush);
                    currentDrawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(largeBrush);
                    currentDrawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            animatedView = animate(view);
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            animatedView = animate(view);
            currentDrawView.startNew();

        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            animatedView = animate(view);
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    currentDrawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    /*String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), currentDrawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");*/
                    String imgSaved = savePicture(currentDrawView.getDrawingCache(),UUID.randomUUID().toString()+".png");
                    //feedback
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    currentDrawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.opacity_btn){
            animatedView = animate(view);
            //launch opacity chooser
            final Dialog seekDialog = new Dialog(this);
            seekDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seekDialog.setContentView(R.layout.opacity_chooser);
            //get ui elements
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            //R.color.g1, R.color.g2, R.color.g3, R.color.g4, R.color.g5, R.color.g6, R.color.g7, R.color.g8, R.color.g9, R.color.g10
            LinearGradient test = new LinearGradient(0.f, 0.f, 700.f, 0.0f,
                    new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                    null, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(test);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            seekOpq.setProgressDrawable( (Drawable)shape );
            //set max
            seekOpq.setMax(100);
            //show current level
            int currLevel = currentDrawView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);

            //update as user interacts
            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress)+"%");
                    currentDrawView.setPaintAlpha(seekOpq.getProgress());
                    //
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });
            //show dialog
            seekDialog.show();
        }
        else if(view.getId() == R.id.next_btn){
            animatedView = animate(view);
            for(int i = 0; i < imageIds.length; i++){
                if(imageId == imageIds[i] && i < imageIds.length-1){
                    imageId = imageIds[i+1];
                    break;
                }
            }
            currentDrawView.startNew();
            currentDrawView.setBackgroundResource(imageId);
        }
        else if(view.getId() == R.id.previous_btn){
            animatedView = animate(view);
            for(int i = 0; i < imageIds.length; i++){
                if(imageId == imageIds[i] && i > 0){
                    imageId = imageIds[i-1];
                    break;
                }
            }
            currentDrawView.startNew();
            currentDrawView.setBackgroundResource(imageId);
        }
    }


    private View animate(View imageView){
        imageView.startAnimation(animation);
        return imageView;
    }

    private String savePicture(Bitmap bm, String imgName) {
        String s = null;
        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            /**Update image to gallery**/
            s = MediaStore.Images.Media.insertImage(getContentResolver(),
                    f.getAbsolutePath(), f.getName(), f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}
