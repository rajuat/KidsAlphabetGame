package com.itservz.android.mayekid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.itservz.android.mayekid.utils.BackgroundMusicService;

/**
 * Created by raju.athokpam on 31-08-2016.
 */
public class BaseActivity extends Activity {
    public Intent backgroundMusicService;
    public boolean wentToAnotherActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(backgroundMusicService == null) {
            backgroundMusicService = new Intent(getApplicationContext(), BackgroundMusicService.class);
        }
    }
}
