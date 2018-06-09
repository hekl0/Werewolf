package com.example.bm.werewolf.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm.werewolf.Activity.PlayActivity;
import com.example.bm.werewolf.Adapter.DayAdapter;
import com.example.bm.werewolf.Adapter.RoleListViewAdapter;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
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
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NightFragment extends Fragment {
    private static final String TAG = "NightFragment";

    @BindView(R.id.gv_player)
    GridViewWithHeaderAndFooter gvPlayer;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    Unbinder unbinder;

    Context context;
    @BindView(R.id.iv_roles)
    ImageView ivRoles;
    @BindView(R.id.lv_roles)
    ListView lvRoles;

    public NightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_night, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();

        RoleListViewAdapter roleListViewAdapter = new RoleListViewAdapter(RoleReceiveFragment.roleList);
        lvRoles.setAdapter(roleListViewAdapter);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View header = layoutInflater.inflate(R.layout.layout_header, null);
        gvPlayer.addHeaderView(header);

        List<PlayerModel> playerModelList = new ArrayList<>();
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive == true)
                if (playerModel.role != PlayActivity.currentRole || PlayActivity.currentRole == Constant.BAO_VE)
                    playerModelList.add(playerModel);

        DayAdapter dayAdapter = new DayAdapter(playerModelList);
        gvPlayer.setAdapter(dayAdapter);

        ImageView ivRole = header.findViewById(R.id.iv_role);
        TextView tvRole = header.findViewById(R.id.tv_role);
        TextView tvGuide = header.findViewById(R.id.tv_guide);

        ivRole.setImageResource(Constant.imageRole[PlayActivity.currentRole + 1]);
        tvRole.setText(Constant.nameRole[PlayActivity.currentRole + 1]);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DayAdapter.pick == -1) {
                    Toast.makeText(getContext(), "Cần chọn mục tiêu trước!", Toast.LENGTH_SHORT).show();
                    return;
                }

                tvConfirm.setVisibility(View.GONE);

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID)
                        .child("response");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> responseList = new ArrayList<>();
                        if (dataSnapshot.getValue() != null)
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                responseList.add(snapshot.getValue(String.class));
                        responseList.add(DayAdapter.playerModelList.get(DayAdapter.pick).id);
                        databaseReference.setValue(responseList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        new CountDownTimer(15000 + PlayActivity.startTime - System.currentTimeMillis(), 1000) {

            public void onTick(long millisUntilFinished) {
                if (tvTimer == null) tvTimer = view.findViewById(R.id.tv_timer);
                tvTimer.setText("" + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (tvConfirm == null) tvConfirm = view.findViewById(R.id.tv_confirm);
                tvConfirm.setVisibility(View.GONE);
                if (tvTimer == null) tvTimer = view.findViewById(R.id.tv_timer);
                tvTimer.setText("0");
            }

        }.start();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_roles)
    public void onViewClicked() {
        if (lvRoles.getVisibility() == View.VISIBLE)
            lvRoles.setVisibility(View.GONE);
        else
            lvRoles.setVisibility(View.VISIBLE);
    }
}
