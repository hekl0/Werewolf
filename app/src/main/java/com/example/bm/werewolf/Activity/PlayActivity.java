package com.example.bm.werewolf.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Database.DatabaseManager;
import com.example.bm.werewolf.Fragment.DayFragment;
import com.example.bm.werewolf.Fragment.NightFragment;
import com.example.bm.werewolf.Fragment.NightWaitingFragment;
import com.example.bm.werewolf.Fragment.RolePickingFragment;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Service.VoiceCallService;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    public static int currentRole;
    public static long startTime;

    static List<String> response;
    static ValueEventListener updateTurnListener;
    static ValueEventListener responseListener;

    public static Boolean healPotion;
    public static Boolean toxicPotion;
    public static String lastProtectedPlayerID;
    public static List<String> dyingPlayerID;
    public static String lastTargetPlayerID;

    static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        currentRole = Constant.NONE;
        dyingPlayerID = new ArrayList<>();
        Constant.listPlayerModel = null;
        healPotion = null;
        toxicPotion = null;
        lastProtectedPlayerID = null;
        lastTargetPlayerID = null;

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("healPotion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) healPotion = true;
                else healPotion = dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("toxicPotion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) toxicPotion = true;
                else toxicPotion = dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("lastProtectedPlayerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) lastProtectedPlayerID = "";
                else lastProtectedPlayerID = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("lastTargetPlayerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) lastTargetPlayerID = "";
                else lastTargetPlayerID = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loadFragment(new RolePickingFragment());
    }

    public static void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    //Soi, Bao ve, Tho San, Phu Thuy, Tien tri
    static int[] roleSequence = new int[]{Constant.NONE, Constant.MA_SOI, Constant.BAO_VE, Constant.THO_SAN, Constant.PHU_THUY, Constant.TIEN_TRI, Constant.NONE};

    public static void nextTurn() {
        if (!Constant.isHost) return;

        if (currentRole == Constant.NONE) {
            dyingPlayerID = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                    .child("dyingPlayer").setValue(dyingPlayerID);
        }

        int pos;
        for (pos = 0; pos < roleSequence.length; pos++)
            if (roleSequence[pos] == currentRole) break;

        int nextRole;
        for (nextRole = pos + 1; nextRole < roleSequence.length; nextRole++)
            if (roleSequence[nextRole] == Constant.NONE) break;
            else if (Constant.availableRole[roleSequence[nextRole]]) break;

        currentRole = roleSequence[nextRole];

        Handler handler = new Handler();
        final int finalNextRole = nextRole;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + System.currentTimeMillis());
                Log.d(TAG, "onData: set");
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                        .child("currentRole").setValue(roleSequence[finalNextRole]);
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                        .child("startTime").setValue(System.currentTimeMillis());
                receiveResponse();
            }
        }, 2000);
    }

    public static void receiveResponse() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response");

        databaseReference.setValue(null);
        response = new ArrayList<>();

        int count = 0;
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive && playerModel.role == currentRole) {
                count += 1;
                if (currentRole == Constant.PHU_THUY) count += 1;
            }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: fail");
                analyzeResponse();
            }
        }, 17000);

        if (responseListener != null) {
            databaseReference.removeEventListener(responseListener);
            responseListener = null;
        }
        final int finalCount = count;
        responseListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response = new ArrayList<>();
                if (dataSnapshot.getValue() == null) return;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    response.add(snapshot.getValue(String.class));

                if (response.size() != finalCount) return;
                handler.removeCallbacksAndMessages(null);

                analyzeResponse();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void analyzeResponse() {
        int maxCount = 0;
        for (String x : response)
            if (Collections.frequency(response, x) > maxCount && !x.equals(""))
                maxCount = Collections.frequency(response, x);

        if (maxCount != 0) {
            String kq = "";
            Collections.shuffle(response);
            for (String x : response)
                if (Collections.frequency(response, x) == maxCount && !x.equals(""))
                    kq = x;

            if (currentRole == Constant.MA_SOI)
                dyingPlayerID.add(kq);
            if (currentRole == Constant.BAO_VE) {
                lastProtectedPlayerID = kq;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                        .child("lastProtectedPlayerID").setValue(lastProtectedPlayerID);
                if (dyingPlayerID.contains(kq)) dyingPlayerID = new ArrayList<>();
            }
            if (currentRole == Constant.THO_SAN) {
                lastTargetPlayerID = kq;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                        .child("lastTargetPlayerID").setValue(lastTargetPlayerID);
            }
            if (currentRole == Constant.PHU_THUY) {
                for (String x : response)
                    if (x.equals("saving")) {
                        dyingPlayerID = new ArrayList<>();
                        healPotion = false;
                        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                                .child("healPotion").setValue(healPotion);
                    } else if (!x.equals("")) {
                        dyingPlayerID.add(x);
                        toxicPotion = false;
                        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                                .child("toxicPotion").setValue(toxicPotion);
                    }
            }

            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                    .child("dyingPlayer").setValue(dyingPlayerID);
        }

        nextTurn();
    }

    public static void updateTurn() {
        updateTurnListener = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        if (dataSnapshot.getValue() == null) return;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            if (snapshot.getKey().equals("currentRole"))
                                currentRole = snapshot.getValue(Integer.class);
                            else
                                startTime = snapshot.getValue(long.class);

                        if (VoiceCallService.isVoiceCall)
                            if (currentRole == Constant.NONE)
                                VoiceCallService.joinChannel(Constant.roomID);
                            else
                                VoiceCallService.leaveChannel();

                        if (currentRole == Constant.NONE)
                            loadFragment(new DayFragment());
                        else if (Constant.myRole == currentRole)
                            loadFragment(new NightFragment());
                        else
                            loadFragment(new NightWaitingFragment());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (responseListener != null)
            FirebaseDatabase.getInstance().getReference("Ingame Data")
                    .child(Constant.roomID).child("response").removeEventListener(responseListener);
        if (updateTurnListener != null)
            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                    .removeEventListener(updateTurnListener);
        super.onDestroy();
    }
}
