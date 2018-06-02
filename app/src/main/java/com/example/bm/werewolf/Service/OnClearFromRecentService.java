package com.example.bm.werewolf.Service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.bm.werewolf.Activity.LoginActivity;
import com.example.bm.werewolf.Activity.MainActivity;

/**
 * Created by bùm on 6/2/2018.
 */

public class OnClearFromRecentService extends Service {
    public static Activity activity;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("ClearFromRecentService", "END");
        activity.finishAffinity();
        stopSelf();
    }
}