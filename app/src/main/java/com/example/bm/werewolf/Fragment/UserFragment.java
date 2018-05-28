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

import com.example.bm.werewolf.Adapter.CoverAdapter;
import com.example.bm.werewolf.Adapter.FavoriteRoleAdapter;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
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

    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.gv_small_window)
    GridView gvSmallWindow;
    @BindView(R.id.rl_small_window)
    RelativeLayout rlSmallWindow;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
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
    @BindView(R.id.tv_small_window)
    TextView tvSmallWindow;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);

        Transformation transformation = new CropCircleTransformation();

        Picasso.get()
                .load("https://graph.facebook.com/" + UserDatabase.facebookID + "/picture?type=large")
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .into(ivAva);

        tvWin.setText(String.valueOf(UserDatabase.getInstance().userModel.win));
        tvLose.setText(String.valueOf(UserDatabase.getInstance().userModel.lose));
        ivCover.setImageResource(Constant.imageCover[UserDatabase.getInstance().userModel.cover]);
        tvName.setText(UserDatabase.getInstance().userModel.name);
        if (UserDatabase.getInstance().userModel.favoriteRole == 0)
            ivFavoriteRole.setVisibility(View.GONE);
        else ivFavoriteRole.setVisibility(View.VISIBLE);
        ivFavoriteRole.setImageResource(Constant.imageRole[UserDatabase.getInstance().userModel.favoriteRole]);
        tvFavoriteRole.setText(Constant.nameRole[UserDatabase.getInstance().userModel.favoriteRole]);

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
        radarChart.setExtraOffsets(-100, -100, -100, -100);
        radarChart.setHighlightPerTapEnabled(false);

        return view;
    }

    public void initFavoriteRole() {
        tvSmallWindow.setText("Nhân vật yêu thích");
        final FavoriteRoleAdapter favoriteRoleAdapter = new FavoriteRoleAdapter();
        gvSmallWindow.setAdapter(favoriteRoleAdapter);
        gvSmallWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rlSmallWindow.setVisibility(View.GONE);
                if (position == 0) ivFavoriteRole.setVisibility(View.GONE);
                else ivFavoriteRole.setVisibility(View.VISIBLE);
                ivFavoriteRole.setImageResource(Constant.imageRole[position]);
                tvFavoriteRole.setText(Constant.nameRole[position]);

                UserDatabase.getInstance().userModel.favoriteRole = position;
                UserDatabase.getInstance().updateUser();
            }
        });
    }

    public void initCover() {
        tvSmallWindow.setText("Chọn ảnh bìa");
        CoverAdapter coverAdapter = new CoverAdapter();
        gvSmallWindow.setAdapter(coverAdapter);
        gvSmallWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rlSmallWindow.setVisibility(View.GONE);
                ivCover.setImageResource(Constant.imageCover[UserDatabase.getInstance().userModel.achievedCover.get(position)]);

                UserDatabase.getInstance().userModel.cover = UserDatabase.getInstance().userModel.achievedCover.get(position);
                UserDatabase.getInstance().updateUser();
            }
        });
    }

    public void initRadarChart() {
        List<RadarEntry> winEntries = new ArrayList<>();
        for (float x : UserDatabase.getInstance().userModel.dataWinRole)
            winEntries.add(new RadarEntry(x));

        List<RadarEntry> totalEntries = new ArrayList<>();
        for (float x : UserDatabase.getInstance().userModel.dataTotalRole)
            totalEntries.add(new RadarEntry(x));

        RadarDataSet winDataSet = new RadarDataSet(winEntries, "số game thắng");
        winDataSet.setColor(Color.CYAN);
        winDataSet.setFillColor(Color.CYAN);
        winDataSet.setDrawFilled(true);
        winDataSet.setDrawValues(true);
        winDataSet.setValueTextColor(Color.LTGRAY);

        RadarDataSet totalDataSet = new RadarDataSet(totalEntries, "số game đã chơi");
        totalDataSet.setColor(Color.parseColor("#B252FF"));
        totalDataSet.setFillColor(Color.parseColor("#B252FF"));
        totalDataSet.setDrawFilled(true);
        totalDataSet.setDrawValues(true);
        totalDataSet.setValueTextColor(Color.LTGRAY);

        List<IRadarDataSet> radarDataSetList = new ArrayList<>();
        radarDataSetList.add(totalDataSet);
        radarDataSetList.add(winDataSet);

        RadarData radarData = new RadarData(radarDataSetList);
        radarData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(Math.round(value));
            }
        });

        radarChart.setData(radarData);
        radarChart.invalidate();

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Constant.nameRole[(int) value + 1];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(winEntries.size(), false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_exit, R.id.ll_favorite_role, R.id.iv_cover})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_exit:
                rlSmallWindow.setVisibility(View.GONE);
                break;
            case R.id.ll_favorite_role:
                initFavoriteRole();
                rlSmallWindow.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_cover:
                initCover();
                rlSmallWindow.setVisibility(View.VISIBLE);
                break;
        }
    }
}
