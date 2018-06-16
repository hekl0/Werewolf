package com.example.bm.werewolf.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bm.werewolf.Adapter.AchievementViewPagerAdapter;
import com.example.bm.werewolf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementFragment extends Fragment {


    @BindView(R.id.tl_achievement)
    TabLayout tlAchievement;
    @BindView(R.id.vp_achievement)
    ViewPager vpAchievement;
    Unbinder unbinder;

    public AchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);
        unbinder = ButterKnife.bind(this, view);

        tlAchievement.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpAchievement.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        AchievementViewPagerAdapter achievementViewPagerAdapter = new AchievementViewPagerAdapter(getFragmentManager());
        vpAchievement.setAdapter(achievementViewPagerAdapter);
        vpAchievement.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlAchievement));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
