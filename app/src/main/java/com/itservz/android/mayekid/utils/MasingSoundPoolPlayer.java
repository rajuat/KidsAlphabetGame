package com.itservz.android.mayekid.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.itservz.android.mayekid.R;

import java.util.HashMap;

/**
 * Created by raju.athokpam on 24-08-2016.
 */
public class MasingSoundPoolPlayer {
    private SoundPool mShortPlayer = null;
    private HashMap mSounds = new HashMap();

    public MasingSoundPoolPlayer(Context pContext) {
        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSounds.put(R.drawable.a0, this.mShortPlayer.load(pContext, R.raw.a0s, 1));
        mSounds.put(R.drawable.b1, this.mShortPlayer.load(pContext, R.raw.b1s, 1));
        mSounds.put(R.drawable.c2, this.mShortPlayer.load(pContext, R.raw.c2s, 1));
        mSounds.put(R.drawable.d3, this.mShortPlayer.load(pContext, R.raw.d3s, 1));
        mSounds.put(R.drawable.e4, this.mShortPlayer.load(pContext, R.raw.e4s, 1));
        mSounds.put(R.drawable.f5, this.mShortPlayer.load(pContext, R.raw.f5s, 1));
        mSounds.put(R.drawable.g6, this.mShortPlayer.load(pContext, R.raw.g6s, 1));
        mSounds.put(R.drawable.h7, this.mShortPlayer.load(pContext, R.raw.h7s, 1));
        mSounds.put(R.drawable.i8, this.mShortPlayer.load(pContext, R.raw.i8s, 1));
        mSounds.put(R.drawable.j9, this.mShortPlayer.load(pContext, R.raw.j9s, 1));
    }

    public void playShortResource(int piResource) {
        int iSoundId = (Integer) mSounds.get(piResource);
        this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);
    }

    public void release() {
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }
}
