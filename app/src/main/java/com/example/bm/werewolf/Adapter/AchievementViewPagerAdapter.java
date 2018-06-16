package com.example.bm.werewolf.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bm.werewolf.Fragment.AchievedFragment;
import com.example.bm.werewolf.Fragment.InProgressFragment;

public class AchievementViewPagerAdapter extends FragmentStatePagerAdapter {
    public AchievementViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new InProgressFragment();
            case 1: return new AchievedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
