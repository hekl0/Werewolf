package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.PlayActivity;
import com.example.bm.werewolf.Adapter.RoleListViewAdapter;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NightWaitingFragment extends Fragment {

    ImageView ivPlayingRole;
    TextView tvRoleName;
    @BindView(R.id.iv_roles)
    ImageView ivRoles;
    @BindView(R.id.lv_roles)
    ListView lvRoles;
    Unbinder unbinder;

    public NightWaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_night_waiting, container, false);

        RoleListViewAdapter roleListViewAdapter = new RoleListViewAdapter(RoleReceiveFragment.roleList);
        lvRoles.setAdapter(roleListViewAdapter);

        ivPlayingRole = view.findViewById(R.id.iv_playing_role);
        tvRoleName = view.findViewById(R.id.tv_role_name);

        ivPlayingRole.setImageResource(Constant.imageRole[PlayActivity.currentRole + 1]);
        tvRoleName.setText(Constant.nameRole[PlayActivity.currentRole + 1]);

        unbinder = ButterKnife.bind(this, view);
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
