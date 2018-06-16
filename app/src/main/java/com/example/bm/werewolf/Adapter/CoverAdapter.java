package com.example.bm.werewolf.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;

import java.util.List;

/**
 * Created by bùm on 27/05/2018.
 */

public class CoverAdapter extends BaseAdapter {
    private static final String TAG = "CoverAdapter";

    @Override
    public int getCount() {
        return UserDatabase.getInstance().userData.achievedCover.size();
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
        convertView = layoutInflater.inflate(R.layout.item_cover, parent, false);

        ImageView iv = convertView.findViewById(R.id.iv);
        TextView tv = convertView.findViewById(R.id.tv);

        iv.setBackgroundResource(Constant.imageCover[UserDatabase.getInstance().userData.achievedCover.get(position)]);
        tv.setText(Constant.nameCover[UserDatabase.getInstance().userData.achievedCover.get(position)]);

        return convertView;
    }
}
