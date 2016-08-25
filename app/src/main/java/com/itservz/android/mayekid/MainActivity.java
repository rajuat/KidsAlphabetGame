package com.itservz.android.mayekid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.media.MediaPlayer;

import com.itservz.android.mayekid.mayek.MayekActivity;
import com.itservz.android.mayekid.picture.PictureActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private MediaPlayer bgMusic;
    private SoundPoolPlayer soundPoolPlayer;
    private Intent backgroundMusicService;
    private boolean soundOnOff = false;
    private ImageView noticeBoard;
    private ImageView mayekBoard;
    private ImageView cardBoard;
    private ImageView goToDraw;
    private ImageView goToPaint;
    private ImageView soundView;
    private ImageView noticeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        backgroundMusicService = new Intent(this, BackgroundMusicService.class);
        startService(backgroundMusicService);
        soundOnOff = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        animate();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPoolPlayer.release();
    }

    public void click(View view){
        if(view.getId() == R.id.mayekBoardButton || view.getId() == R.id.gotodraw){
            soundPoolPlayer.playShortResource(R.raw.whoa);
            Intent intent = new Intent(this, MayekActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.cartoonBoardButton|| view.getId() == R.id.gotopaint){
            soundPoolPlayer.playShortResource(R.raw.whoa);
            Intent intent = new Intent(this, PictureActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.noticeboard){
            soundPoolPlayer.playShortResource(R.raw.whoa);
            animateNoticeBoard();
        } else if(view.getId() == R.id.soundOnOff){
            if(soundOnOff){
                stopService(backgroundMusicService);
                soundOnOff = false;
            } else {
                startService(backgroundMusicService);
                soundOnOff = true;
            }

        }
    }

    private void clearAnimations() {
        noticeBoard.clearAnimation();
        mayekBoard.clearAnimation();
        cardBoard.clearAnimation();
        goToDraw.clearAnimation();
        goToPaint.clearAnimation();
        soundView.clearAnimation();
        noticeView.clearAnimation();
    }

    @Override
    public void onClick(View view) {
    }

    private void animate() {
        mayekBoard = (ImageView) findViewById(R.id.mayekBoardButton);
        cardBoard = (ImageView) findViewById(R.id.cartoonBoardButton);
        goToDraw = (ImageView) findViewById(R.id.gotodraw);
        goToPaint = (ImageView) findViewById(R.id.gotopaint);
        soundView = (ImageView) findViewById(R.id.soundOnOff);
        noticeView = (ImageView) findViewById(R.id.noticeboard);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        Animation slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        slowAnimation.setDuration(1800);
        goToDraw.startAnimation(animation);
        goToPaint.startAnimation(animation);

        mayekBoard.startAnimation(slowAnimation);
        cardBoard.startAnimation(slowAnimation);
        soundView.startAnimation(slowAnimation);
        noticeView.startAnimation(slowAnimation);
    }

    private void animateNoticeBoard() {
        noticeBoard = (ImageView) findViewById(R.id.noticeboard);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int originalPos[] = new int[2];
        noticeBoard.getLocationOnScreen(originalPos);

        int xDelta = (dm.widthPixels - noticeBoard.getMeasuredWidth() - originalPos[0] * 2) / 2;
        //int yDelta = (dm.heightPixels - noticeBoard.getMeasuredHeight() - originalPos[1]) / 2;

        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(1000);
        animSet.setInterpolator(new BounceInterpolator());

        TranslateAnimation translate = new TranslateAnimation(xDelta, xDelta, -noticeBoard.getMeasuredHeight(), 0);
        animSet.addAnimation(translate);

        noticeBoard.startAnimation(animSet);
    }
}
