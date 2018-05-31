package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Adapter.WaitingRoomAdapter;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitingRoomActivity extends AppCompatActivity {
    private static final String TAG = "WaitingRoomActivity";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_invite)
    ImageView ivInvite;
    @BindView(R.id.et_chat)
    EditText etChat;
    @BindView(R.id.iv_chat_submit)
    ImageView ivChatSubmit;
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.rv_waiting_room)
    RecyclerView rvWaitingRoom;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;

    String roomID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        roomID = String.valueOf(getIntent().getIntExtra("roomID", 0));
        RoomLogin();

        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(roomID);
        rvWaitingRoom.setAdapter(waitingRoomAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rvWaitingRoom.setLayoutManager(gridLayoutManager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ChatAdapter chatAdapter = new ChatAdapter(roomID, linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.iv_back, R.id.iv_invite, R.id.iv_chat_submit, R.id.iv_mute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_invite:
                break;
            case R.id.iv_chat_submit:
                String chat = etChat.getText().toString();
                chat = chat.trim();
                etChat.setText("");
                if (!chat.equals(""))
                    submitChat("[" + UserDatabase.getInstance().userData.name + "]: " + chat);
                break;
            case R.id.iv_mute:
                break;
        }
    }

    void submitChat(final String chat) {
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

    void RoomLogin() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("players");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> playerList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    playerList.add(snapshot.getValue(String.class));
                playerList.add(UserDatabase.facebookID);
                databaseReference.setValue(playerList);

                if (getIntent().getBooleanExtra("isHost", true))
                    submitChat(UserDatabase.getInstance().userData.name + " đã tạo phòng.");
                else
                    submitChat(UserDatabase.getInstance().userData.name + " đã vào phòng. Số người hiện tại là " + playerList.size() + ".");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void RoomLogout() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("players");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> playerList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    if (!snapshot.getValue(String.class).equals(UserDatabase.facebookID))
                        playerList.add(snapshot.getValue(String.class));
                databaseReference.setValue(playerList);

                submitChat(UserDatabase.getInstance().userData.name + " đã rời khỏi phòng. Số người hiện tại là " + playerList.size() + ".");

                if (playerList.size() == 0) {
                    firebaseDatabase.getReference("chat").child(roomID).removeValue();
                    firebaseDatabase.getReference("rooms").child(roomID).removeValue();
                } else if (WaitingRoomAdapter.hostID.equals(UserDatabase.facebookID)) {
                    Random random = new Random();
                    String nextHost = playerList.get(random.nextInt(playerList.size()));
                    firebaseDatabase.getReference("rooms").child(roomID).child("roomMasterID").setValue(nextHost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        RoomLogout();
        super.onDestroy();
    }
}
