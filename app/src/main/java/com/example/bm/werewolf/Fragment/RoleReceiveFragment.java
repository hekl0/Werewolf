package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoleReceiveFragment extends Fragment {


    @BindView(R.id.iv_playing_role)
    ImageView ivPlayingRole;
    @BindView(R.id.tv_role_name)
    TextView tvRoleName;
    @BindView(R.id.tv_start_game)
    TextView tvStartGame;
    @BindView(R.id.cl_content)
    ConstraintLayout clContent;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    Unbinder unbinder;

    public RoleReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_role_receive, container, false);
        unbinder = ButterKnife.bind(this, view);

        tvStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DayFragment())
                        .commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("Player Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;

                Constant.listPlayerModel = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlayerModel playerModel = snapshot.getValue(PlayerModel.class);
                    Constant.listPlayerModel.add(playerModel);
                    if (playerModel.id.equals(UserDatabase.facebookID))
                        Constant.myRole = playerModel.role;
                }

                if (avi == null) avi = view.findViewById(R.id.avi);
                if (clContent == null) clContent = view.findViewById(R.id.cl_content);
                if (ivPlayingRole == null) ivPlayingRole = view.findViewById(R.id.iv_playing_role);
                if (tvRoleName == null) tvRoleName = view.findViewById(R.id.tv_role_name);

                avi.setVisibility(View.INVISIBLE);
                clContent.setVisibility(View.VISIBLE);
                ivPlayingRole.setImageResource(Constant.imageRole[Constant.myRole + 1]);
                tvRoleName.setText(Constant.nameRole[Constant.myRole + 1]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
