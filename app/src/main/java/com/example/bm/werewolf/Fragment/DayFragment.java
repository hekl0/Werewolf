package com.example.bm.werewolf.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.PlayActivity;
import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Adapter.DayAdapter;
import com.example.bm.werewolf.Adapter.DyingAdapter;
import com.example.bm.werewolf.Adapter.RoleListViewAdapter;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Service.VoiceCallService;
import com.example.bm.werewolf.Utils.Constant;
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
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {
    private static final String TAG = "DayFragment";

    Context context;
    @BindView(R.id.et_chat)
    EditText etChat;
    @BindView(R.id.iv_chat_submit)
    ImageView ivChatSubmit;
    @BindView(R.id.iv_voice_call)
    ImageView ivVoiceCall;
    @BindView(R.id.ll1)
    RelativeLayout ll1;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.gv_player)
    GridView gvPlayer;
    @BindView(R.id.tv_start_game)
    TextView tvStartGame;
    Unbinder unbinder;

    public static RelativeLayout rlSmallWindow;
    public static ImageView ivExit;
    public static GridView gvSmallWindow;
    @BindView(R.id.iv_roles)
    ImageView ivRoles;
    @BindView(R.id.lv_roles)
    ListView lvRoles;
    public List<String> dyingList = new ArrayList<>();
    @BindView(R.id.bt_dying)
    Button btDying;
    @BindView(R.id.ll_dying)
    LinearLayout llDying;
    @BindView(R.id.lv_dying)
    ListView lvDying;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    Boolean picked = false;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_room_id)
    TextView tvRoomId;
    @BindView(R.id.tv_timer)
    TextView tvTimer;

    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();

        setupUI(view);

        getDyingList();
        DyingAdapter dyingAdapter = new DyingAdapter(dyingList);
        lvDying.setAdapter(dyingAdapter);

        RoleListViewAdapter roleListViewAdapter = new RoleListViewAdapter(RoleReceiveFragment.roleList);
        lvRoles.setAdapter(roleListViewAdapter);

        List<PlayerModel> playerModelList = new ArrayList<>();
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive == true)
                playerModelList.add(playerModel);
        DayAdapter dayAdapter = new DayAdapter(playerModelList);

        gvPlayer.setAdapter(dayAdapter);
        tvStartGame.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ChatAdapter chatAdapter = new ChatAdapter(Constant.roomID, linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(linearLayoutManager);

        if (VoiceCallService.isVoiceCall) ivVoiceCall.setImageResource(R.drawable.ic_voice_call);
        else ivVoiceCall.setImageResource(R.drawable.ic_mute);

        rlSmallWindow = view.findViewById(R.id.rl_small_window);
        ivExit = view.findViewById(R.id.iv_exit);
        gvSmallWindow = view.findViewById(R.id.gv_small_window);

        return view;
    }

    private void setupUI(final View view) {
        tvRoomId.setText(Constant.roomID);
        new CountDownTimer(120000 + PlayActivity.startTime - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvTimer == null) tvTimer = view.findViewById(R.id.tv_timer);
                tvTimer.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).
                        child("Vote").child(UserDatabase.facebookID).setValue(-1);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_chat_submit, R.id.iv_voice_call, R.id.iv_exit, R.id.bt_dying, R.id.tv_skip, R.id.tv_start_game,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_chat_submit:
                String chat = etChat.getText().toString();
                chat = chat.trim();
                etChat.setText("");
                if (!chat.equals(""))
                    submitChat("[" + UserDatabase.getInstance().userData.name + "]: " + chat);
                break;
            case R.id.iv_voice_call:
                if (VoiceCallService.isVoiceCall) {
                    ivVoiceCall.setImageResource(R.drawable.ic_mute);
                    VoiceCallService.leaveChannel();
                } else {
                    ivVoiceCall.setImageResource(R.drawable.ic_voice_call);
                    VoiceCallService.joinChannel(Constant.roomID);
                }
                VoiceCallService.isVoiceCall = !VoiceCallService.isVoiceCall;
                break;
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
                break;
            case R.id.bt_dying:
                llDying.setVisibility(View.GONE);
                break;
            case R.id.tv_skip:
                if (picked) break;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).
                        child("Vote").child(UserDatabase.facebookID).setValue(-1);
                picked = true;
                break;
            case R.id.tv_start_game:
                if (DayAdapter.pick == -1) break;
                if (picked) break;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).
                        child("Vote").child(UserDatabase.facebookID).setValue(DayAdapter.pick);
                picked = true;
                break;
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
        }
    }

    void submitChat(final String chat) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("chat").child(Constant.roomID);
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

    @OnClick(R.id.iv_roles)
    public void onViewClicked() {
        if (lvRoles.getVisibility() == View.VISIBLE)
            lvRoles.setVisibility(View.GONE);
        else
            lvRoles.setVisibility(View.VISIBLE);
    }

    public void getDyingList() {
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("dyingPlayer").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String a = snapshot.getValue(String.class);
                            dyingList.add(a);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
