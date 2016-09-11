package com.itservz.android.mayekid.masing;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.itservz.android.mayekid.BaseActivity;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.mayek.MayekDrawActivity;
import com.itservz.android.mayekid.utils.BackgroundMusicFlag;
import com.itservz.android.mayekid.utils.MasingSoundPoolPlayer;

import java.util.List;

public class MasingActivity extends BaseActivity {

    private List<MasingCard> masings;
    private int[] imageIds = null;
    private AdView mAdViewAdMob;
    private MasingSoundPoolPlayer masingSoundPoolPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_masing);

        masings = Masings.getInstance().getCards();
        imageIds = new int[masings.size()];
        for(int i = 0; i < masings.size(); i++){
            imageIds[i] = masings.get(i).getPic();
        }
        RecyclerView recycler = (RecyclerView)findViewById(R.id.masing_recycler_view);
        MasingCardAdapter mayekCardAdapter = new MasingCardAdapter(this, masings, getListener());
        recycler.setAdapter(mayekCardAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
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
        mAdViewAdMob = (AdView) findViewById(R.id.masingCardAdView);
        Bundle extras = new Bundle();
        extras.putBoolean("is_designed_for_families", true);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        mAdViewAdMob.loadAd(adRequest);
    }

    private MasingListener getListener(){
        return new MasingListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                wentToAnotherActivity = true;
                masingSoundPoolPlayer.playShortResource(imageId);
                Intent intent =  new Intent(getBaseContext(), MasingDrawActivity.class);
                intent.putExtra("imageIds", imageIds);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        masingSoundPoolPlayer = new MasingSoundPoolPlayer(getApplicationContext());
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
        super.onStop();
        masingSoundPoolPlayer.release();
        if(!wentToAnotherActivity && BackgroundMusicFlag.getInstance().isSoundOnOff()){
            stopService(backgroundMusicService);
        }
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
