package com.example.bm.werewolf.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.bm.werewolf.Adapter.AchievementELVAdapter;
import com.example.bm.werewolf.Database.AchievementItemModel;
import com.example.bm.werewolf.Database.AchievementModel;
import com.example.bm.werewolf.Database.DatabaseManager;
import com.example.bm.werewolf.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InProgressFragment extends Fragment {

    private static final String TAG = "InProgressFragment";
    Context context;
    @BindView(R.id.elv_achievement)
    ExpandableListView elvAchievement;
    Unbinder unbinder;

    public InProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
//
//        Log.d(TAG, "onCreateView: " + achievementModels);
        List<AchievementModel> achievementModels1 = DatabaseManager.getInstance(context).getListAchievement();
        List<AchievementModel> achievementModels = new ArrayList<>();
        for (AchievementModel a: achievementModels1) {
            if (a.progress != a.total)
                achievementModels.add(a);
        }

        List<AchievementItemModel> achievementItemModels1 = DatabaseManager.getInstance(context).getListAchievementItem();
        List<AchievementItemModel> achievementItemModels = new ArrayList<>();
        for (AchievementItemModel a: achievementItemModels1) {
            if (achievementModels1.get(a.group).progress != achievementModels1.get(a.group).total)
                achievementItemModels.add(a);
        }

        AchievementELVAdapter achievementELVAdapter = new AchievementELVAdapter(achievementModels, achievementItemModels, context);

        elvAchievement.setAdapter(achievementELVAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
