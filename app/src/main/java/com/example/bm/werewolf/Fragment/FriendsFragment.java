package com.example.bm.werewolf.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.FriendsExpandableListViewAdapter;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
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

    ExpandableListView elvFriends;
    FriendsExpandableListViewAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<UserModel> childUser;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        childUser = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User list");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel model = snapshot.getValue(UserModel.class);
                    childUser.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        elvFriends = rootView.findViewById(R.id.elv_friends);
        adapter = new FriendsExpandableListViewAdapter(
                getContext(),
                new ArrayList<String>() {
                    {
                        add("Friends");
                    }
                },
                childUser
        );
        elvFriends.setAdapter(adapter);

        Intent intent = new Intent(getContext(), WaitingRoomActivity.class);
        startActivity(intent);
        return rootView;
    }


}
