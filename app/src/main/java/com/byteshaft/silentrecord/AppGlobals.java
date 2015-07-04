package com.byteshaft.silentrecord;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

public class AppGlobals extends Application {

    private static SharedPreferences sPreferences;
    private static Context sContext;

    public static class DIRECTORY {
        public static final String PICTURES = "SpyPics";
        public static final String VIDEOS = "SpyVideos";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static File getVideosDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, DIRECTORY.VIDEOS);
    }

    public static File getPicturesDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, DIRECTORY.PICTURES);
    }
}