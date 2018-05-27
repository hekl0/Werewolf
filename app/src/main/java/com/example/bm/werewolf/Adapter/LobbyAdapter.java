package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.models.RoomModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {

    Map<Integer, RoomModel> roomMap;
    List<RoomModel> roomList;
    Context context;

    public LobbyAdapter(Map<Integer, RoomModel> roomMap, Context context) {
        this.roomMap = roomMap;
        this.context = context;
        roomList = new ArrayList<>(roomMap.values());
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_room_lobby, parent, false);
        return new LobbyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        holder.setData(roomList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomMap.size();
    }

    public class LobbyViewHolder extends  RecyclerView.ViewHolder {
        @BindView(R.id.tv_room_id)
        TextView tvRoomId;
        @BindView(R.id.tv_room_name)
        TextView tvRoomName;
        @BindView(R.id.tv_capacity)
        TextView tvCapacity;
        @BindView(R.id.tv_game_status)
        TextView tvGameStatus;
        @BindView(R.id.iv_lock)
        ImageView ivLock;
        @BindView(R.id.rl_room_item)
        RelativeLayout rlRoomItem;

        public LobbyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(RoomModel roomModel) {
            if (roomModel.isPasswordProtected)
                Picasso.get().load(R.drawable.ic_lock_outline_amber_24dp).into(ivLock);
            tvRoomId.setText(String.format("ID: %d", roomMap.get(roomModel)));
            tvCapacity.setText(String.format("%d/10", roomModel.players.size()));
            tvRoomName.setText(roomModel.roomName);
            if (roomModel.gameInProgress) {
                tvGameStatus.setTextColor(Color.parseColor("#E6E600"));
                tvGameStatus.setText("ĐANG CHƠI");
            } else {
                tvGameStatus.setTextColor(Color.parseColor("#2624b5"));
                tvGameStatus.setText("CHƯA CHƠI");
            }
            rlRoomItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something a room is choosen
                    Toast.makeText(context, "Room choosen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
