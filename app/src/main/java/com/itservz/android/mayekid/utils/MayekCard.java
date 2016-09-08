package com.itservz.android.mayekid.utils;

import java.io.Serializable;

/**
 * Created by raju.athokpam on 19-08-2016.
 */
public class MayekCard implements Serializable{
    private String title;
    private int res;
    private int picture;
    private int sound;


    public MayekCard(String title, int res, int picture, int sound) {
        this.title = title;
        this.res = res;
        this.picture = picture;
        this.sound = sound;
    }

    public int getSound(){ return sound; }

    public int getRes() {
        return res;
    }

    public String getTitle() {
        return title;
    }

    public int getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "MayekCard{" +
                "title='" + title + '\'' +
                ", res=" + res +
                ", picture=" + picture +
                ", sound=" + sound +
                '}';
    }
}
