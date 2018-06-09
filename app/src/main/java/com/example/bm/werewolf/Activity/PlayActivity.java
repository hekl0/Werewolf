package com.example.bm.werewolf.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        loadFragment(new RolePickingFragment());
        updateTurn();
    }

    @Override
    protected void onResume() {
        currentRole = Constant.NONE;
        super.onResume();
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    //Soi, Bao ve, Tho San, Phu Thuy, Tien tri
    static int[] roleSequence = new int[]{Constant.NONE, Constant.MA_SOI, Constant.BAO_VE, Constant.THO_SAN, Constant.PHU_THUY, Constant.TIEN_TRI, Constant.NONE};

    public static void nextTurn() {
        Log.d(TAG, "nextTurn: " + Constant.availableRole[1] + " " + currentRole);
        if (!Constant.isHost) return;
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
        }, 3000);
    }

    public static void receiveResponse() {
        response = new ArrayList<>();

        int count = 0;
        for (PlayerModel playerModel : Constant.listPlayerModel) {
            if (playerModel.alive && playerModel.role == currentRole)
                count += 1;
            Log.d(TAG, "receiveResponse: " + playerModel.alive + " " + playerModel.role + " " + currentRole);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: fail");
            }
        }, 17000);

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateTurn() {
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
        FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response").removeEventListener(responseListener);
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                .removeEventListener(updateTurnListener);
        super.onDestroy();
    }
}
