package com.example.bm.werewolf.Database;

public class AchievementItemModel {
    public int id;
    public String description;
    public int group;
    public int total;
    public int progress;

    public AchievementItemModel(int id, String description, int group, int total, int progress) {
        this.id = id;
        this.description = description;
        this.group = group;
        this.total = total;
        this.progress = progress;
    }
}
