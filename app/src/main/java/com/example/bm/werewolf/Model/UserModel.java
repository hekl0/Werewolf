package com.example.bm.werewolf.Model;

import java.util.List;

/**
 * Created by bùm on 22/05/2018.
 */

public class UserModel {
    public int win;
    public int lose;
    public boolean isOnline;
    public int favoriteRole;
    public String name;
    public int cover;

    public List<Integer> achievedCover;
    public List<Achievement> achievementList;
    public List<Integer> dataWinRole;
    public List<Integer> dataTotalRole;
    public List<String> friendList;
    public List<String> recentPlayWith;

    @Override
    public String toString() {
        return "UserModel{" +
                "win=" + win +
                ", lose=" + lose +
                ", isOnline=" + isOnline +
                ", favoriteRole=" + favoriteRole +
                ", name='" + name + '\'' +
                ", achievementList=" + achievementList +
                ", dataWinRole=" + dataWinRole +
                ", dataTotalRole=" + dataTotalRole +
                ", friendList=" + friendList +
                '}';
    }

    public UserModel() {
    }


    public class Achievement {
        public String name;
        public int currentPoint;
        public int totalPoint;
    }
}
