package com.example.bm.werewolf.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.FriendsExpandableListViewAdapter;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    ExpandableListView elvFriends;
    static FriendsExpandableListViewAdapter adapter;

    //small window
    static TextView tvName;
    static ImageView ivCover;
    static ImageView ivAva;
    static TextView tvWin;
    static TextView tvLose;
    static ImageView ivFavoriteRole;
    static TextView tvFavoriteRole;
    static ImageView ivAddDeleteFriend;
    static ImageView ivExit;
    static RelativeLayout rlSmallWindow;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        elvFriends = rootView.findViewById(R.id.elv_friends);

        adapter = new FriendsExpandableListViewAdapter(getContext());
        elvFriends.setAdapter(adapter);

        tvName = rootView.findViewById(R.id.tv_name);
        ivCover = rootView.findViewById(R.id.iv_cover);
        ivAva = rootView.findViewById(R.id.iv_ava);
        tvWin = rootView.findViewById(R.id.tv_win);
        tvLose = rootView.findViewById(R.id.tv_lose);
        ivFavoriteRole = rootView.findViewById(R.id.iv_favorite_role);
        tvFavoriteRole = rootView.findViewById(R.id.tv_favorite_role);
        ivAddDeleteFriend = rootView.findViewById(R.id.iv_add_delete_friend);
        ivExit = rootView.findViewById(R.id.iv_exit);
        rlSmallWindow = rootView.findViewById(R.id.rl_small_window);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSmallWindow.setVisibility(View.GONE);
            }
        });

        return rootView;
    }


    public static void openSmallWindow(final String userID) {
        if (userID.equals(UserDatabase.facebookID))
            return;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User list").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                rlSmallWindow.setVisibility(View.VISIBLE);
                tvWin.setText("Thắng: " + userModel.win);
                tvLose.setText("Thua: " + userModel.lose);
                ivCover.setImageResource(Constant.imageCover[userModel.cover]);
                tvName.setText(userModel.name);
                if (userModel.favoriteRole == 0) ivFavoriteRole.setVisibility(View.GONE);
                else ivFavoriteRole.setVisibility(View.VISIBLE);
                ivFavoriteRole.setImageResource(Constant.imageRole[userModel.favoriteRole]);
                tvFavoriteRole.setText(Constant.nameRole[userModel.favoriteRole]);

                Transformation transformation = new CropCircleTransformation();
                Picasso.get()
                        .load("https://graph.facebook.com/" + userID + "/picture?type=large")
                        .placeholder(R.drawable.progress_animation)
                        .transform(transformation)
                        .into(ivAva);

                if (UserDatabase.getInstance().userData.friendList.contains(userID))
                    ivAddDeleteFriend.setImageResource(R.drawable.ic_delete);
                else
                    ivAddDeleteFriend.setImageResource(R.drawable.ic_add_friend);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivAddDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserDatabase.getInstance().userData.friendList.contains(userID)) {
                    UserDatabase.getInstance().userData.friendList.remove(userID);
                    ivAddDeleteFriend.setImageResource(R.drawable.ic_add_friend);
                } else {
                    UserDatabase.getInstance().userData.friendList.add(userID);
                    ivAddDeleteFriend.setImageResource(R.drawable.ic_delete);
                }
                UserDatabase.getInstance().updateUser();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
