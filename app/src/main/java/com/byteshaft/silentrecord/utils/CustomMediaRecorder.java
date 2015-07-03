package com.byteshaft.silentrecord.utils;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import com.byteshaft.silentrecord.notification.RecordingNotification;

import java.io.IOException;

public class CustomMediaRecorder extends MediaRecorder {

    private static CustomMediaRecorder sInstance;
    private Context mContext;
    private SurfaceHolder mHolder;

    private CustomMediaRecorder(Context context) {
        super();
        mContext = context;
    }

    public static CustomMediaRecorder getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CustomMediaRecorder(context);
        }
        return sInstance;
    }

    public static CustomMediaRecorder getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("Wrong getInstance() called, " +
                    "class needs to be initialized first by calling " +
                    "getInstance(Camera, SurfaceHolder");
        } else {
            return sInstance;
        }
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        RecordingNotification.show();

    }

    public void start(Camera camera, SurfaceHolder holder) {
        setCamera(camera);
        setAudioSource(MediaRecorder.AudioSource.MIC);
        setVideoSource(MediaRecorder.VideoSource.CAMERA);
        setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        setOrientationHint(90);
        setVideoSize(
                Values.getVideoDimensions()[0], Values.getVideoDimensions()[1]);
        setPreviewDisplay(holder.getSurface());
        try {
            prepare();
            Silencer.silentSystemStream(2000);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        RecordingNotification.hide();
        reset();
    }
}
