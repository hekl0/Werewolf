package com.example.bm.werewolf.Model;

public class PlayerModel {
    public String id;
    public int mark;
    public int role;
    public boolean alive;
    public String name;

    public PlayerModel() {}

    public PlayerModel(String id, int mark, int role, boolean alive, String name) {
        this.id = id;
        this.mark = mark;
        this.role = role;
        this.alive = alive;
        this.name = name;
    }
}
