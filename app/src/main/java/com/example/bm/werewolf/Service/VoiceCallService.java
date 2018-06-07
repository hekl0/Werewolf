package com.example.bm.werewolf.Service;

import android.content.Context;
import android.util.Log;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

/**
 * Created by bùm on 6/3/2018.
 */

public class VoiceCallService {
    public static boolean isVoiceCall;
    public static RtcEngine rtcEngine;

    public static void joinChannel(String chanelID) {
        rtcEngine.joinChannel(null, chanelID, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
    }

    public static void leaveChannel() {
        rtcEngine.leaveChannel();
    }
}
