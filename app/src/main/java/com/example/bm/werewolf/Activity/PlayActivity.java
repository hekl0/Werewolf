package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Fragment.DayFragment;
import com.example.bm.werewolf.Fragment.NightFragment;
import com.example.bm.werewolf.Fragment.NightWaitingFragment;
import com.example.bm.werewolf.Fragment.RolePickingFragment;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Service.OnClearFromRecentService;
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

import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    public static int currentRole;
    public static long startTime;

    public static List<String> response;
    public static ValueEventListener updateTurnListener;
    public static ValueEventListener responseListener;
    public static ValueEventListener voteListener;

    public static Boolean healPotion;
    public static Boolean toxicPotion;
    public static String lastProtectedPlayerID;
    public static List<String> dyingPlayerID;
    public static String lastTargetPlayerID;

    static FragmentManager fragmentManager;

    static List<String> voter;
    static List<String> votedPlayer;

    public static boolean isWin = false;
    public static Handler handler;

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

    public static void updateTurn() {
        final boolean[] isOn = {false};

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

                        if (currentRole == Constant.NONE && isOn[0])
                            VoiceCallService.joinChannel(Constant.roomID);
                        else {
                            isOn[0] = VoiceCallService.isVoiceCall;
                            VoiceCallService.leaveChannel();
                        }

                        if (currentRole == Constant.NONE)
                            loadFragment(new DayFragment());
                        else if (Constant.myRole == currentRole && getPlayerModelByID(UserDatabase.facebookID).alive)
                            loadFragment(new NightFragment());
                        else
                            loadFragment(new NightWaitingFragment());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    //Soi, Bao ve, Tho San, Phu Thuy, Tien tri
    static int[] roleSequence = new int[]{Constant.NONE, Constant.MA_SOI, Constant.BAO_VE, Constant.THO_SAN, Constant.PHU_THUY, Constant.TIEN_TRI, Constant.NONE};

    public static void nextTurn() {
        if (!Constant.isHost) return;

        if (currentRole == Constant.NONE) {
            dyingPlayerID = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                    .child("dyingPlayer").setValue(dyingPlayerID);
            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("hangedPlayerID").setValue(null);
            FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                    .child("Player Data").setValue(Constant.listPlayerModel);
        }

        int pos;
        for (pos = 0; pos < roleSequence.length; pos++)
            if (roleSequence[pos] == currentRole) break;

        int nextRole;
        for (nextRole = pos + 1; nextRole < roleSequence.length; nextRole++)
            if (roleSequence[nextRole] == Constant.NONE) break;
            else if (Constant.availableRole[roleSequence[nextRole]]) break;

        currentRole = roleSequence[nextRole];

        handler = new Handler();
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

                if (currentRole == Constant.NONE) Vote();
                else receiveResponse();
            }
        }, 2000);
    }

    public static void receiveResponse() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response");
        databaseReference.setValue(null);
        response = new ArrayList<>();

        //counting number of response need to receive
        int count = 0;
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive && playerModel.role == currentRole) {
                count += 1;
                if (currentRole == Constant.PHU_THUY) count += 1;
            }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: fail");
                analyzeResponse();
            }
        }, 16000);

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
        FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response").removeEventListener(responseListener);
        Log.d(TAG, "analyzeResponse: " + (responseListener == null));

        while (response.contains(""))
            response.remove("");

        int maxCount = 0;
        for (String x : response)
            if (Collections.frequency(response, x) > maxCount)
                maxCount = Collections.frequency(response, x);

        if (maxCount != 0) {
            String kq = "";
            Collections.shuffle(response);
            for (String x : response)
                if (Collections.frequency(response, x) == maxCount)
                    kq = x;

            if (currentRole == Constant.MA_SOI)
                dyingPlayerID.add(kq);
            if (currentRole == Constant.BAO_VE) {
                lastProtectedPlayerID = kq;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                        .child("lastProtectedPlayerID").setValue(lastProtectedPlayerID);
                if (dyingPlayerID.contains(kq)) dyingPlayerID.remove(kq);
            }
            if (currentRole == Constant.THO_SAN) {
                lastTargetPlayerID = kq;
                for (PlayerModel playerModel : Constant.listPlayerModel)
                    if (playerModel.role == Constant.THO_SAN)
                        if (dyingPlayerID.contains(playerModel.id))
                            dyingPlayerID.add(lastTargetPlayerID);
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
                    } else {
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

    public static void Vote() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Vote");
        databaseReference.setValue(null);
        voter = new ArrayList<>();
        votedPlayer = new ArrayList<>();

        int count = 0;
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive && !dyingPlayerID.contains(playerModel.id)) count++;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: fail");
                finishVote();
            }
        }, 122000);

        final int finalCount = count;
        voteListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                voter = new ArrayList<>();
                votedPlayer = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    voter.add(snapshot.getKey());
                    votedPlayer.add(snapshot.getValue(String.class));
                }

                if (voter.size() != finalCount) return;

                handler.removeCallbacksAndMessages(null);
                finishVote();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void finishVote() {
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("Vote").removeEventListener(voteListener);

        String hangedPlayerID = "";

        int total = 0;
        for (String s : votedPlayer)
            if (!s.equals("")) total++;
        for (String s : votedPlayer) {
            if (s.equals("")) continue;
            int count = 0;
            for (String x : votedPlayer)
                if (s.equals(x)) count++;

            if (count > total / 2) hangedPlayerID = s;
        }

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("hangedPlayerID").setValue(hangedPlayerID);

        String systemChat = "";

        for (int i = 0; i < votedPlayer.size(); i++) {
            String id = votedPlayer.get(i);
            if (id.equals("")) continue;

            List<String> voterList = new ArrayList<>();
            for (int j = 0; j < votedPlayer.size(); j++)
                if (votedPlayer.get(j).equals(id))
                    voterList.add(voter.get(j));

            String chat = getPlayerModelByID(id).name + " đã bị bỏ phiếu bởi " + voterList.size() + " người: ";
            for (int j = 0; j < voterList.size(); j++) {
                chat += getPlayerModelByID(voterList.get(j)).name;
                if (j != voterList.size() - 1) chat += ", ";
                else chat += ".";
            }

            if (voterList.size() > 0) {
                if (!systemChat.equals("")) systemChat += "\n";
                systemChat += chat;
            }
        }

        if (!hangedPlayerID.equals("")) {
            if (!systemChat.equals("")) systemChat += "\n";
            systemChat += getPlayerModelByID(hangedPlayerID).name + " đã bị treo cổ.";
        } else {
            if (!systemChat.equals("")) systemChat += "\n";
            systemChat += "Ý kiến không thống nhất, không ai bị treo cổ.";
        }

        submitChat(systemChat);
        submitChat("10s nữa sẽ đến tối");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextTurn();
            }
        }, 8000);
    }

    public static PlayerModel getPlayerModelByID(String id) {
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.id.equals(id))
                return playerModel;
        return null;
    }

    static void submitChat(final String chat) {
        ChatAdapter.chatData.add(chat);
        FirebaseDatabase.getInstance().getReference("chat").child(Constant.roomID).setValue(ChatAdapter.chatData);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("Vote").removeEventListener(voteListener);
        FirebaseDatabase.getInstance().getReference("Ingame Data")
                .child(Constant.roomID).child("response").removeEventListener(responseListener);
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                .removeEventListener(updateTurnListener);

        int temp;
        if (!isWin) {
            UserDatabase.getInstance().userData.lose++;
            temp = UserDatabase.getInstance().userData.dataTotalRole.get(Constant.myRole);
            UserDatabase.getInstance().userData.dataTotalRole.set(Constant.myRole, temp + 1);
        } else {
            UserDatabase.getInstance().userData.win++;
            Log.d(TAG, "onDestroy: " + UserDatabase.getInstance().userData.dataTotalRole.get(Constant.myRole));
            temp = UserDatabase.getInstance().userData.dataTotalRole.get(Constant.myRole);
            UserDatabase.getInstance().userData.dataTotalRole.set(Constant.myRole, temp + 1);
            Log.d(TAG, "onDestroy: " + UserDatabase.getInstance().userData.dataTotalRole.get(Constant.myRole));
            temp = UserDatabase.getInstance().userData.dataWinRole.get(Constant.myRole);
            UserDatabase.getInstance().userData.dataWinRole.set(Constant.myRole, temp + 1);
        }

        List<String> recent = new ArrayList<>();
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (!playerModel.id.equals(UserDatabase.facebookID))
                recent.add(playerModel.id);
        for (String s : UserDatabase.getInstance().userData.recentPlayWith) {
            if (recent.size() >= 30) break;
            recent.add(s);
        }
        UserDatabase.getInstance().userData.recentPlayWith = recent;

        UserDatabase.getInstance().updateUser();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        OnClearFromRecentService.activity = this;
        super.onResume();
    }
}
