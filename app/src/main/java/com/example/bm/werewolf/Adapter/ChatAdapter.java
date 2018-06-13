package com.example.bm.werewolf.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bùm on 5/29/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    public static List<String> chatData = new ArrayList<>();

    public static ValueEventListener valueEventListener;

    public ChatAdapter(String roomID, final LinearLayoutManager linearLayoutManager) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("chat").child(roomID);

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String chat = snapshot.getValue(String.class);
                    chatData.add(chat);
                }
                notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(chatData.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setData(chatData.get(position));
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ChatViewHolder";
        @BindView(R.id.tv_chat)
        TextView tvChat;

        View view;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(String chat) {
            tvChat.setText(chat);
            if (chat.charAt(0) != '[')
                tvChat.setTextColor(Color.parseColor("#FFC109"));
            else
                tvChat.setTextColor(Color.parseColor("#F0EFF5"));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (valueEventListener != null)
            FirebaseDatabase.getInstance().getReference("chat")
                    .child(Constant.roomID).removeEventListener(valueEventListener);
        super.finalize();
    }
}
