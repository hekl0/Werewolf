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
            firebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("User list").child(facebookID);
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
                }

                if (userData.friendList == null)
                    userData.friendList = new ArrayList<>();
                if (userData.recentPlayWith == null)
                    userData.recentPlayWith = new ArrayList<>();
                userData.name = name;
                userData.isOnline = true;

                updateUser();

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                //((Activity)context).finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void offlineStatus() {
        userData.isOnline = false;
        updateUser();
    }
}
