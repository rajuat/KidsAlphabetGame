package com.itservz.android.mayekid;

/**
 * Created by raju.athokpam on 31-08-2016.
 */
public class BackgroundMusicFlag {
    private boolean soundOnOff;
    private static BackgroundMusicFlag bgmFlag = new BackgroundMusicFlag();

    public static BackgroundMusicFlag getInstance(){
        return bgmFlag;
    }

    public boolean isSoundOnOff() {
        return soundOnOff;
    }

    public void setSoundOnOff(boolean soundOnOff) {
        this.soundOnOff = soundOnOff;
    }
}
