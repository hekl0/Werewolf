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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
    float[] winGame;
    float[] totalGame;

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

        initRadarChart();

        radarChart.animate();
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.WHITE);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.WHITE);
        radarChart.setWebAlpha(100);
        radarChart.animateXY(1400, 1400, Easing.EasingOption.EaseOutSine, Easing.EasingOption.EaseOutSine);
        radarChart.getLegend().setTextColor(Color.WHITE);

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
        roleName = new String[]{"aaa", "bbb", "ccc", "ddd", "eee"};
        winGame = new float[]{5, 6, 7, 8, 9};
        totalGame = new float[]{10, 9, 12, 15, 11};
        
        List<RadarEntry> winEntries = new ArrayList<>();
        for (float x : winGame)
            winEntries.add(new RadarEntry(x));

        List<RadarEntry> totalEntries = new ArrayList<>();
        for (float x : totalGame)
            totalEntries.add(new RadarEntry(x));

        RadarDataSet winDataSet = new RadarDataSet(winEntries, "số game thắng");
        winDataSet.setColor(Color.CYAN);
        winDataSet.setFillColor(Color.CYAN);
        winDataSet.setDrawFilled(true);
        winDataSet.setDrawValues(false);

        RadarDataSet totalDataSet = new RadarDataSet(totalEntries, "số game đã chơi");
        totalDataSet.setColor(Color.parseColor("#B252FF"));
        totalDataSet.setFillColor(Color.parseColor("#B252FF"));
        totalDataSet.setDrawFilled(true);
        totalDataSet.setDrawValues(false);

        List<IRadarDataSet> radarDataSetList = new ArrayList<>();
        radarDataSetList.add(totalDataSet);
        radarDataSetList.add(winDataSet);

        RadarData radarData = new RadarData(radarDataSetList);

        radarChart.setData(radarData);
        radarChart.invalidate();

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return roleName[(int) value];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(winEntries.size(), false);
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(R.color.white);
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
