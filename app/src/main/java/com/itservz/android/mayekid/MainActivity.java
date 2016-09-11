package com.itservz.android.mayekid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.itservz.android.mayekid.masing.MasingActivity;
import com.itservz.android.mayekid.mayek.MayekActivity;
import com.itservz.android.mayekid.picture.PictureActivity;
import com.itservz.android.mayekid.utils.BackgroundMusicFlag;
import com.itservz.android.mayekid.utils.SoundPoolPlayer;

public class MainActivity extends BaseActivity  {

    private SoundPoolPlayer soundPoolPlayer;
    private ImageView noticeBoard;
    private ImageView mayekBoard;
    private ImageView cardBoard;
    private ImageView soundView;
    private ImageView noticeView;
    private ImageView masingView;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        startService(backgroundMusicService);
        BackgroundMusicFlag.getInstance().setSoundOnOff(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animate();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            startService(backgroundMusicService);
        }
        wentToAnotherActivity = false;
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        soundPoolPlayer.release();
        if(!wentToAnotherActivity){
            stopService(backgroundMusicService);
        }
    }

    @Override
    protected void onDestroy(){
        stopService(backgroundMusicService);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void click(View view) {
        if (view.getId() == R.id.mayekBoardButton ) {
            soundPoolPlayer.playShortResource(R.raw.whoa);
            wentToAnotherActivity = true;
            Intent intent = new Intent(this, MayekActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.masing_board ) {
            soundPoolPlayer.playShortResource(R.raw.whoa);
            wentToAnotherActivity = true;
            Intent intent = new Intent(this, MasingActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.cartoonBoardButton) {
            soundPoolPlayer.playShortResource(R.raw.whoa);
            wentToAnotherActivity = true;
            Intent intent = new Intent(this, PictureActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.noticeboard) {
            soundPoolPlayer.playShortResource(R.raw.whoa);
            animateNoticeBoard();
        } else if (view.getId() == R.id.soundOnOff) {
            if (BackgroundMusicFlag.getInstance().isSoundOnOff()) {
                stopService(backgroundMusicService);
                BackgroundMusicFlag.getInstance().setSoundOnOff(false);
            } else {
                startService(backgroundMusicService);
                BackgroundMusicFlag.getInstance().setSoundOnOff(true);
            }

        }
    }

    private void clearAnimations() {
        noticeBoard.clearAnimation();
        mayekBoard.clearAnimation();
        cardBoard.clearAnimation();
        soundView.clearAnimation();
        noticeView.clearAnimation();
    }

    private void animate() {
        mayekBoard = (ImageView) findViewById(R.id.mayekBoardButton);
        cardBoard = (ImageView) findViewById(R.id.cartoonBoardButton);
        soundView = (ImageView) findViewById(R.id.soundOnOff);
        noticeView = (ImageView) findViewById(R.id.noticeboard);
        masingView = (ImageView) findViewById(R.id.masing_board);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        Animation slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        slowAnimation.setDuration(1800);

        mayekBoard.startAnimation(animation);
        masingView.startAnimation(animation);

        cardBoard.startAnimation(slowAnimation);
        soundView.startAnimation(slowAnimation);
        noticeView.startAnimation(slowAnimation);
    }

    private void animateNoticeBoard() {
        noticeBoard = (ImageView) findViewById(R.id.noticeboard);

        int originalPos[] = new int[2];
        noticeBoard.getLocationOnScreen(originalPos);

        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(1000);
        animSet.setInterpolator(new BounceInterpolator());

        TranslateAnimation translate = new TranslateAnimation(originalPos[0], originalPos[0], -noticeBoard.getMeasuredHeight(), 0);
        animSet.addAnimation(translate);

        noticeBoard.startAnimation(animSet);
    }
}
