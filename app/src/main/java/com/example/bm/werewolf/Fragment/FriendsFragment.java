package com.example.bm.werewolf.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.FriendsExpandableListViewAdapter;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    ExpandableListView elvFriends;
    FriendsExpandableListViewAdapter adapter;

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

        return rootView;
    }


}
