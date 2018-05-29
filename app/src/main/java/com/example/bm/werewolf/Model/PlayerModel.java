package com.example.bm.werewolf.Model;

public class PlayerModel {
    public int id;
    public int mark;
    public int role;
    public boolean alive;

    public PlayerModel(int id, int mark, int role, boolean alive) {
        this.id = id;
        this.mark = mark;
        this.role = role;
        this.alive = alive;
    }
}
