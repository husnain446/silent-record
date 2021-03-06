package com.byteshaft.silentrecord.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.byteshaft.ezflashlight.FlashlightGlobals;
import com.byteshaft.silentrecord.AppGlobals;
import com.byteshaft.silentrecord.services.PictureService;
import com.byteshaft.silentrecord.services.RecordService;
import com.byteshaft.silentrecord.utils.Helpers;

public class WidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Helpers.isAppRunningForTheFirstTime()) {
            Toast.makeText(
                    context,
                    "The App needs to be run at least once before using the widget",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent pictureService = AppGlobals.getPictureServiceIntent();
        Intent serviceIntent = AppGlobals.getRecordServiceIntent();
        String widget = intent.getStringExtra("key");

        if (widget.equals("1") && !FlashlightGlobals.isResourceOccupied()) {
            AppGlobals.getContext().startService(pictureService);
        } else if (widget.equals("2")) {
            if (RecordService.isRecording()) {
                AppGlobals.getContext().stopService(serviceIntent);
            } else {
                if (!PictureService.isTakingPicture() && !FlashlightGlobals.isResourceOccupied()) {
                    AppGlobals.getContext().startService(serviceIntent);
                }
            }
        }
        updateWidget(context);
    }

    public static void updateWidget(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, WidgetProvider.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        Intent intent = new Intent(context.getApplicationContext(), WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        context.sendBroadcast(intent);
    }
}
