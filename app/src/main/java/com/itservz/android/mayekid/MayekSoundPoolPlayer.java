package com.itservz.android.mayekid;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import com.itservz.android.mayekid.R;

/**
 * Created by raju.athokpam on 24-08-2016.
 */
public class MayekSoundPoolPlayer {
    private SoundPool mShortPlayer = null;
    private HashMap mSounds = new HashMap();

    public MayekSoundPoolPlayer(Context pContext) {
        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSounds.put(R.drawable.a, this.mShortPlayer.load(pContext, R.raw.akok, 1));
        mSounds.put(R.drawable.b, this.mShortPlayer.load(pContext, R.raw.bsam, 1));
        mSounds.put(R.drawable.c, this.mShortPlayer.load(pContext, R.raw.clai, 1));
        mSounds.put(R.drawable.d, this.mShortPlayer.load(pContext, R.raw.dmit, 1));
        mSounds.put(R.drawable.e, this.mShortPlayer.load(pContext, R.raw.epa, 1));
        mSounds.put(R.drawable.f, this.mShortPlayer.load(pContext, R.raw.fna, 1));
        mSounds.put(R.drawable.g, this.mShortPlayer.load(pContext, R.raw.gchil, 1));
        mSounds.put(R.drawable.h, this.mShortPlayer.load(pContext, R.raw.htil, 1));
        mSounds.put(R.drawable.i, this.mShortPlayer.load(pContext, R.raw.iknou, 1));
        mSounds.put(R.drawable.j, this.mShortPlayer.load(pContext, R.raw.jngou, 1));
        mSounds.put(R.drawable.k, this.mShortPlayer.load(pContext, R.raw.kthou, 1));
        mSounds.put(R.drawable.l, this.mShortPlayer.load(pContext, R.raw.lwai, 1));
        mSounds.put(R.drawable.m, this.mShortPlayer.load(pContext, R.raw.myang, 1));
        mSounds.put(R.drawable.n, this.mShortPlayer.load(pContext, R.raw.nhuk, 1));
        mSounds.put(R.drawable.o, this.mShortPlayer.load(pContext, R.raw.ouoon, 1));
        mSounds.put(R.drawable.p, this.mShortPlayer.load(pContext, R.raw.pee, 1));
        mSounds.put(R.drawable.q, this.mShortPlayer.load(pContext, R.raw.qpham, 1));
        mSounds.put(R.drawable.r, this.mShortPlayer.load(pContext, R.raw.ratiya, 1));
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
