package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class GridViewAdapter extends BaseAdapter {
    List<PlayerModel> playerModels = new ArrayList<>();
    Context context;

    public GridViewAdapter(List<PlayerModel> playerModels, Context context) {
        this.playerModels = playerModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playerModels.size();
    }

    @Override
    public Object getItem(int position) {
        return playerModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.player_item, parent, false);

        PlayerModel playerModel = playerModels.get(position);
        TextView tvNum = convertView.findViewById(R.id.tv_number);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        ImageView ivAva = convertView.findViewById(R.id.iv_ava);
        ImageView ivMark = convertView.findViewById(R.id.iv_mark);

        tvName.setText(playerModel.name);
        tvName.setSelected(true);
        Transformation transformation = new CropCircleTransformation();
        Picasso.get()
                .load("https://graph.facebook.com/" + playerModel.id + "/picture?type=large")
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .into(ivAva);
        tvNum.setText(position+1 + "");
        Picasso.get()
                .load(Constant.imageRole[playerModel.mark])
                .transform(transformation)
                .into(ivMark);

        ivMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return convertView;
    }
}
