package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Fragment.DayFragment;
import com.example.bm.werewolf.R;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DayAdapter extends BaseAdapter {
    public static List<PlayerModel> playerModelList;
    public static int pick;

    public DayAdapter(List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
        if (this.playerModelList == null)
            this.playerModelList = new ArrayList<>();

        pick = -1;
    }

    @Override
    public int getCount() {
        return playerModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.player_item, parent, false);

        TextView tvNum = convertView.findViewById(R.id.tv_number);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        ImageView ivAva = convertView.findViewById(R.id.iv_ava);
        final ImageView ivCheck = convertView.findViewById(R.id.iv_check);
        final ImageView ivMark = convertView.findViewById(R.id.iv_mark);

        tvName.setText(playerModelList.get(pos).name);
        tvName.setSelected(true);

        final Transformation transformation = new CropCircleTransformation();
        if (playerModelList.get(pos).alive)
            Picasso.get()
                    .load("https://graph.facebook.com/" + playerModelList.get(pos).id + "/picture?type=large")
                    .placeholder(R.drawable.progress_animation)
                    .transform(transformation)
                    .into(ivAva);
        else
            Picasso.get()
                    .load(R.drawable.die)
                    .placeholder(R.drawable.progress_animation)
                    .transform(transformation)
                    .into(ivAva);

        tvNum.setText((pos + 1) + "");
        Picasso.get()
                .load(Constant.imageRole[playerModelList.get(pos).mark])
                .transform(transformation)
                .into(ivMark);

        ivMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DayFragment.rlSmallWindow == null) return;
                DayFragment.rlSmallWindow.setVisibility(View.VISIBLE);
                DayFragment.gvSmallWindow.setAdapter(new FavoriteRoleAdapter());
                DayFragment.gvSmallWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DayFragment.rlSmallWindow.setVisibility(View.GONE);
                        playerModelList.get(pos).mark = position;
                        Picasso.get()
                                .load(Constant.imageRole[playerModelList.get(pos).mark])
                                .transform(transformation)
                                .into(ivMark);
                    }
                });
            }
        });
        if (pick == pos) ivCheck.setVisibility(View.VISIBLE);
        else ivCheck.setVisibility(View.GONE);

        ivAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerModelList.get(pos).alive == true) {
                    pick = pos;
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }
}
