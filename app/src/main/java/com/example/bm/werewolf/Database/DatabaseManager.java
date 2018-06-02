package com.example.bm.werewolf.Database;

import android.content.Context;

import com.example.bm.werewolf.Model.AchievementItemModel;
import com.example.bm.werewolf.Model.AchievementModel;
import com.example.bm.werewolf.Model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
   List<AchievementModel> achievementModels;
   List<AchievementItemModel> achievementItemModels;

   public static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }

    public DatabaseManager (Context context) {}

   public List<AchievementModel> getListAchievement() {
       List<AchievementModel> achievementModels = new ArrayList<>();
       AchievementModel achievementModel = new AchievementModel(0, "Vua Sói", null, 2, 2);
       achievementModels.add(achievementModel);
       achievementModel = new AchievementModel(1, "Già Làng", null, 0, 2);
       achievementModels.add(achievementModel);
       return achievementModels;
   }

   public List<AchievementItemModel> getListAchievementItem() {
       List<AchievementItemModel> achievementItemModels = new ArrayList<>();

       AchievementItemModel achievementItemModel = new AchievementItemModel(0, "Làm sói thắng 10 lần", 0, 10, 10);
       achievementItemModels.add(achievementItemModel);

       achievementItemModel = new AchievementItemModel(1, "Làm sói thua 10 lần", 0,10,10);
       achievementItemModels.add(achievementItemModel);

       achievementItemModel = new AchievementItemModel(2,"Làm người thắng 10 lần", 1, 10, 0);
       achievementItemModels.add(achievementItemModel);

       achievementItemModel = new AchievementItemModel(3,"Làm người thua 10 lần", 1, 10, 0);
       achievementItemModels.add(achievementItemModel);

       return achievementItemModels;
   }

   public List<PlayerModel> getListPlayer() {
        List<PlayerModel> playerModels = new ArrayList<>();

        PlayerModel playerModel = new PlayerModel("2012942565700579", 2, 1, true, "Trung Đào");
        playerModels.add(playerModel);
        playerModel = new PlayerModel("612849455731067", 1, 1, true, "Vũ Thị Thiên Anh");
        playerModels.add(playerModel);

        return playerModels;
   }
}
