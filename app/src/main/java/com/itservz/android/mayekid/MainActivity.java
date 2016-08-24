package com.itservz.android.mayekid;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.SoundPool;
import com.itservz.android.mayekid.mayek.MayekActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private MediaPlayer bgMusic;
    private SoundPoolPlayer soundPoolPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        animate();
        bgMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgmusic);
        bgMusic.setLooping(true);
        bgMusic.start();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
    }

    private void animate(){
        ImageView mayekBoard = (ImageView)findViewById(R.id.mayekBoardButton);
        ImageView cardBoard = (ImageView)findViewById(R.id.cartoonBoardButton);
        ImageView goToDraw = (ImageView)findViewById(R.id.gotodraw);
        ImageView goToPaint = (ImageView)findViewById(R.id.gotopaint);
        Animation animation  = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        animation.setRepeatCount(Animation.INFINITE);
        /*mayekBoard.startAnimation(animation);
        cardBoard.startAnimation(animation);*/
        goToDraw.startAnimation(animation);
        goToPaint.startAnimation(animation);
    }

    private void animateNoticeBoard() {
        ImageView noticeBoard = (ImageView) findViewById(R.id.noticeboard);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int originalPos[] = new int[2];
        noticeBoard.getLocationOnScreen(originalPos);

        int xDelta = (dm.widthPixels - noticeBoard.getMeasuredWidth() - originalPos[0]*2) / 2;
        //int yDelta = (dm.heightPixels - noticeBoard.getMeasuredHeight() - originalPos[1]) / 2;

        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(1000);
        animSet.setInterpolator(new BounceInterpolator());

        TranslateAnimation translate = new TranslateAnimation(xDelta, xDelta, -noticeBoard.getMeasuredHeight(), 0);
        animSet.addAnimation(translate);
        /*RotateAnimation rotateAnimation = new RotateAnimation(0,360);
        animSet.addAnimation(rotateAnimation);*/

        noticeBoard.startAnimation(animSet);
    }

    public void goToNoticeBoard(View view){
        soundPoolPlayer.playShortResource(R.raw.whoa);
        animateNoticeBoard();
    }

    public void goToMayek(View view){
        soundPoolPlayer.playShortResource(R.raw.whoa);
        Intent intent = new Intent(this, MayekActivity.class);
        startActivity(intent);
    }

    public void goToCartoon(View view){
        soundPoolPlayer.playShortResource(R.raw.whoa);
        Intent intent = new Intent(this, MayekActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
    }
}
