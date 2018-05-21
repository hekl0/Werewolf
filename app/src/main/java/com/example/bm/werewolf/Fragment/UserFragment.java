package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bm.werewolf.Adapter.GridViewAdapter;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.AccountManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    @BindView(R.id.iv_ava)
    ImageView ivAva;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_win)
    TextView tvWin;
    @BindView(R.id.tv_lose)
    TextView tvLose;
    @BindView(R.id.tv_total_game)
    TextView tvTotalGame;
    @BindView(R.id.tv_favorite_role)
    TextView tvFavoriteRole;
    @BindView(R.id.tv_heart)
    TextView tvHeart;
    @BindView(R.id.tv_leader)
    TextView tvLeader;
    @BindView(R.id.tv_gg)
    TextView tvGg;
    @BindView(R.id.rl_small_window)
    RelativeLayout rlSmallWindow;
    Unbinder unbinder;
    @BindView(R.id.iv_favorite_role)
    ImageView ivFavoriteRole;
    @BindView(R.id.ll_favorite_role)
    LinearLayout llFavoriteRole;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.gv_list_role)
    GridView gvListRole;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);

        final GridViewAdapter gridViewAdapter = new GridViewAdapter();
        gvListRole.setAdapter(gridViewAdapter);
        gvListRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rlSmallWindow.setVisibility(View.GONE);
                if (position == 0) ivFavoriteRole.setVisibility(View.GONE);
                else ivFavoriteRole.setVisibility(View.VISIBLE);
                ivFavoriteRole.setImageResource(gridViewAdapter.image[position]);
                tvFavoriteRole.setText(gridViewAdapter.name[position]);
            }
        });

        Transformation transformation = new CropCircleTransformation();
        Picasso.get().load(AccountManager.avaLink).transform(transformation).into(ivAva);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_ava, R.id.ll_favorite_role, R.id.iv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ava:
                break;
            case R.id.ll_favorite_role:
                rlSmallWindow.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
                break;
        }
    }
}
