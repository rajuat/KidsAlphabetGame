package com.itservz.android.mayekid.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.itservz.android.mayekid.R;

/**
 * Created by Raju on 8/25/2016.
 */
public class BackgroundMusicService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.bgmusic);
        player.setLooping(true);
        player.setVolume(0.25f, 0.25f);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (player != null) {
                            pauseMedia();
                            isPausedInCall = true;
                        }

                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (player != null) {
                            if (isPausedInCall) {
                                isPausedInCall = false;
                                playMedia();
                            }

                        }
                        break;
                }

            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {
    }

    public void onPause() {
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }

        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener,  PhoneStateListener.LISTEN_NONE);
        }

    }

    @Override
    public void onLowMemory() {
    }

    public void playMedia() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public void pauseMedia() {
        if (player.isPlaying()) {
            player.pause();
        }

    }

    public void stopMedia() {
        if (player.isPlaying()) {
            player.stop();
        }
    }
}