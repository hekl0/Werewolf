package com.example.bm.werewolf.Database;

public class AchievementModel {
    public int id;
    public String name;
    public String image;
    public int progress;
    public int total;

    public AchievementModel(int id, String name, String image, int progress, int total) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.progress = progress;
        this.total = total;
    }
}
