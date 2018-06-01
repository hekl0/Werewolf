package com.example.bm.werewolf.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by bùm on 28/05/2018.
 */

public class WaitingRoomAdapter extends RecyclerView.Adapter<WaitingRoomAdapter.WaitingRoomViewHolder> {
    private static final String TAG = "WaitingRoomAdapter";

    public static List<String> playerList = new ArrayList<>();
    public static String hostID;
    public String roomID;

    public WaitingRoomAdapter(String roomID) {
        this.roomID = roomID;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("roomMasterID");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostID = dataSnapshot.getValue(String.class);
                notifyDataSetChanged();
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
                    playerList.add(snapshot.getValue(String.class));
                Log.d(TAG, "onDataChange: " + playerList.get(0));
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public WaitingRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_waiting_player, parent, false);
        return new WaitingRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingRoomViewHolder holder, int position) {
        holder.setData(playerList.get(position));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class WaitingRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_ava)
        ImageView ivAva;
        @BindView(R.id.iv_change_host)
        ImageView ivChangeHost;
        @BindView(R.id.iv_host)
        ImageView ivHost;
        @BindView(R.id.iv_kick)
        ImageView ivKick;

        View view;
        String playerID;
        String name;

        public WaitingRoomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(final String playerID) {
            this.playerID = playerID;

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("User list").child(playerID).child("name");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.getValue(String.class);
                    tvName.setText(name);

                    Transformation transformation = new CropCircleTransformation();
                    Picasso.get()
                            .load("https://graph.facebook.com/" + playerID + "/picture?type=large")
                            .placeholder(R.drawable.progress_animation)
                            .transform(transformation)
                            .into(ivAva);

                    if (playerID.equals(hostID)){
                        ivChangeHost.setVisibility(View.GONE);
                        ivHost.setVisibility(View.VISIBLE);
                        ivKick.setVisibility(View.GONE);
                    } else if (!playerID.equals(hostID) && UserDatabase.facebookID.equals(hostID)){
                        ivChangeHost.setVisibility(View.VISIBLE);
                        ivHost.setVisibility(View.INVISIBLE);
                        ivKick.setVisibility(View.VISIBLE);
                    } else if (!playerID.equals(hostID) && !UserDatabase.facebookID.equals(hostID)) {
                        ivChangeHost.setVisibility(View.GONE);
                        ivHost.setVisibility(View.INVISIBLE);
                        ivKick.setVisibility(View.GONE);
                    }

                    ivChangeHost.setOnClickListener(WaitingRoomViewHolder.this);
                    ivKick.setOnClickListener(WaitingRoomViewHolder.this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference;
            switch (v.getId()) {
                case R.id.iv_change_host:
                    submitSystemChat(UserDatabase.getInstance().userData.name + " đã nhường quyền chủ phòng cho " + name + ".");
                    databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("roomMasterID");
                    databaseReference.setValue(playerID);
                    break;
                case R.id.iv_kick:
                    submitSystemChat(UserDatabase.getInstance().userData.name + " đã đá " + name + " ra khỏi phòng.");
                    databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("players");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            playerList = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                if (!snapshot.getValue(String.class).equals(playerID))
                                    playerList.add(snapshot.getValue(String.class));
                            databaseReference.setValue(playerList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        }

        public void submitSystemChat(final String chat) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference("chat").child(roomID);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> chatList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        chatList.add(snapshot.getValue(String.class));
                    chatList.add(chat);
                    databaseReference.setValue(chatList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

