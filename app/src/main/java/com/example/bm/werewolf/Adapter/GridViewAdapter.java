package com.example.bm.werewolf.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;

/**
 * Created by bùm on 21/05/2018.
 */

public class GridViewAdapter extends BaseAdapter {
    private static final String TAG = "GridViewAdapter";

    public int[] image = new int[]{R.mipmap.ic_launcher, R.mipmap.bao_ve, R.mipmap.ma_soi, R.mipmap.dan_lang, R.mipmap.tho_san, R.mipmap.tien_tri};
    public String[] name = new String[]{"None", "Bảo vệ", "Ma sói", "Dân làng", "Thợ săn", "Tiên tri"};

    @Override
    public int getCount() {
        return name.length;
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
        convertView = layoutInflater.inflate(R.layout.favorite_role_model, parent, false);

        ImageView iv = convertView.findViewById(R.id.iv);
        TextView tv = convertView.findViewById(R.id.tv);

        iv.setImageResource(image[position]);
        tv.setText(name[position]);

        return convertView;
    }
}
