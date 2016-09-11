package com.itservz.android.mayekid.picture;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itservz.android.mayekid.BaseActivity;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.utils.BackgroundMusicFlag;
import com.itservz.android.mayekid.utils.MayekCard;
import com.itservz.android.mayekid.utils.Mayeks;
import com.itservz.android.mayekid.utils.SoundPoolPlayer;

import java.util.List;

public class PictureActivity extends BaseActivity {

    private RecyclerView recycler;
    private PictureAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<MayekCard> cards;
    private int[] imageIds = null;
    private SoundPoolPlayer soundPoolPlayer;
    private AdView mAdViewAdMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture);

        recycler = (RecyclerView) findViewById(R.id.pictureRecyclerView);
        cards = Mayeks.getInstance().getCards();
        imageIds = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            imageIds[i] = cards.get(i).getPicture();
        }

        Animation slowAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation01);
        slowAnimation.setDuration(1800);

        adapter = new PictureAdapter(this, cards, getListener(), slowAnimation);
        recycler.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int totalHeight = parent.getHeight();
                int maxCardWidth = 108;
                int sidePadding = ((totalHeight - maxCardWidth) / 4);
                sidePadding = Math.max(0, sidePadding);
                outRect.set(0, sidePadding, 0, sidePadding);
            }
        });
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7027483312186624~8107159399");
        mAdViewAdMob = (com.google.android.gms.ads.AdView) findViewById(R.id.pictureCardAdView);
        Bundle extras = new Bundle();
        extras.putBoolean("is_designed_for_families", true);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        mAdViewAdMob.loadAd(adRequest);

    }


    private PictureAdapterListener getListener() {
        return new PictureAdapterListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                wentToAnotherActivity = true;
                soundPoolPlayer.playShortResource(R.raw.whoa);
                Intent intent = new Intent(getBaseContext(), PictureDrawActivity.class);
                intent.putExtra("imageIds", imageIds);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPoolPlayer = new SoundPoolPlayer(getApplicationContext());
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            startService(backgroundMusicService);
        }
        wentToAnotherActivity = false;
        if (mAdViewAdMob != null) {
            mAdViewAdMob.resume();
        }
    }

    @Override
    public void onPause() {
        if (mAdViewAdMob != null) {
            mAdViewAdMob.pause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        soundPoolPlayer.release();
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            stopService(backgroundMusicService);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mAdViewAdMob != null) {
            mAdViewAdMob.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        wentToAnotherActivity = true;
    }


}
