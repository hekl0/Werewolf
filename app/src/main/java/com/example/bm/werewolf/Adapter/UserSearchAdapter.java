package com.example.bm.werewolf.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Fragment.FriendsFragment;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.view.View.GONE;

/**
 * Created by bùm on 6/12/2018.
 */

public class UserSearchAdapter extends BaseAdapter {
    List<UserModel> userModelList = new ArrayList<>();

    public UserSearchAdapter(List<UserModel> userModelList) {
        this.userModelList = userModelList;
    }

    @Override
    public int getCount() {
        return userModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.item_list_player, parent, false);

        final TextView tvPlayerName = convertView.findViewById(R.id.tv_player);
        ImageView ivAva = convertView.findViewById(R.id.iv_ava);

        tvPlayerName.setText(userModelList.get(position).name);
        Transformation transformation = new CropCircleTransformation();
        Picasso.get()
                .load("https://graph.facebook.com/" + userModelList.get(position).id + "/picture?type=large")
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .into(ivAva);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsFragment.openSmallWindow(userModelList.get(position).id);
            }
        });

        return convertView;
    }
}
