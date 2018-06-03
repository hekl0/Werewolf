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

import com.example.bm.werewolf.Adapter.ChatAdapter;
import com.example.bm.werewolf.Adapter.RolePickingAdapter;
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_chat_submit, R.id.iv_voice_call})
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
}
