package com.example.bm.werewolf.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;

/**
 * Created by bùm on 21/05/2018.
 */

public class FavoriteRoleAdapter extends BaseAdapter {
    private static final String TAG = "FavoriteRoleAdapter";

    @Override
    public int getCount() {
        return Constant.nameRole.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.item_favorite_role, parent, false);

        ImageView iv = convertView.findViewById(R.id.iv);
        TextView tv = convertView.findViewById(R.id.tv);

        iv.setImageResource(Constant.imageRole[position]);
        tv.setText(Constant.nameRole[position]);

        return convertView;
    }
}
