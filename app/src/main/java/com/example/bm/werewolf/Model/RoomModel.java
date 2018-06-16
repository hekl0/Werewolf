package com.example.bm.werewolf.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class RoomModel {
    public String roomName;
    public String roomPassword;
    public boolean isPasswordProtected;
    public List<String> players;
    public boolean gameInProgress;
    public String roomMasterID;

    public RoomModel() {

    }

    public RoomModel(String roomName, String roomPassword, boolean isPasswordProtected, List<String> players, String roomMasterID, boolean gameInProgress) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.isPasswordProtected = isPasswordProtected;
        this.players = players;
        this.roomMasterID = roomMasterID;
        this.gameInProgress = gameInProgress;
    }

    @Override
    public String toString() {
        return "RoomModel{" +
                "roomName=" + roomName + '\n' +
                "roomPassword=" + roomPassword + '\n' +
                "isPasswordProtected=" + isPasswordProtected + "\n" +
                "players=" + players +
                "gameInProgress=" + gameInProgress +
                "roomMasterID=" + roomMasterID +
                '}';
    }
}
