package com.example.bm.werewolf.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoleListViewAdapter extends BaseAdapter {
    List<Integer> roles;

    public RoleListViewAdapter(List<Integer> roles) {
        this.roles = roles;
    }

    @Override
    public int getCount() {
        return roles.size();
    }

    @Override
    public Object getItem(int position) {
        return roles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.role_info_item, parent, false);
        ImageView ivRole = convertView.findViewById(R.id.iv_role);
        TextView tvRole = convertView.findViewById(R.id.tv_role);
        Picasso.get().load(Constant.imageRole[roles.get(position) + 1]).into(ivRole);
        tvRole.setText(Constant.roleRule[roles.get(position)]);

        return convertView;
    }
}
