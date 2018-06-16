package com.example.bm.werewolf.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Model.RoomModel;
import com.example.bm.werewolf.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.LobbyViewHolder> {
    private static final String TAG = "LobbyAdapter";

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
        return roomList.size();
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

        public void setData(final RoomModel roomModel) {
            if (roomModel.isPasswordProtected)
                ivLock.setVisibility(View.VISIBLE);
            if (roomModel.players == null)
                roomModel.players = new ArrayList<>();
            tvRoomId.setText(String.format("ID: %d", getKeyByValue(roomMap, roomModel)));
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
                    //chech is room playing
                    if (roomModel.gameInProgress) {
                        Toast.makeText(context, "Phòng đang chơi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //do something a room is choosen
                    Toast.makeText(context, "Room choosen", Toast.LENGTH_SHORT).show();
                    if (roomModel.isPasswordProtected)
                        showPasswordDialog(roomModel.roomName, roomModel.roomPassword);
                    else {
                        int roomID = Integer.parseInt(tvRoomId.getText().toString().split(" ")[1]);
                        Intent intent = new Intent(context, WaitingRoomActivity.class);
                        intent.putExtra("roomID", roomID);
                        intent.putExtra("isHost", false);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public int getKeyByValue(Map<Integer, RoomModel> map, RoomModel value) {
            for (Integer key : map.keySet()) {
                if (map.get(key).equals(value))
                    return key;
            }
            return -1;
        }

        public void showPasswordDialog(String roomName, final String roomPass) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.dialog_with_edittext, null);
            dialogBuilder.setView(dialogView);

            final EditText etPass = dialogView.findViewById(R.id.edittext);
            etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            dialogBuilder.setTitle(roomName);
            dialogBuilder.setMessage("Nhập mật khẩu phòng");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (etPass.getText().toString().equals(roomPass)) {
                        Toast.makeText(context, "Vào phòng", Toast.LENGTH_SHORT).show();

                        int roomID = Integer.parseInt(tvRoomId.getText().toString().split(" ")[1]);
                        Intent intent = new Intent(context, WaitingRoomActivity.class);
                        intent.putExtra("roomID", roomID);
                        intent.putExtra("isHost", false);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog ad = dialogBuilder.create();
            ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7dffffff")));
            ad.show();
        }
    }
}
