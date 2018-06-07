package com.example.bm.werewolf.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

/**
 * Created by bùm on 6/3/2018.
 */

public class RolePickingAdapter extends BaseAdapter {
    Context context;
    public static int[] count = new int[Constant.nameRole.length - 1];

    public RolePickingAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < Constant.nameRole.length - 1; i++)
            count[i] = 0;
    }

    @Override
    public int getCount() {
        return Constant.nameRole.length - 1;
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
        convertView = layoutInflater.inflate(R.layout.item_role_picking, parent, false);

        ImageView ivRole = convertView.findViewById(R.id.iv_role);
        TextView tvRole = convertView.findViewById(R.id.tv_role);
        final TextView tvNumber = convertView.findViewById(R.id.tv_number);
        ImageView ivInc = convertView.findViewById(R.id.iv_inc);
        ImageView ivDec = convertView.findViewById(R.id.iv_dec);

        ivRole.setImageResource(Constant.imageRole[position+1]);
        tvRole.setText(Constant.nameRole[position+1]);

        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("role picking")
                .child("roleList").child(String.valueOf(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNumber.setText(String.valueOf(dataSnapshot.getValue(Integer.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (Constant.isHost) {
            ivInc.setVisibility(View.VISIBLE);
            ivDec.setVisibility(View.VISIBLE);
        } else {
            ivInc.setVisibility(View.GONE);
            ivDec.setVisibility(View.GONE);
        }

        ivInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = 0;
                for (int x : count) total += x;
                if (total == Constant.totalPlayer) {
                    Toast.makeText(context, "Đã đủ số người", Toast.LENGTH_SHORT).show();
                    return;
                }
                count[position] += 1;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("role picking")
                        .child("roleList").child(String.valueOf(position)).setValue(count[position]);
            }
        });

        ivDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[position] == 0) {
                    Toast.makeText(context, "Không thể giảm nữa", Toast.LENGTH_SHORT).show();
                    return;
                }
                count[position] -= 1;
                FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("role picking")
                        .child("roleList").child(String.valueOf(position)).setValue(count[position]);
            }
        });

        return convertView;
    }
}
