package com.itservz.android.mayekid.picture;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import com.itservz.android.mayekid.BackgroundMusicFlag;
import com.itservz.android.mayekid.BaseActivity;
import com.itservz.android.mayekid.BitmapHelper;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.SoundPoolPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PictureDrawActivity extends BaseActivity implements View.OnClickListener {

    //custom drawing view
    private PictureDrawView currentDrawView;
    //buttons
    private ImageView currPaint, drawBtn, eraseBtn, newBtn, opacityBtn, nextBtn, previousBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;
    private int[] imageIds;
    private int imageId;
    private Animation animation;
    private View animatedView;
    private ViewFlipper viewFlipper;
    private AnimationSet animSet;
    private SoundPoolPlayer soundPoolPlayer;

    private void setFlipperImage(int res) {
        PictureDrawView image = new PictureDrawView(getApplicationContext(), res);
        image.setTag(res);
        viewFlipper.addView(image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture_draw);
        //ads start
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id_picture));
        AdView mAdView = (AdView) findViewById(R.id.pictureAdView);
        //AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        //ads end
        Intent intent = getIntent();
        imageId = intent.getIntExtra("imageId", 0);
        imageIds = intent.getIntArrayExtra("imageIds");

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewFlipper = (ViewFlipper) findViewById(R.id.pictureFlipper);
        for (int i = 0; i < imageIds.length; i++) {
            setFlipperImage(imageIds[i]);
        }
        currentDrawView = (PictureDrawView) viewFlipper.findViewWithTag(imageId);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(currentDrawView));
        initCurrentView();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            startService(backgroundMusicService);
        }
        wentToAnotherActivity = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPoolPlayer.release();
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            stopService(backgroundMusicService);
        }
    }

    private void init() {
        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageView) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageView) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageView) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        opacityBtn = (ImageView) findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        previousBtn = (ImageView) findViewById(R.id.previous_btn);
        previousBtn.setOnClickListener(this);

        nextBtn = (ImageView) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.paint_animation);
        animSet = new AnimationSet(false);
        animSet.setFillAfter(true);
        animSet.setDuration(1500);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        Animation slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        animSet.addAnimation(slowAnimation);

    }

    private void initCurrentView() {
        currentDrawView.startAnimation(animSet);
        currentDrawView.setPicture(imageId);
        Bitmap immutableBmp = BitmapHelper.decodeSampledBitmapFromResource(getResources(), imageId, 224, 224);
        currentDrawView.pictureBitMap = immutableBmp.copy(Bitmap.Config.ARGB_8888, true);

        float alpha = 1.0f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentDrawView.setAlpha(alpha);
        }
        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currentDrawView.setColor(currPaint.getTag().toString());
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        currentDrawView.setBrushSize(mediumBrush);
        setCordinates(immutableBmp);
    }

    private void setCordinates(Bitmap immutableBmp) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int originalPos[] = new int[2];
        currentDrawView.getLocationOnScreen(originalPos);
        int x = (originalPos[0] == 0) ? dm.widthPixels/6 : originalPos[0];
        currentDrawView.x = (dm.widthPixels - immutableBmp.getWidth()- x * 2 ) / 2;
        currentDrawView.y = (dm.heightPixels - immutableBmp.getHeight()- originalPos[1] * 2) / 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void paintClicked(View view) {
        currentDrawView.setErase(false);
        currentDrawView.setPaintAlpha(50);
        currentDrawView.setBrushSize(currentDrawView.getLastBrushSize());

        if (view != currPaint) {
            ImageButton imageButton = (ImageButton) view;
            String color = view.getTag().toString();
            currentDrawView.setColor(color);
            //update ui
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {
        currentDrawView.clearAnimation();
        if (animatedView != null) {
            animatedView.clearAnimation();
        }
        if (view.getId() == R.id.draw_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(smallBrush);
                    currentDrawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(mediumBrush);
                    currentDrawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(largeBrush);
                    currentDrawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            currentDrawView.startNew();

        } else if (view.getId() == R.id.opacity_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            final Dialog seekDialog = new Dialog(this);
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
            int currLevel = currentDrawView.getPaintAlpha();
            seekTxt.setText(currLevel + "%");
            seekOpq.setProgress(currLevel);

            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                    currentDrawView.setPaintAlpha(seekOpq.getProgress());
                    //
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

            });
            seekDialog.show();
        } else if (view.getId() == R.id.next_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            animatedView = animate(view);
            for (int i = 0; i < imageIds.length; i++) {
                if (imageId == imageIds[i] && i < imageIds.length - 1) {
                    imageId = imageIds[i + 1];
                    viewFlipper.showNext();
                    currentDrawView = (PictureDrawView) viewFlipper.getCurrentView();
                    initCurrentView();
                    break;
                }
            }
        } else if (view.getId() == R.id.previous_btn) {
            soundPoolPlayer.playShortResource(R.raw.click);
            for (int i = 0; i < imageIds.length; i++) {
                if (imageId == imageIds[i] && i > 0) {
                    imageId = imageIds[i - 1];
                    viewFlipper.showPrevious();
                    currentDrawView = (PictureDrawView) viewFlipper.getCurrentView();
                    initCurrentView();
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        wentToAnotherActivity = true;
    }

    private View animate(View imageView) {
        imageView.startAnimation(animation);
        return imageView;
    }


}
