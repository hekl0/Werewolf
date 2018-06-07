package com.example.bm.werewolf.Service;

import android.util.Log;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by bùm on 6/3/2018.
 */

public class EventHandler extends IRtcEngineEventHandler {
    private static final String TAG = "EventHandler";

    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Log.d(TAG, "onJoinChannelSuccess: join chanel");
    }

    @Override
    public void onLeaveChannel(RtcStats stats) {
        Log.d(TAG, "onLeaveChannel: leave chanel");
    }
}
