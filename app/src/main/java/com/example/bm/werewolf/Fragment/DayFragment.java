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
import android.widget.Toast;

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
    @BindView(R.id.iv_roles)
    ImageView ivRoles;
    @BindView(R.id.lv_roles)
    ListView lvRoles;
    @BindView(R.id.bt_dying)
    Button btDying;
    @BindView(R.id.ll_dying)
    LinearLayout llDying;
    @BindView(R.id.lv_dying)
    ListView lvDying;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_room_id)
    TextView tvRoomId;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    Unbinder unbinder;

    public static RelativeLayout rlSmallWindow;
    public static ImageView ivExit;
    public static GridView gvSmallWindow;
    public View view;

    public List<String> dyingList = new ArrayList<>();

    ValueEventListener valueEventListener;

    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();

        checkAlive();
        setupUI(view);
        getDyingList();

        RoleListViewAdapter roleListViewAdapter = new RoleListViewAdapter(RoleReceiveFragment.roleList);
        lvRoles.setAdapter(roleListViewAdapter);

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

        valueEventListener = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("hangedPlayerID").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) return;
                        for (PlayerModel playerModel : Constant.listPlayerModel)
                            if (playerModel.id.equals(dataSnapshot.getValue(String.class)))
                                playerModel.alive = false;
                        checkAlive();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    private void setupUI(final View view) {
        tvRoomId.setText(Constant.roomID);
        new CountDownTimer(120000 + PlayActivity.startTime - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvTimer == null) tvTimer = view.findViewById(R.id.tv_timer);
                tvTimer.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (tvTimer == null) tvTimer = view.findViewById(R.id.tv_timer);
                if (tvStartGame == null) tvStartGame = view.findViewById(R.id.tv_start_game);
                if (tvSkip == null) tvSkip = view.findViewById(R.id.tv_skip);
                tvTimer.setText("0");
                tvStartGame.setVisibility(View.GONE);
                tvSkip.setVisibility(View.GONE);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_chat_submit, R.id.iv_voice_call, R.id.iv_exit, R.id.bt_dying, R.id.tv_skip, R.id.tv_start_game, R.id.iv_back, R.id.iv_roles})
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
                break;
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
                break;
            case R.id.bt_dying:
                llDying.setVisibility(View.GONE);
                break;
            case R.id.tv_skip:
                tvSkip.setVisibility(View.GONE);
                tvStartGame.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).
                        child("Vote").child(UserDatabase.facebookID).setValue("");
                break;
            case R.id.tv_start_game:
                if (DayAdapter.pick == -1) {
                    Toast.makeText(getContext(), "Cần chọn mục tiêu trước", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvSkip.setVisibility(View.GONE);
                tvStartGame.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).
                        child("Vote").child(UserDatabase.facebookID).setValue(DayAdapter.playerModelList.get(DayAdapter.pick).id);
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_roles:
                if (lvRoles.getVisibility() == View.VISIBLE)
                    lvRoles.setVisibility(View.GONE);
                else
                    lvRoles.setVisibility(View.VISIBLE);
        }
    }

    static void submitChat(final String chat) {
        ChatAdapter.chatData.add(chat);
        FirebaseDatabase.getInstance().getReference("chat").child(Constant.roomID).setValue(ChatAdapter.chatData);
    }

    public void getDyingList() {
        dyingList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("dyingPlayer").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (lvDying == null) lvDying = view.findViewById(R.id.lv_dying);
                        if (gvPlayer == null) gvPlayer = view.findViewById(R.id.gv_player);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String a = snapshot.getValue(String.class);
                            dyingList.add(a);
                        }

                        for (PlayerModel playerModel : Constant.listPlayerModel)
                            if (dyingList.contains(playerModel.id)) playerModel.alive = false;

                        DyingAdapter dyingAdapter = new DyingAdapter(dyingList);
                        lvDying.setAdapter(dyingAdapter);

                        DayAdapter dayAdapter = new DayAdapter(Constant.listPlayerModel, getContext());
                        gvPlayer.setAdapter(dayAdapter);

                        checkAlive();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    void checkAlive() {
        boolean alive = true;
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.id.equals(UserDatabase.facebookID))
                alive = playerModel.alive;

        if (!alive) {
            if (tvStartGame == null) tvStartGame = view.findViewById(R.id.tv_start_game);
            if (tvSkip == null) tvSkip = view.findViewById(R.id.tv_skip);
            if (ll1 == null) ll1 = view.findViewById(R.id.ll1);
            if (VoiceCallService.isVoiceCall) VoiceCallService.leaveChannel();
            tvStartGame.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
        } else {
            if (tvStartGame == null) tvStartGame = view.findViewById(R.id.tv_start_game);
            if (tvSkip == null) tvSkip = view.findViewById(R.id.tv_skip);
            tvStartGame.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.VISIBLE);
        }

        int total = 0;
        int wolf = 0;
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive) {
                total++;
                if (playerModel.role == Constant.MA_SOI) wolf++;
            }

        if (wolf == 0 || wolf*2 >= total)
            PlayActivity.loadFragment(new EndGameFragment());
    }

    @Override
    public void onDestroy() {
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("hangedPlayerID").removeEventListener(valueEventListener);
        super.onDestroy();
    }
}
