package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.PlayActivity;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoleReceiveFragment extends Fragment {
    private static final String TAG = "RoleReceiveFragment";

    @BindView(R.id.iv_playing_role)
    ImageView ivPlayingRole;
    @BindView(R.id.tv_role_name)
    TextView tvRoleName;
    @BindView(R.id.cl_content)
    LinearLayout clContent;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    Unbinder unbinder;

    ValueEventListener valueEventListener;
    public static List<Integer> roleList = new ArrayList<Integer>();

    public RoleReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_role_receive, container, false);
        unbinder = ButterKnife.bind(this, view);

        valueEventListener = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
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

                for (int i = 0; i <= 5; i++)
                    Constant.availableRole[i] = false;
                for (PlayerModel i: Constant.listPlayerModel)
                {
                    Constant.availableRole[i.role] = true;
                }
                int count = 0;
                for(boolean i : Constant.availableRole){
                    if (i) roleList.add(count);
                    count++;
                }


                if (avi == null) avi = view.findViewById(R.id.avi);
                if (clContent == null) clContent = view.findViewById(R.id.cl_content);
                if (ivPlayingRole == null) ivPlayingRole = view.findViewById(R.id.iv_playing_role);
                if (tvRoleName == null) tvRoleName = view.findViewById(R.id.tv_role_name);

                avi.setVisibility(View.INVISIBLE);
                clContent.setVisibility(View.VISIBLE);
                ivPlayingRole.setImageResource(Constant.imageRole[Constant.myRole + 1]);
                tvRoleName.setText(Constant.nameRole[Constant.myRole + 1]);

                PlayActivity.nextTurn();
                PlayActivity.updateTurn();
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

    @Override
    public void onDestroy() {
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                .child("Player Data").removeEventListener(valueEventListener);
        super.onDestroy();
    }
}
