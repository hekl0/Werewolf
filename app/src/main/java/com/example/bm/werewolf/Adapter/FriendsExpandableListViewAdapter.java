package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Fragment.FriendsFragment;
import com.example.bm.werewolf.Model.UserModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.view.View.GONE;

public class FriendsExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "FriendsExpandableListVi";

    Context context;
    String[] groupName = new String[]{"Bạn bè", "Chơi cùng gần đây"};
    List<String> friend;
    List<String> recentPlayWith;

    HashMap<String, String> nameFromID = new HashMap<>();
    Map<String, Boolean> onlineStatusFromID = new HashMap<>();
    HashMap<String, String> roomFromID = new HashMap<>();

    public FriendsExpandableListViewAdapter(Context context) {
        this.context = context;
        this.friend = UserDatabase.getInstance().userData.friendList;
        this.recentPlayWith = UserDatabase.getInstance().userData.recentPlayWith;

        nameFromID = new HashMap<>();
        onlineStatusFromID = new HashMap<>();
        roomFromID = new HashMap<>();
    }

    @Override
    public int getGroupCount() {
        return groupName.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0)
            return friend.size();
        else
            return recentPlayWith.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupName[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0)
            return friend.get(childPosition);
        else
            return recentPlayWith.get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_list_friends, parent, false);
        ImageView ivArrow = convertView.findViewById(R.id.iv_arrow);
        TextView tvGroupName = convertView.findViewById(R.id.tv_group_name);
        ImageView ivIcon = convertView.findViewById(R.id.iv_icon);

        tvGroupName.setText(groupName[groupPosition]);
        if (isExpanded) {
            ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
        } else {
            ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
        }

        if(groupPosition == 0) ivIcon.setImageResource(R.drawable.icon_friend);
        else ivIcon.setImageResource(R.drawable.icon_recent);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_list_player, parent, false);

        final TextView tvPlayerName = convertView.findViewById(R.id.tv_player);
        final ImageView ivOnline = convertView.findViewById(R.id.iv_online);
        ImageView ivAva = convertView.findViewById(R.id.iv_ava);
        final TextView tvCurrentRoom = convertView.findViewById(R.id.tv_current_room);

        ivOnline.setVisibility(GONE);
        tvCurrentRoom.setVisibility(GONE);

        final String userID = (String) getChild(groupPosition, childPosition);

        if (nameFromID.containsKey(userID)) {
            tvPlayerName.setText(nameFromID.get(userID));
            if (onlineStatusFromID.get(userID) && roomFromID.get(userID) == null) {
                ivOnline.setVisibility(View.VISIBLE);
                ivOnline.setImageResource(R.drawable.ic_online_dot);
            } else if (onlineStatusFromID.get(userID) && roomFromID.get(userID) != null) {
                ivOnline.setImageResource(R.drawable.ic_busy_dot);
                ivOnline.setVisibility(View.VISIBLE);
                tvCurrentRoom.setText("Đang ở phòng " + roomFromID.get(userID));
                tvCurrentRoom.setVisibility(View.VISIBLE);
            }
        } else {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("User list").child(userID);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);

                    tvPlayerName.setText(userModel.name);
                    if (userModel.isOnline && userModel.currentRoom == null) {
                        ivOnline.setVisibility(View.VISIBLE);
                        ivOnline.setImageResource(R.drawable.ic_online_dot);
                    } else if (userModel.isOnline && userModel.currentRoom != null) {
                        ivOnline.setImageResource(R.drawable.ic_busy_dot);
                        ivOnline.setVisibility(View.VISIBLE);
                        tvCurrentRoom.setText("Đang ở phòng " + userModel.currentRoom);
                        tvCurrentRoom.setVisibility(View.VISIBLE);
                    }

                    nameFromID.put(userID, userModel.name);
                    onlineStatusFromID.put(userID, userModel.isOnline);
                    roomFromID.put(userID, userModel.currentRoom);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Transformation transformation = new CropCircleTransformation();
        Picasso.get()
                .load("https://graph.facebook.com/" + userID + "/picture?type=large")
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .into(ivAva);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsFragment.openSmallWindow(userID);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        friend = UserDatabase.getInstance().userData.friendList;
        super.notifyDataSetChanged();
    }
}
