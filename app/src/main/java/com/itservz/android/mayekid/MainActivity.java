package com.itservz.android.mayekid;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itservz.android.mayekid.mayek.MayekActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        animate();
    }

    private void animate(){
        ImageView mayekBoard = (ImageView)findViewById(R.id.mayekBoardButton);
        ImageView cardBoard = (ImageView)findViewById(R.id.cartoonBoardButton);
        Animation animation  = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        animation.setRepeatCount(Animation.INFINITE);
        mayekBoard.startAnimation(animation);
        cardBoard.startAnimation(animation);
    }


    public void goToMayek(View view){
        Intent intent = new Intent(this, MayekActivity.class);
        startActivity(intent);
    }

    public void goToCartoon(View view){
        Intent intent = new Intent(this, MayekActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
    }
}
