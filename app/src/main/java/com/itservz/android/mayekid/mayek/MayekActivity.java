package com.itservz.android.mayekid.mayek;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.itservz.android.mayekid.R;

import java.util.ArrayList;
import java.util.List;

public class MayekActivity extends Activity {

    private List<MayekCard> mayeks;
    private int[] imageIds = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mayek);

        mayeks = getCards();
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
        //recycler.smoothScrollBy(); use http://stackoverflow.com/questions/25684167/recyclerview-does-not-scroll-as-expected

//        RecyclerView recycler1 = (RecyclerView)findViewById(R.id.recyclerview1);
//        //recycler.setHasFixedSize(true);
//        MayekCardAdapter mayekCardAdapter1 = new MayekCardAdapter(this, mayeks, getListener());
//        recycler1.setAdapter(mayekCardAdapter);
//        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
//        recycler1.setLayoutManager(layoutManager);
    }

    private List<MayekCard> getCards(){
        List<MayekCard> cards = new ArrayList<>();
        cards.add(new MayekCard("KOK", R.drawable.a, R.drawable.kok));
        cards.add(new MayekCard("SAM", R.drawable.b, R.drawable.kok));
        cards.add(new MayekCard("LAI", R.drawable.c, R.drawable.kok));
        cards.add(new MayekCard("MIT", R.drawable.d, R.drawable.kok));
        cards.add(new MayekCard("PA", R.drawable.e, R.drawable.kok));
        cards.add(new MayekCard("NA", R.drawable.f, R.drawable.kok));
        cards.add(new MayekCard("CHIL", R.drawable.g, R.drawable.kok));
        cards.add(new MayekCard("TIL", R.drawable.h, R.drawable.kok));
        cards.add(new MayekCard("KHOU", R.drawable.i, R.drawable.kok));
        cards.add(new MayekCard("NGOU", R.drawable.j, R.drawable.kok));
        cards.add(new MayekCard("THOU", R.drawable.k, R.drawable.kok));
        cards.add(new MayekCard("WAI", R.drawable.l, R.drawable.kok));
        cards.add(new MayekCard("YANG", R.drawable.m, R.drawable.kok));
        return cards;
    }

    private MayekCardAdapterClickListener getListener(){
        return new MayekCardAdapterClickListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                System.out.println("MayekActivity onclick " + imageId);
                Intent intent =  new Intent(getBaseContext(), MayekDrawActivity.class);
                intent.putExtra("imageIds", imageIds);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }
        };
    }
}
