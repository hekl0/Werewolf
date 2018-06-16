package com.example.bm.werewolf.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.bm.werewolf.Activity.MainActivity;
import com.example.bm.werewolf.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bùm on 22/05/2018.
 */

public class UserDatabase {
    private static final String TAG = "UserDatabase";

    public static com.google.firebase.database.FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;

    public static UserDatabase userDatabase;
    public static String facebookID;

    public UserModel userData;

    public static UserDatabase getInstance() {
        if (userDatabase == null)
            userDatabase = new UserDatabase();
        databaseReference = FirebaseDatabase.getInstance().getReference("User list").child(facebookID);

        return userDatabase;
    }

    public void updateUser() {
        databaseReference.setValue(userData);
    }

    public void accessUser(final String name, final Context context) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 userData = dataSnapshot.getValue(UserModel.class);
                if (userData == null) {
                    userData = new UserModel();
                    userData.win = 0;
                    userData.lose = 0;
                    userData.isOnline = true;
                    userData.favoriteRole = 0;
                    userData.name = name;
                    userData.cover = 0;
                    userData.achievementList = new ArrayList<>();
                    userData.dataWinRole = new ArrayList<>();
                    userData.dataTotalRole = new ArrayList<>();
                    for (int i = 0; i < Constant.nameRole.length - 1; i++) {
                        userData.dataWinRole.add(0);
                        userData.dataTotalRole.add(0);
                    }
                    userData.friendList = new ArrayList<>();
                    userData.recentPlayWith = new ArrayList<>();
                    userData.achievedCover = new ArrayList<>();
                    userData.achievedCover.add(0);
                    userData.id = facebookID;
                }

                if (userData.friendList == null)
                    userData.friendList = new ArrayList<>();
                if (userData.recentPlayWith == null)
                    userData.recentPlayWith = new ArrayList<>();
                userData.name = name;
                userData.isOnline = true;
                userData.id = facebookID;

                int soiwin  = userData.dataWinRole.get(1);
                int soilose = userData.dataTotalRole.get(1) - soiwin;
                if (soiwin >= 10 && soilose >= 10) {
                    boolean exist = false;
                    for (int i : userData.achievedCover)
                        if (i == 2) exist = true;
                    if (!exist) userData.achievedCover.add(2);
                }

                int nguoiwin = 0;
                int nguoilose = 0;
                for (int i = 0; i <= userData.dataWinRole.size()-1; i++) {
                    if (i != 1) {
                        nguoiwin += userData.dataWinRole.get(i);
                        nguoilose += userData.dataTotalRole.get(i);
                    }
                }
                nguoilose -= nguoiwin;
                if (nguoilose >= 10 && nguoiwin >= 10){
                    boolean exist = false;
                    for (int i : userData.achievedCover)
                        if (i == 1) exist = true;
                    if (!exist) userData.achievedCover.add(1);
                }

                updateUser();

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void offlineStatus() {
        if (userData == null) return;
        userData.isOnline = false;
        updateUser();
    }
}
