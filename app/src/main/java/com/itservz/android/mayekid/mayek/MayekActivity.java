package com.itservz.android.mayekid.mayek;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.itservz.android.mayekid.MayekCard;
import com.itservz.android.mayekid.MayekSoundPoolPlayer;
import com.itservz.android.mayekid.Mayeks;
import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.SoundPoolPlayer;

import java.util.List;

public class MayekActivity extends Activity {

    private List<MayekCard> mayeks;
    private int[] imageIds = null;
    private MayekSoundPoolPlayer mayekSoundPoolPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mayek);

        mayeks = Mayeks.getInstance().getCards();
        imageIds = new int[mayeks.size()];
        for(int i = 0; i < mayeks.size(); i++){
            imageIds[i] = mayeks.get(i).getRes();
        }
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recyclerview);
        //recycler.setHasFixedSize(true);
        MayekCardAdapter mayekCardAdapter = new MayekCardAdapter(this, mayeks, getListener());
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
    }



    private MayekListener getListener(){
        return new MayekListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                //int soundId = Mayeks.getInstance().getSoundIdFromImageId(imageId);
                mayekSoundPoolPlayer.playShortResource(imageId);
                Intent intent =  new Intent(getBaseContext(), MayekDrawActivity.class);
                intent.putExtra("imageIds", imageIds);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mayekSoundPoolPlayer = new MayekSoundPoolPlayer(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mayekSoundPoolPlayer.release();
    }
}

