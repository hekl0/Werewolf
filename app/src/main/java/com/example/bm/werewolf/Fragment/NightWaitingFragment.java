package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.PlayActivity;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NightWaitingFragment extends Fragment {

    ImageView ivPlayingRole;
    TextView tvRoleName;

    public NightWaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_night_waiting, container, false);

        ivPlayingRole = view.findViewById(R.id.iv_playing_role);
        tvRoleName = view.findViewById(R.id.tv_role_name);

        ivPlayingRole.setImageResource(Constant.imageRole[PlayActivity.currentRole + 1]);
        tvRoleName.setText(Constant.nameRole[PlayActivity.currentRole + 1]);

        return view;
    }

}
