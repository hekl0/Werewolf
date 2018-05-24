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

    public UserModel userModel;

    public static UserDatabase getInstance() {
        if (userDatabase == null)
            userDatabase = new UserDatabase();
            firebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("User list");
        return userDatabase;
    }

    public void updateUser() {
        databaseReference.child(facebookID).setValue(userModel);
    }

    public void accessUser(final String name, final Context context) {
        databaseReference.orderByKey().equalTo(facebookID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    userModel = userSnapShot.getValue(UserModel.class);
                    Log.d(TAG, "onDataChange1: " + userSnapShot.getValue(UserModel.class));
                }
                if (userModel == null) {
                    userModel = new UserModel();
                    userModel.win = 0;
                    userModel.lose = 0;
                    userModel.isOnline = true;
                    userModel.favoriteRole = 0;
                    userModel.name = name;
                    userModel.achievementList = new ArrayList<>();
                    userModel.dataWinRole = new ArrayList<>();
                    userModel.dataTotalRole = new ArrayList<>();
                    for (int i = 0; i < Constant.nameRole.length - 1; i++) {
                        userModel.dataWinRole.add(0);
                        userModel.dataTotalRole.add(0);
                    }
                    userModel.friendList = new ArrayList<>();
                }

                userModel.name = name;
                userModel.isOnline = true;

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
        userModel.isOnline = false;
        updateUser();
    }
}
