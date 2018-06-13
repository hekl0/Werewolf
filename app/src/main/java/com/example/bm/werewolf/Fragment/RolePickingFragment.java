package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Adapter.RolePickingAdapter;
import com.example.bm.werewolf.Adapter.WaitingRoomAdapter;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Model.UserModel;
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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RolePickingFragment extends Fragment {


    @BindView(R.id.et_chat)
    EditText etChat;
    @BindView(R.id.iv_chat_submit)
    ImageView ivChatSubmit;
    @BindView(R.id.iv_voice_call)
    ImageView ivVoiceCall;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.gv_role)
    GridView gvRole;
    Unbinder unbinder;
    @BindView(R.id.tv_start_game)
    TextView tvStartGame;

    ValueEventListener valueEventListener;

    public RolePickingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_role_picking, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ChatAdapter chatAdapter = new ChatAdapter(Constant.roomID, linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(linearLayoutManager);

        RolePickingAdapter rolePickingAdapter = new RolePickingAdapter(getContext());
        gvRole.setAdapter(rolePickingAdapter);

        if (VoiceCallService.isVoiceCall) ivVoiceCall.setImageResource(R.drawable.ic_voice_call);
        else ivVoiceCall.setImageResource(R.drawable.ic_mute);

        valueEventListener = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("pickingFinish").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) return;
                        if (dataSnapshot.getValue(Boolean.class))
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, new RoleReceiveFragment())
                                    .commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if (!Constant.isHost) tvStartGame.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_chat_submit, R.id.iv_voice_call, R.id.tv_start_game})
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
            case R.id.tv_start_game:
                int num = 0;
                for (int x : RolePickingAdapter.count) num += x;
                if (num < Constant.totalPlayer) {
                    Toast.makeText(getContext(), "Chưa đủ số người", Toast.LENGTH_SHORT).show();
                    break;
                }

                final List<String> playerList = Constant.listPlayer;
                Collections.shuffle(playerList);

                for (int i = 0; i < RolePickingAdapter.count.length; i++)
                    if (RolePickingAdapter.count[i] > 0)
                        Constant.availableRole[i] = true;
                    else
                        Constant.availableRole[i] = false;

                Constant.listPlayerModel = new ArrayList<>();
                for (final String x : playerList) {
                    FirebaseDatabase.getInstance().getReference("User list").child(x)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                    int favorite = userModel.favoriteRole;
                                    List<Integer> role = new ArrayList<>();
                                    for (int i = 0; i < RolePickingAdapter.count.length; i++)
                                        for (int j = 0; j < RolePickingAdapter.count[i]; j++) {
                                            role.add(i);
                                            if (i == favorite) role.add(i);
                                        }

                                    Collections.shuffle(role);
                                    int pick = role.get(0);
                                    RolePickingAdapter.count[pick] -= 1;

                                    PlayerModel model = new PlayerModel(x, 0, pick, true, userModel.name);
                                    Constant.listPlayerModel.add(model);

                                    if (Constant.listPlayerModel.size() == playerList.size())
                                        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                                                .child("Player Data").setValue(Constant.listPlayerModel);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                        .child("pickingFinish").setValue(true);

                break;
        }
    }

    static void submitChat(final String chat) {
        ChatAdapter.chatData.add(chat);
        FirebaseDatabase.getInstance().getReference("chat").child(Constant.roomID).setValue(ChatAdapter.chatData);
    }

    @Override
    public void onDestroy() {
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("pickingFinish").removeEventListener(valueEventListener);
        super.onDestroy();
    }
}
