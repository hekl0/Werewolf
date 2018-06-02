package com.example.bm.werewolf.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.bm.werewolf.Adapter.GridViewAdapter;
import com.example.bm.werewolf.Database.DatabaseManager;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {

    Context context;
    @BindView(R.id.et_chat)
    EditText etChat;
    @BindView(R.id.iv_chat_submit)
    ImageView ivChatSubmit;
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.ll1)
    RelativeLayout ll1;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.gv_player)
    GridView gvPlayer;
    Unbinder unbinder;

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

        List<PlayerModel> playerModels = DatabaseManager.getInstance(context).getListPlayer();

        GridViewAdapter gridViewAdapter = new GridViewAdapter(playerModels, context);
        gvPlayer.setAdapter(gridViewAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_chat_submit, R.id.iv_mute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_chat_submit:
                break;
            case R.id.iv_mute:
                break;
        }
    }
}
