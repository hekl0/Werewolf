package com.example.bm.werewolf.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bm.werewolf.Fragment.AchievementFragment;
import com.example.bm.werewolf.Fragment.FriendsFragment;
import com.example.bm.werewolf.Fragment.LobbyFragment;
import com.example.bm.werewolf.Fragment.UserFragment;

/**
 * Created by bùm on 20/05/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FriendsFragment();
            case 1: return new LobbyFragment();
            case 2: return new AchievementFragment();
            case 3: return new UserFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
