package com.itservz.android.mayekid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju on 8/25/2016.
 */
public class Mayeks {
    private List<MayekCard> cards = null;
    private static Mayeks instance = null;
    private Mayeks(){
        cards = new ArrayList<>();
        cards.add(new MayekCard("KOK", R.drawable.a, R.drawable.akok, R.raw.akok));
        cards.add(new MayekCard("SAM", R.drawable.b, R.drawable.bsam, R.raw.bsam));
        cards.add(new MayekCard("LAI", R.drawable.c, R.drawable.clai, R.raw.clai));
        cards.add(new MayekCard("MIT", R.drawable.d, R.drawable.dmit, R.raw.dmit));
        cards.add(new MayekCard("PA", R.drawable.e, R.drawable.epa, R.raw.epa));
        cards.add(new MayekCard("NA", R.drawable.f, R.drawable.fna, R.raw.fna));
        cards.add(new MayekCard("CHIL", R.drawable.g, R.drawable.gchil, R.raw.gchil));
        cards.add(new MayekCard("TIL", R.drawable.h, R.drawable.htil, R.raw.htil));
        cards.add(new MayekCard("KHOU", R.drawable.i, R.drawable.ikhou, R.raw.iknou));
        cards.add(new MayekCard("NGOU", R.drawable.j, R.drawable.jngou, R.raw.jngou));
        cards.add(new MayekCard("THOU", R.drawable.k, R.drawable.kthou, R.raw.kthou));
        cards.add(new MayekCard("WAI", R.drawable.l, R.drawable.lwai, R.raw.lwai));
        cards.add(new MayekCard("YANG", R.drawable.m, R.drawable.myang, R.raw.myang));
        cards.add(new MayekCard("HUK", R.drawable.n, R.drawable.nhuk, R.raw.nhuk));
        cards.add(new MayekCard("UOON", R.drawable.o, R.drawable.ouoon, R.raw.ouoon));
        cards.add(new MayekCard("EE", R.drawable.p, R.drawable.pee, R.raw.pee));
        cards.add(new MayekCard("PHAM", R.drawable.q, R.drawable.qpham, R.raw.qpham));
        cards.add(new MayekCard("ATIYA", R.drawable.r, R.drawable.ratiya, R.raw.ratiya));
    }

    public static Mayeks getInstance(){
        if(instance == null ){
            instance = new Mayeks();
        }
        return instance;
    }

    public List<MayekCard> getCards(){
        return cards;
    }


}
