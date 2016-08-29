package com.itservz.android.mayekid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

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
        System.out.println("BackgroundMusicService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("BackgroundMusicService OnCreate");
        player = MediaPlayer.create(this, R.raw.bgmusic);
        player.setLooping(true); // Set looping
        player.setVolume(0.25f, 0.25f);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("BackgroundMusicService onStartCommand");
        // Manage incoming phone calls during playback. Pause mp on incoming,
        // resume on hangup.
        // -----------------------------------------------------------------------------------
        // Get the telephony manager
        Log.v(TAG, "Starting telephony");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.v(TAG, "Starting listener");
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // String stateString = "N/A";
                Log.v(TAG, "Starting CallStateChange");
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (player != null) {
                            pauseMedia();
                            isPausedInCall = true;
                        }

                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
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

        // Register the listener with the telephony manager
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        System.out.println("BackgroundMusicService onStart");
    }

    public IBinder onUnBind(Intent arg0) {
        System.out.println("BackgroundMusicService onUnBind");
        return null;
    }

    public void onStop() {
        System.out.println("BackgroundMusicService onStop");
    }

    public void onPause() {
        System.out.println("BackgroundMusicService onPause");
    }

    @Override
    public void onDestroy() {
        System.out.println("BackgroundMusicService onDestroy");
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
        System.out.println("BackgroundMusicService onLowMemory");
    }

    public void playMedia() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    // Add for Telephony Manager
    public void pauseMedia() {
        // Log.v(TAG, "Pause Media");
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