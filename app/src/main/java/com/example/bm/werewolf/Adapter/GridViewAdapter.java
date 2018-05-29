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

import java.util.ArrayList;
import java.util.List;

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

        return convertView;
    }
}
