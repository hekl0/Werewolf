package com.example.bm.werewolf.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Database.DatabaseManager;
import com.example.bm.werewolf.Fragment.DayFragment;
import com.example.bm.werewolf.Fragment.NightFragment;
import com.example.bm.werewolf.Fragment.RolePickingFragment;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";

    @BindView(R.id.tv_timer)
    TextView tvTimer;

    public List<PlayerModel> playerModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        playerModels = DatabaseManager.getInstance(this).getListPlayer();

        loadFragment(new RolePickingFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
