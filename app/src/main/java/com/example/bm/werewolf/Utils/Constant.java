package com.example.bm.werewolf.Utils;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;

import java.util.List;

/**
 * Created by bùm on 24/05/2018.
 */

public class Constant {
    public static int[] imageRole = new int[]{R.mipmap.card_back, R.mipmap.bao_ve, R.mipmap.ma_soi, R.mipmap.dan_lang, R.mipmap.tho_san, R.mipmap.tien_tri, R.mipmap.phu_thuy};
    public static String[] nameRole = new String[]{"Không có", "Bao vê", "Ma sói", "Dân làng", "Thợ săn", "Tiên tri", "Phù thuy"};

    public static int NONE = 0;
    public static int BAO_VE = 1;
    public static int MA_SOI = 2;
    public static int DAN_LANG = 3;
    public static int THO_SAN = 4;
    public static int TIEN_TRI = 5;
    public static int PHU_THUY = 6;

    public static int[] imageCover = new int[]{R.mipmap.achieve_default, R.mipmap.achieve_gia_lang, R.mipmap.achieve_vua_soi};
    public static String[] nameCover = new String[]{"Mặc định", "Già làng", "Ma sói"};

    public static int DEFAULT = 0;
    public static int GIALANG = 1;
    public static int MASOI = 2;

    public static String roomID;
    public static boolean isHost;
    public static int totalPlayer;
    public static List<String> listPlayer;
    public static List<PlayerModel> listPlayerModel;
    public static int myRole;
}
