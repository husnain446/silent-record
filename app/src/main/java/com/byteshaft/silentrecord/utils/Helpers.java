package com.byteshaft.silentrecord.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Environment;

import com.byteshaft.silentrecord.AppGlobals;
import com.byteshaft.silentrecord.R;

import java.io.File;
import java.util.ArrayList;


public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    public String readZoomSettings() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getString("camera_zoom_control", "0");
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
}