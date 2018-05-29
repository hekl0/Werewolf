package com.example.bm.werewolf.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.LobbyAdapter;
import com.example.bm.werewolf.Model.RoomModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {

    private static final String TAG = "PlayFragment";

    @BindView(R.id.bt_create_room)
    Button btCreateRoom;
    @BindView(R.id.bt_find_room)
    Button btFindRoom;
    Unbinder unbinder;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    HashMap<Integer, RoomModel> roomMap = new HashMap<>();

    LobbyAdapter adapter;
    Context context;
    @BindView(R.id.rv_rooms)
    RecyclerView rvRooms;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        unbinder = ButterKnife.bind(this, view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("rooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int roomID = Integer.parseInt(snapshot.getKey());
                    RoomModel model = snapshot.getValue(RoomModel.class);
                    roomMap.put(roomID, model);
                }
                adapter = new LobbyAdapter(roomMap, context);
                rvRooms.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        context = getContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        rvRooms.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_create_room, R.id.bt_find_room})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_create_room:
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View mView = inflater.inflate(R.layout.create_room_dialog_box, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(mView);

                final EditText etRoomName = mView.findViewById(R.id.et_room_name);
                final EditText etRoomPass = mView.findViewById(R.id.et_room_password);
                final CheckBox checkBox = mView.findViewById(R.id.checkbox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        etRoomPass.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });

                builder.setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = 0;
                                String roomName = etRoomName.getText().toString();
                                String roomPass = etRoomPass.getText().toString();
                                boolean isPasswordProtected = checkBox.isChecked();
                                boolean gameInProgress = false;
                                final String currentPlayerID = UserDatabase.facebookID;

                                while (roomMap.containsKey(id)) {
                                    id++;
                                }
                                RoomModel model = new RoomModel(
                                        roomName,
                                        roomPass,
                                        isPasswordProtected,
                                        new ArrayList<String>() {
                                            {
                                                add(currentPlayerID);
                                            }
                                        },
                                        UserDatabase.facebookID,
                                        gameInProgress);
                                databaseReference.child(String.valueOf(id)).setValue(model);
                                //adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7dffffff")));
                alertDialog.show();
                break;
            case R.id.bt_find_room:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater dialogInflater = LayoutInflater.from(context);
                final View dialogView = dialogInflater.inflate(R.layout.dialog_with_edittext, null);
                dialogBuilder.setView(dialogView);

                final EditText etRoomID = dialogView.findViewById(R.id.edittext);
                etRoomID.setInputType(InputType.TYPE_CLASS_NUMBER);

                dialogBuilder.setTitle("Tìm phòng");
                dialogBuilder.setMessage("Nhập ID phòng");
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (etRoomID.getText().toString().isEmpty()) {
                            etRoomID.setError("ID phòng không thể trống");
                            return;
                        }
                        int roomID = Integer.parseInt(etRoomID.getText().toString());
                        if (checkRoomID(roomID)) {
                            //found the room
                            Toast.makeText(context, "Thấy Phòng", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, WaitingRoomActivity.class);
                            intent.putExtra("roomID", roomID);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, "Không thấy phòng", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
                break;
        }
    }

    boolean checkRoomID(int ID) {
        for (int roomID : roomMap.keySet()) {
            if (roomID == ID)
                return true;
        }
        return false;
    }
}
