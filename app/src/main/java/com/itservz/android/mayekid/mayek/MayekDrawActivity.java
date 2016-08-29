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
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itservz.android.mayekid.MayekCard;
import com.itservz.android.mayekid.MayekSoundPoolPlayer;
import com.itservz.android.mayekid.Mayeks;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.SoundPoolPlayer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

public class MayekDrawActivity extends Activity implements View.OnClickListener {

    //custom drawing view
    private MayekDrawView currentDrawView;
    //buttons
    private ImageView currPaint, drawBtn, soundBtn, newBtn, saveBtn, opacityBtn, nextBtn, previousBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;
    private int[] imageIds;
    private int imageId;
    private Animation animation;
    private View animatedView;
    private ViewFlipper viewFlipper;
    private AnimationSet animSet;
    private List<MayekCard> mayeks;
    private SoundPoolPlayer soundPoolPlayer;
    private MayekSoundPoolPlayer mayekSoundPoolPlayer;

    private void setFlipperImage(int res) {
        System.out.println("Set Filpper Called");
        MayekDrawView image = new MayekDrawView(getApplicationContext());
        image.setBackgroundResource(res);
        image.setTag(res);
        viewFlipper.addView(image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mayek_draw);
        //ads start
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id_mayek));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //ads end
        Intent intent = getIntent();
        imageId = intent.getIntExtra("imageId", 0);
        imageIds = intent.getIntArrayExtra("imageIds");

        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        for(int i = 0;  i < imageIds.length; i++){
            setFlipperImage(imageIds[i]);
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
        mayekSoundPoolPlayer = new MayekSoundPoolPlayer(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPoolPlayer.release();
        mayekSoundPoolPlayer.release();
    }

    private void init() {
        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageView)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //erase button
        soundBtn = (ImageView)findViewById(R.id.sound_btn);
        soundBtn.setOnClickListener(this);

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

        animation  = AnimationUtils.loadAnimation(this, R.anim.paint_animation);
        animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(1500);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        Animation slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        animSet.addAnimation(slowAnimation);
        initCurrentView();
    }

    private String getMayekName(int imageId){
        mayeks = Mayeks.getInstance().getCards();
        for(MayekCard mayek : mayeks){
            if(mayek.getRes() == imageId){
                return mayek.getTitle();
            }
        }
        return "";
    }

    private void initCurrentView() {
        currentDrawView = (MayekDrawView) viewFlipper.findViewWithTag(imageId);
        currentDrawView.setMayekName(getMayekName(imageId));
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(currentDrawView));

        float alpha = 0.9f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentDrawView.setAlpha(alpha);
        }
        //currentDrawView.setBackgroundResource(imageId);
        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currentDrawView.setColor(currPaint.getTag().toString());
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        currentDrawView.setBrushSize(mediumBrush);
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
        currentDrawView.clearAnimation();
        if(animatedView != null) {
            animatedView.clearAnimation();
        }
        if(view.getId()==R.id.draw_btn){
            soundPoolPlayer.playShortResource(R.raw.click);
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
        else if(view.getId()==R.id.sound_btn){

            animatedView = animate(view);
            mayekSoundPoolPlayer.playShortResource(imageId);

        }
        else if(view.getId()==R.id.new_btn){
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            currentDrawView.startNew();

        }
        else if(view.getId()==R.id.save_btn){
            soundPoolPlayer.playShortResource(R.raw.click);
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
            soundPoolPlayer.playShortResource(R.raw.click);
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
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            for(int i = 0; i < imageIds.length; i++){
                if(imageId == imageIds[i] && i < imageIds.length-1){
                    imageId = imageIds[i+1];
            viewFlipper.showNext();
            currentDrawView = (MayekDrawView) viewFlipper.getCurrentView();
            currentDrawView.startAnimation(animSet);
            currentDrawView.setMayekName(getMayekName(imageId));
                    break;
                }
            }
        }
        else if(view.getId() == R.id.previous_btn){
            soundPoolPlayer.playShortResource(R.raw.click);
            for(int i = 0; i < imageIds.length; i++){
                if(imageId == imageIds[i] && i > 0){
                    imageId = imageIds[i-1];
            viewFlipper.showPrevious();
            currentDrawView = (MayekDrawView) viewFlipper.getCurrentView();
            currentDrawView.startAnimation(animSet);
            currentDrawView.setMayekName(getMayekName(imageId));
                    break;
                }
            }

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
