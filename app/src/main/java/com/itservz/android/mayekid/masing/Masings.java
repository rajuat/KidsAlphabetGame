package com.itservz.android.mayekid.masing;

import com.itservz.android.mayekid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju.athokpam on 08-09-2016.
 */
public class Masings {

    private List<MasingCard> cards = null;
    private static Masings instance = null;

    private Masings() {
        cards = new ArrayList<>();
        cards.add(new MasingCard(R.drawable.large, R.drawable.a0, R.raw.akok, "PHUN"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.b1, R.raw.akok, "AMA"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.c2, R.raw.akok, "ANI"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.d3, R.raw.akok, "AHUM"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.e4, R.raw.akok, "MARI"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.f5, R.raw.akok, "MANGA"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.g6, R.raw.akok, "TARUK"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.h7, R.raw.akok, "TARET"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.i8, R.raw.akok, "NIPAN"));
        cards.add(new MasingCard(R.drawable.paint, R.drawable.j9, R.raw.akok, "MAPAN"));
    }

    public static Masings getInstance() {
        if (instance == null) {
            instance = new Masings();
        }
        return instance;
    }

    public List<MasingCard> getCards() {
        return cards;
    }
}
