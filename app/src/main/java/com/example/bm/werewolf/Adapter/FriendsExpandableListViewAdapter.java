package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;

import java.util.List;

public class FriendsExpandableListViewAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> groupName;
    List<UserModel> childUser;

    public FriendsExpandableListViewAdapter(Context context, List<String> groupName, List<UserModel> childUser) {
        this.context = context;
        this.groupName = groupName;
        this.childUser = childUser;
    }

    @Override
    public int getGroupCount() {
        return groupName.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childUser.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childUser.get(childPosition);
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_list_friends, parent, false);
        ImageView ivArrow = convertView.findViewById(R.id.iv_arrow);

        if (isExpanded) {
            ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
        } else {
            ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_list_player, parent, false);

        UserModel user = (UserModel) getChild(0, childPosition);

        TextView tvPlayerName = convertView.findViewById(R.id.tv_player);
        ImageView ivOnline = convertView.findViewById(R.id.iv_online);

        tvPlayerName.setText(user.name);
        ivOnline.setImageResource((user.isOnline) ? R.drawable.ic_grid_world_green : R.drawable.ic_grid_world_red);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
