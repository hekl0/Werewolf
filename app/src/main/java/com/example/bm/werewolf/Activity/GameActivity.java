package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.bm.werewolf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.iv_invite, R.id.iv_chat_submit, R.id.iv_mute, R.id.rv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_invite:
                break;
            case R.id.iv_chat_submit:
                break;
            case R.id.iv_mute:
                break;
            case R.id.rv_chat:
                break;
        }
    }
}
