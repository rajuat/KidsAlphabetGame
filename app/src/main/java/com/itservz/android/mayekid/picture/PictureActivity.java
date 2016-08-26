package com.itservz.android.mayekid.picture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.itservz.android.mayekid.Mayeks;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.MayekCard;
import com.itservz.android.mayekid.SoundPoolPlayer;

import java.util.List;

public class PictureActivity extends Activity {

    private RecyclerView recycler;
    private PictureAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<MayekCard> cards;
    private int[] imageIds = null;
    private SoundPoolPlayer soundPoolPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture);

        recycler = (RecyclerView) findViewById(R.id.pictureRecyclerView);
        cards = Mayeks.getInstance().getCards();
        imageIds = new int[cards.size()];
        for(int i = 0; i < cards.size(); i++){
            imageIds[i] = cards.get(i).getPicture();
        }
        adapter = new PictureAdapter(this, cards , getListener());
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

    }


    private PictureAdapterListener getListener() {
        return new PictureAdapterListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                soundPoolPlayer.playShortResource(R.raw.whoa);
                Intent intent =  new Intent(getBaseContext(), PictureDrawActivity.class);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPoolPlayer.release();
    }


}
