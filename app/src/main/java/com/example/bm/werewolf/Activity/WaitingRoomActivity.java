package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Adapter.WaitingRoomAdapter;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
    @BindView(R.id.tv_room_name)
    TextView tvRoomName;

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

    String roomID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        ButterKnife.bind(this);

        roomID = String.valueOf(getIntent().getIntExtra("roomID", 0));
        RoomLogin();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms").child(roomID).child("roomName");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvRoomName.setText(roomID + "." + dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvWaitingRoom.setLayoutManager(gridLayoutManager);
        rvWaitingRoom.setHasFixedSize(true);
        rvWaitingRoom.setItemViewCacheSize(20);
        rvWaitingRoom.setDrawingCacheEnabled(true);
        rvWaitingRoom.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ChatAdapter chatAdapter = new ChatAdapter(roomID, linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(linearLayoutManager);

        tvName = findViewById(R.id.tv_name);
        ivCover = findViewById(R.id.iv_cover);
        ivAva = findViewById(R.id.iv_ava);
        tvWin = findViewById(R.id.tv_win);
        tvLose = findViewById(R.id.tv_lose);
        ivFavoriteRole = findViewById(R.id.iv_favorite_role);
        tvFavoriteRole = findViewById(R.id.tv_favorite_role);
        ivAddDeleteFriend = findViewById(R.id.iv_add_delete_friend);
        ivExit = findViewById(R.id.iv_exit);
        rlSmallWindow = findViewById(R.id.rl_small_window);
    }

    @OnClick({R.id.iv_back, R.id.iv_invite, R.id.iv_chat_submit, R.id.iv_mute, R.id.iv_add_delete_friend, R.id.iv_exit})
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
            case R.id.iv_add_delete_friend:
                break;
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
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
        final DatabaseReference databaseReference;

        databaseReference = firebaseDatabase.getReference("rooms").child(roomID).child("players");
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

                WaitingRoomAdapter waitingRoomAdapter = new WaitingRoomAdapter(roomID);
                waitingRoomAdapter.setHasStableIds(true);
                rvWaitingRoom.setAdapter(waitingRoomAdapter);
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

                //submitChat(UserDatabase.getInstance().userData.name + " đã rời khỏi phòng. Số người hiện tại là " + playerList.size() + ".");

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

    public static void openSmallWindow(final String userID) {
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
            }
        });
    }

    @Override
    protected void onDestroy() {
        RoomLogout();
        super.onDestroy();
    }
}
