package com.example.bm.werewolf.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.bm.werewolf.Adapter.DayAdapter;
import com.example.bm.werewolf.Database.DatabaseManager;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;

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

    @BindView(R.id.gv_player)
    GridViewWithHeaderAndFooter gvPlayer;
    Unbinder unbinder;

    Context context;

    public NightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_night, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View header = layoutInflater.inflate(R.layout.layout_header, null);
        gvPlayer.addHeaderView(header);

        DayAdapter dayAdapter = new DayAdapter();
        gvPlayer.setAdapter(dayAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
