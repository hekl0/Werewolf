package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.bm.werewolf.Activity.PlayActivity;
import com.example.bm.werewolf.Activity.WaitingRoomActivity;
import com.example.bm.werewolf.Adapter.DayAdapter;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Utils.Constant;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndGameFragment extends Fragment {
    View view;

    int total = 0;
    int wolf = 0;

    public EndGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_end_game, container, false);

        //set online data game end
        WaitingRoomActivity.isGameInProgress = false;
        if (Constant.isHost)
            FirebaseDatabase.getInstance().getReference("rooms").child(Constant.roomID)
                    .child("gameInProgress").setValue(false);

        //remove listener
        FirebaseDatabase.getInstance().getReference("Ingame Data").child(Constant.roomID).child("Game Data")
                .removeEventListener(PlayActivity.updateTurnListener);


        //identify win or not
        for (PlayerModel playerModel : Constant.listPlayerModel)
            if (playerModel.alive) {
                total++;
                if (playerModel.role == Constant.MA_SOI) wolf++;
            }

        if (wolf == 0)
            PlayActivity.isWin = (Constant.myRole != Constant.MA_SOI);
        else
            PlayActivity.isWin = (Constant.myRole == Constant.MA_SOI);

        setupUI();

        return view;
    }

    private void setupUI() {
        TextView tvTitle = view.findViewById(R.id.tv_title);
        GridView gvPlayer = view.findViewById(R.id.gv_player);
        TextView tvOk = view.findViewById(R.id.tv_ok);

        if (wolf == 0) tvTitle.setText("Người đã thắng");
        else tvTitle.setText("Sói đã thắng");

        for (PlayerModel playerModel : Constant.listPlayerModel) {
            playerModel.mark = playerModel.role;
            playerModel.alive = true;
        }
        gvPlayer.setAdapter(new DayAdapter(Constant.listPlayerModel, getContext()));

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

}
