package com.example.bm.werewolf.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.bm.werewolf.Utils.UserDatabase;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    String[] roleName;
    float[] winRate;

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.gv_list_role)
    GridView gvListRole;
    @BindView(R.id.rl_small_window)
    RelativeLayout rlSmallWindow;
    @BindView(R.id.iv_wall)
    ImageView ivWall;
    @BindView(R.id.iv_ava)
    ImageView ivAva;
    @BindView(R.id.tv_lose)
    TextView tvLose;
    @BindView(R.id.tv_win)
    TextView tvWin;
    @BindView(R.id.radar_chart)
    RadarChart radarChart;
    @BindView(R.id.iv_favorite_role)
    ImageView ivFavoriteRole;
    @BindView(R.id.tv_favorite_role)
    TextView tvFavoriteRole;
    @BindView(R.id.ll_favorite_role)
    LinearLayout llFavoriteRole;
    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView tvName;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);

        initSmallWindow();

        Transformation transformation = new CropCircleTransformation();

        Picasso.get()
                .load("https://graph.facebook.com/" + UserDatabase.facebookID + "/picture?type=large")
                .transform(transformation)
                .into(ivAva);

        tvName.setText(UserDatabase.getInstance().userModel.name);

        return view;
    }

    public void initSmallWindow() {
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
    }

    public void initRadarChart() {
        List<RadarEntry> entries = new ArrayList<>();
        for (float x : winRate)
            entries.add(new RadarEntry(x));

        radarChart.animate();
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);
        radarChart.animateXY(1400, 1400, Easing.EasingOption.EaseOutSine, Easing.EasingOption.EaseOutSine);

        RadarDataSet dataSet = new RadarDataSet(entries, "axx");
        dataSet.setColor(Color.CYAN);
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);

        RadarData radarData = new RadarData(dataSet);

        radarChart.setData(radarData);

        radarChart.invalidate();

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return roleName[(int) value];
            }
        });

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setLabelCount(entries.size(), false);
        yAxis.setDrawLabels(false);

        radarChart.getDescription().setText("Tỉ lệ thắng");
        radarChart.getLegend().setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_exit, R.id.ll_favorite_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
                break;
            case R.id.ll_favorite_role:
                rlSmallWindow.setVisibility(View.VISIBLE);
                break;
        }
    }
}
