package com.example.bm.werewolf.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bm.werewolf.Adapter.ViewPagerAdapter;
import com.example.bm.werewolf.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_screen_name)
    TextView tvScreenName;
    @BindView(R.id.iv_add_friend)
    ImageView ivAddFriend;
    @BindView(R.id.iv_inbox)
    ImageView ivInbox;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigation.getMenu().getItem(0).getIcon().setAlpha(100);
        bottomNavigation.getMenu().getItem(1).getIcon().setAlpha(255);
        bottomNavigation.getMenu().getItem(2).getIcon().setAlpha(100);
        bottomNavigation.getMenu().getItem(3).getIcon().setAlpha(100);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setSelectedItemId(R.id.item_play);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigation.getMenu().getItem(0).getIcon().setAlpha(100);
                bottomNavigation.getMenu().getItem(1).getIcon().setAlpha(100);
                bottomNavigation.getMenu().getItem(2).getIcon().setAlpha(100);
                bottomNavigation.getMenu().getItem(3).getIcon().setAlpha(100);
                switch (item.getItemId()) {
                    case R.id.item_friends:
                        bottomNavigation.getMenu().getItem(0).getIcon().setAlpha(255);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_play:
                        bottomNavigation.getMenu().getItem(1).getIcon().setAlpha(255);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_achievement:
                        bottomNavigation.getMenu().getItem(2).getIcon().setAlpha(255);
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.item_user:
                        bottomNavigation.getMenu().getItem(3).getIcon().setAlpha(255);
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                switch (position) {
                    case 0:
                        tvScreenName.setText("Friends list");
                        break;
                    case 1:
                        tvScreenName.setText("Lobby");
                        break;
                    case 2:
                        tvScreenName.setText("Achievement");
                        break;
                    case 3:
                        tvScreenName.setText("Your profile");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(1);
    }
}
