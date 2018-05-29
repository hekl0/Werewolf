package com.example.bm.werewolf.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;
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
 * Created by bùm on 28/05/2018.
 */

public class WaitingRoomAdapter extends BaseAdapter {
    private static final String TAG = "WaitingRoomAdapter";

    List<String> playerList = new ArrayList<>();
    String hostID;

    public WaitingRoomAdapter(String roomID) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("roomMasterID");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostID = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("players");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playerList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    playerList.add(String.valueOf(snapshot.getValue()));
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.item_waiting_player, parent, false);

        final TextView tvName = convertView.findViewById(R.id.tv_name);
        final ImageView ivAva = convertView.findViewById(R.id.iv_ava);
        final ImageView ivChangeHost = convertView.findViewById(R.id.iv_change_host);
        final ImageView ivHost = convertView.findViewById(R.id.iv_host);
        final ImageView ivKick = convertView.findViewById(R.id.iv_kick);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User list").child(playerList.get(position)).child("name");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);

                tvName.setText(name);

                Transformation transformation = new CropCircleTransformation();
                Picasso.get()
                        .load("https://graph.facebook.com/" + playerList.get(position) + "/picture?type=large")
                        .placeholder(R.drawable.progress_animation)
                        .transform(transformation)
                        .into(ivAva);
                if (playerList.get(position).equals(hostID)) {
                    ivChangeHost.setVisibility(View.GONE);
                    ivHost.setVisibility(View.VISIBLE);
                    ivKick.setVisibility(View.GONE);
                } else {
                    ivChangeHost.setVisibility(View.VISIBLE);
                    ivHost.setVisibility(View.INVISIBLE);
                    ivKick.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
