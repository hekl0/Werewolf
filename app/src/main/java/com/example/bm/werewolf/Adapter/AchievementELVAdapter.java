package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bm.werewolf.Database.AchievementItemModel;
import com.example.bm.werewolf.Database.AchievementModel;
import com.example.bm.werewolf.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AchievementELVAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "AchievementELVAdapter";

    List<AchievementModel> achievementModels;
    List<AchievementItemModel> achievementItemModels;
    Context context;

    public AchievementELVAdapter(List<AchievementModel> achievementModels, List<AchievementItemModel> achievementItemModels, Context context) {
        this.achievementModels = achievementModels;
        this.achievementItemModels = achievementItemModels;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return achievementModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return achievementModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return achievementItemModels.get(groupPosition*2+childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.item_list_achievement, parent, false);

        AchievementModel achievementModel = achievementModels.get(groupPosition);
        TextView tvName = convertView.findViewById(R.id.tv_achievement);
        ImageView ivImage = convertView.findViewById(R.id.iv_achievement);
        CircularProgressBar pbAchievement = convertView.findViewById(R.id.pb_achievement);
        pbAchievement.setProgress(0);
        pbAchievement.setProgressWithAnimation((float)achievementModel.progress*100/achievementModel.total,2000);
        Transformation transformation = new CropCircleTransformation();
        if (achievementModel.id == 0) Picasso.get().load(R.mipmap.achieve_vua_soi).transform(transformation).into(ivImage);
        else Picasso.get().load(R.mipmap.achieve_gia_lang).transform(transformation).into(ivImage);

        tvName.setText(achievementModel.name);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.item_list_item_achievement, parent, false);

        AchievementItemModel achievementItemModel = achievementItemModels.get(groupPosition*2+childPosition);
        TextView tvDescription = convertView.findViewById(R.id.tv_item_achievement);
        ProgressBar pbProgress = convertView.findViewById(R.id.pb_item_achievement);
        tvDescription.setText(achievementItemModel.description);
        pbProgress.setMax(achievementItemModel.total);
        pbProgress.setProgress(achievementItemModel.progress);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
