package com.itservz.android.mayekid.mayek;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MayekCard {
    private String title;
    private int res;


    public MayekCard(String title, int res) {
        this.title = title;
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public String getTitle() {
        return title;
    }


}
