package com.itservz.android.mayekid.picture;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.facebook.FacebookSdk;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.appevents.AppEventsLogger;

import com.itservz.android.mayekid.BaseActivity;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.utils.BackgroundMusicFlag;
import com.itservz.android.mayekid.utils.BitmapHelper;
import com.itservz.android.mayekid.utils.SoundPoolPlayer;

public class PictureDrawActivity extends BaseActivity implements View.OnClickListener {

    private PictureDrawView currentDrawView;
    private ImageView currPaint, drawBtn, eraseBtn, newBtn, opacityBtn, nextBtn, previousBtn;
    private Animation animation;
    private View animatedView;
    private ViewFlipper viewFlipper;
    private SoundPoolPlayer soundPoolPlayer;
    private Animation slowAnimation;
    private AdView adViewFacebook;
    private float dpWidth, dpHeight;
    private float smallBrush, mediumBrush, largeBrush;
    private int[] imageIds;
    private int imageId;

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
        //ads start  - facebook - Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //AdSettings.addTestDevice("4c05d1a9f3c86e17fd806e48202e6a94");
        adViewFacebook = new AdView(this, "1782121292033969_1785127565066675", AdSize.BANNER_HEIGHT_50);
        LinearLayout layout = (LinearLayout)findViewById(R.id.pictureAdView);
        layout.addView(adViewFacebook);
        adViewFacebook.loadAd();
        //ads end

        Intent intent = getIntent();
        imageId = intent.getIntExtra("imageId", 0);
        imageIds = intent.getIntArrayExtra("imageIds");

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth  = outMetrics.widthPixels / density;

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
        if (!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()) {
            startService(backgroundMusicService);
        }
        wentToAnotherActivity = false;
    }

    @Override
    protected void onStop() {
        soundPoolPlayer.release();
        if (!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()) {
            stopService(backgroundMusicService);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(adViewFacebook != null){
            adViewFacebook.destroy();
        }
        super.onDestroy();
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
        slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

    }

    private void initCurrentView() {
        currentDrawView.startAnimation(slowAnimation);
        currentDrawView.setPicture(imageId);

        Bitmap immutableBmp = BitmapHelper.decodeSampledBitmapFromResource(getResources(), imageId, ((int)dpWidth)*4/6, ((int)dpHeight)- 32);
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
        int x = (originalPos[0] == 0) ? dm.widthPixels / 6 : originalPos[0];
        currentDrawView.x = (dm.widthPixels - immutableBmp.getWidth() - x * 2) / 2;
        currentDrawView.y = (dm.heightPixels - immutableBmp.getHeight() - originalPos[1] * 2) / 2;
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
            currentDrawView.brushSizeAction(this);
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
            currentDrawView.changeOpacity(this);

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