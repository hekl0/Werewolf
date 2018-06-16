package com.example.bm.werewolf.Database;

import android.content.Context;
import android.util.Log;

import com.example.bm.werewolf.Model.AchievementItemModel;
import com.example.bm.werewolf.Model.AchievementModel;
import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.Utils.Constant;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
   List<AchievementModel> achievementModels = new ArrayList<>();
   List<AchievementItemModel> achievementItemModels = new ArrayList<>();
    List<Integer> total = new ArrayList<>();
    List<Integer> win = new ArrayList<>();

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
       List<AchievementItemModel> achievementItemModels = getListAchievementItem();
       int demsoi = 0;
       for (int i = 0; i <= 1; i++)
       {
           AchievementItemModel a = achievementItemModels.get(i);
           if (a.progress == a.total) demsoi++;
       }
       int demnguoi = 0;
       for (int i = 2; i <= 3; i++)
       {
           AchievementItemModel a = achievementItemModels.get(i);
           if (a.progress == a.total) demnguoi++;
       }
       AchievementModel achievementModel = new AchievementModel(0, "Vua Sói", null, demsoi, 2);
       achievementModels.add(achievementModel);
       achievementModel = new AchievementModel(1, "Già Làng", null, demnguoi, 2);
       achievementModels.add(achievementModel);
       return achievementModels;
   }

   public void getUserData() {
        UserDatabase.getInstance().updateUser();
       win = UserDatabase.getInstance().userData.dataWinRole;
       total = UserDatabase.getInstance().userData.dataTotalRole;
   }

   public List<AchievementItemModel> getListAchievementItem() {
       List<AchievementItemModel> achievementItemModels = new ArrayList<>();
       getUserData();

       AchievementItemModel achievementItemModel = new AchievementItemModel(0, "Làm sói thắng 10 lần", 0, 10, min(10,win.get(1)));
       achievementItemModels.add(achievementItemModel);

       achievementItemModel = new AchievementItemModel(1, "Làm sói thua 10 lần", 0,10,min(10,total.get(1)-win.get(1)));
       achievementItemModels.add(achievementItemModel);

       int tongnguoi = total.get(0);
       int tongnguoiwin = win.get(0);
       for (int i = 2; i <= 5; i++)
       {
           tongnguoi += total.get(i);
           tongnguoiwin += win.get(i);
       }
       achievementItemModel = new AchievementItemModel(2,"Làm người thắng 10 lần", 1, 10, min(10,tongnguoiwin));
       achievementItemModels.add(achievementItemModel);

       achievementItemModel = new AchievementItemModel(3,"Làm người thua 10 lần", 1, 10, min(10,tongnguoi-tongnguoiwin));
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
