package com.byteshaft.silentrecord.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.Environment;
import com.byteshaft.silentrecord.AppGlobals;
import com.byteshaft.silentrecord.R;
import java.util.Calendar;
import java.io.File;
import java.util.ArrayList;


public class Helpers extends ContextWrapper {

    private AlarmManager mAlarmManager;
    private PendingIntent mPIntent;

    public Helpers(Context base) {
        super(base);
    }

    public String readZoomSettings() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getString("camera_zoom_control", "0");
    }

    public String readMaxVideoValue() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getString("max_video", "5");
    }

    public void showCameraResourceBusyDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Camera busy");
        builder.setMessage("The App needs to read camera capabilities on first run, " +
                "please make sure camera is not in use by any other app");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                activity.finish();
            }
        });
        builder.create();
        builder.show();
    }

    public boolean isAppRunningForTheFirstTime() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getBoolean("first_run", true);
    }

    public void setIsAppRunningForTheFirstTime(boolean firstTime) {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        preferences.edit().putBoolean("first_run", firstTime).apply();
    }

    public Camera openCamera() {
        try {
            return Camera.open();
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static  void setPicAlarm(boolean picAlarm) {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        sharedPreferences.edit().putBoolean("picAlarm", picAlarm).apply();
    }

    public static boolean getPicAlarmStatus() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("picAlarm", false);
    }

    public static  void setVideoAlarm(boolean videoAlarm) {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        sharedPreferences.edit().putBoolean("videoAlarm", videoAlarm).apply();
    }

    public static  boolean getVideoAlarmStatus() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("videoAlarm", false);
    }

    public static void setTime(boolean value) {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        sharedPreferences.edit().putBoolean("time_set", value).apply();
    }

    public static boolean getTime() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("time_set", false);
    }

    public static void setDate(boolean value) {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        sharedPreferences.edit().putBoolean("date_time", value).apply();
    }

    public static boolean getDate() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("date_time", false);
    }

    private  AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    public void removePreviousAlarm(){
        if (mPIntent != null) {
            mAlarmManager.cancel(mPIntent);
        }
    }

    public void setAlarm(int date, int month,
                                   int year, int hour, int minutes, String operationType) {
        mAlarmManager = getAlarmManager();
        Intent intent = null;
        if (operationType.equals("pic")) {
           intent =new Intent("com.byteShaft.Alarm");
        } else if (operationType.equals("video")) {
            intent = new Intent("com.byteShaft.VideoAlarm");
        }
        System.out.println(operationType);
        intent.putExtra("operationType",operationType);
        mPIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, date);  //1-31
        calendar.set(Calendar.MONTH, month);  //first month is 0!!! January is zero!!!
        calendar.set(Calendar.YEAR, year);//year...

        calendar.set(Calendar.HOUR_OF_DAY, hour);  //HOUR
        calendar.set(Calendar.MINUTE, minutes);       //MIN
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPIntent);
        Log.i(AppGlobals.getLogTag(getClass()), "setting alarm of :" + calendar.getTime());
    }

    public void spyVideosDirectory() {

    }
        File recordingsDirectory = new File(Environment.getExternalStorageDirectory() + "/" + "SpyVideos");
    public static void createDirectoryIfNotExists(String directoryName) {
        File recordingsDirectory = new File(Environment.getExternalStorageDirectory(), directoryName);
        if (!recordingsDirectory.exists()) {
            recordingsDirectory.mkdir();
        }
    }

    public static ArrayList<String> getFileNamesFromDirectory(String directoryName) {
        ArrayList<String> arrayList = new ArrayList<>();
        File filePath = Environment.getExternalStorageDirectory();
        File fileDirectory = new File(filePath, directoryName);
        for (File file : fileDirectory.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                arrayList.add(name);
            }
        }
        return arrayList;
    }

    public static void hideFilesInDirectory(String directoryName) {
        File directory;
        switch (directoryName) {
            case AppGlobals.DIRECTORY.VIDEOS:
                directory = AppGlobals.getVideosDirectory();
                break;
            case AppGlobals.DIRECTORY.PICTURES:
                directory = AppGlobals.getPicturesDirectory();
                break;
            default:
                return;
        }
        for (File file : directory.listFiles()) {
            String fileName = file.getName();
            if (!fileName.startsWith(".")) {
                File hiddenFile = new File(directory, "." + fileName);
                file.renameTo(hiddenFile);
            }
        }
    }

    public static void unHideFilesInDirectory(String directoryName) {
        File directory;
        switch (directoryName) {
            case AppGlobals.DIRECTORY.VIDEOS:
                directory = AppGlobals.getVideosDirectory();
                break;
            case AppGlobals.DIRECTORY.PICTURES:
                directory = AppGlobals.getPicturesDirectory();
                break;
            default:
                return;
        }
        for (File file : directory.listFiles()) {
            String fileName = file.getName();
            if (fileName.startsWith(".")) {
                File unhidden = new File(directory, fileName.substring(1));
                file.renameTo(unhidden);
            }
        }
    }

    public static boolean isImageHiderOn() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("image_visibility", false);
    }

    public static boolean isVideoHiderOn() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("video_visibility", false);
    }

    public boolean isNotificationWidgetOn() {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getBoolean("notification_widget", true);
    }

    public String getValueFromKey(String value) {
        SharedPreferences sharedPreferences = AppGlobals.getPreferenceManager();
        return sharedPreferences.getString(value, " ");
    }
}
