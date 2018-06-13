package com.example.bm.werewolf.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DyingAdapter extends BaseAdapter {
    List<String> dyingList;

    public DyingAdapter(List<String> dyingList) {
        this.dyingList = dyingList;
    }

    @Override
    public int getCount() {
        return dyingList.size();
    }

    @Override
    public Object getItem(int position) {
        return dyingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.dying_player_item, parent, false);
        ImageView ivAva = convertView.findViewById(R.id.iv_ava);
        TextView tvName = convertView.findViewById(R.id.tv_name);

        Transformation transformation = new CropCircleTransformation();
        Picasso.get()
                .load("https://graph.facebook.com/" + dyingList.get(position) + "/picture?type=large")
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .into(ivAva);
        for (PlayerModel i: Constant.listPlayerModel)
            if (i.id.equals(dyingList.get(position)))
            {
                tvName.setText(i.name);
                break;
            }

        return convertView;
    }
}
