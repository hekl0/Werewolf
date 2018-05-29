package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
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
    @BindView(R.id.gv_waiting_room)
    GridView gvWaitingRoom;

    String roomID = "0";
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    boolean isWindowChatLarge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(roomID);
        gvWaitingRoom.setAdapter(waitingRoomAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ChatAdapter chatAdapter = new ChatAdapter(roomID, linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(linearLayoutManager);
    }

    private void changeWindowChat() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if (!isWindowChatLarge)
            constraintSet.connect(R.id.rl_chat, ConstraintSet.TOP, R.id.rl1, ConstraintSet.BOTTOM);
        else
            constraintSet.connect(R.id.rl_chat, ConstraintSet.TOP, R.id.guideline2, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);

        isWindowChatLarge = !isWindowChatLarge;
    }

    @OnClick({R.id.iv_back, R.id.iv_invite, R.id.iv_chat_submit, R.id.iv_mute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_invite:
                break;
            case R.id.iv_chat_submit:
                String chat = etChat.getText().toString();
                chat = chat.trim();
                etChat.setText("");
                if (!chat.equals(""))
                    submitChat("[" + UserDatabase.getInstance().userModel.name + "]: " + chat);
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
                Log.d(TAG, "onDataChange: submit");
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
