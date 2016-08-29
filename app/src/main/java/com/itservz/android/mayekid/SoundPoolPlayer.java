package com.itservz.android.mayekid;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by raju.athokpam on 24-08-2016.
 */
public class SoundPoolPlayer {
    private SoundPool mShortPlayer = null;
    private HashMap mSounds = new HashMap();

    public SoundPoolPlayer(Context pContext) {
        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSounds.put(R.raw.whoa, this.mShortPlayer.load(pContext, R.raw.whoa, 1));
        mSounds.put(R.raw.click, this.mShortPlayer.load(pContext, R.raw.click, 1));
    }

    public void playShortResource(int piResource) {
        int iSoundId = (Integer) mSounds.get(piResource);
        this.mShortPlayer.play(iSoundId, 0.49f, 0.49f, 0, 0, 1);
    }

    // Cleanup
    public void release() {
        // Cleanup
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }
}
