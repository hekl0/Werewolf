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

    public List<Achievement> achievementList;
    public List<DataRole> dataRoleList;
    public List<String> friendList;

    public UserModel() {
    }

    public UserModel(int win, int lose, boolean isOnline, int favoriteRole, String name,
                     List<Achievement> achievementList,
                     List<DataRole> dataRoleList,
                     List<String> friendList) {
        this.win = win;
        this.lose = lose;
        this.isOnline = isOnline;
        this.favoriteRole = favoriteRole;
        this.name = name;
        this.achievementList = achievementList;
        this.dataRoleList = dataRoleList;
        this.friendList = friendList;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "win=" + win +
                ", lose=" + lose +
                ", isOnline=" + isOnline +
                ", favoriteRole=" + favoriteRole +
                ", name='" + name + '\'' +
                ", achievementList=" + achievementList +
                ", dataRoleList=" + dataRoleList +
                ", friendList=" + friendList +
                '}';
    }

    public class Achievement {
        public String name;
        public int currentPoint;
        public int totalPoint;
    }

    public class DataRole {
        public String role;
        public int win;
        public int totalGame;
    }
}
