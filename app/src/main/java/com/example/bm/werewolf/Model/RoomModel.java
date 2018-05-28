package com.example.bm.werewolf.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class RoomModel {
    public String roomName;
    public String roomPassword;
    public boolean isPasswordProtected;
    public List<Integer> players;
    public boolean gameInProgress;

    public RoomModel() {

    }

    public RoomModel(String roomName, String roomPassword, boolean isPasswordProtected, List<Integer> players, boolean gameInProgress) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.isPasswordProtected = isPasswordProtected;
        this.players = players;
        this.gameInProgress = gameInProgress;
    }

    @Override
    public String toString() {
        return "RoomModel{\n" +
                "roomName='" + roomName + '\n' +
                "roomPassword='" + roomPassword + '\n' +
                "isPasswordProtected=" + isPasswordProtected + '\n' +
                "players=" + players + '\n' +
                "gameInProgress=" + gameInProgress + '\n' +
                '}';
    }
}
