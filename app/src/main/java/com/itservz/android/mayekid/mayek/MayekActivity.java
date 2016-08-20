package com.itservz.android.mayekid.mayek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.itservz.android.mayekid.R;

import java.util.ArrayList;
import java.util.List;

public class MayekActivity extends AppCompatActivity {

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
        //recycler.smoothScrollBy(); use http://stackoverflow.com/questions/25684167/recyclerview-does-not-scroll-as-expected

    }

    private List<MayekCard> getCards(){
        List<MayekCard> cards = new ArrayList<>();
        cards.add(new MayekCard("A", R.drawable.a));
        cards.add(new MayekCard("B", R.drawable.b));
        cards.add(new MayekCard("C", R.drawable.c));
        cards.add(new MayekCard("D", R.drawable.d));
        cards.add(new MayekCard("E", R.drawable.e));
        cards.add(new MayekCard("F", R.drawable.f));
        cards.add(new MayekCard("G", R.drawable.g));
        cards.add(new MayekCard("H", R.drawable.h));
        cards.add(new MayekCard("I", R.drawable.i));
        cards.add(new MayekCard("J", R.drawable.j));
        cards.add(new MayekCard("K", R.drawable.k));
        cards.add(new MayekCard("L", R.drawable.l));
        cards.add(new MayekCard("M", R.drawable.m));
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
