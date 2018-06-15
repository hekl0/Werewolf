package com.example.bm.werewolf.Utils;

import com.example.bm.werewolf.Model.PlayerModel;
import com.example.bm.werewolf.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bùm on 24/05/2018.
 */

public class Constant {
    public static int[] imageRole = new int[]{R.mipmap.card_back, R.mipmap.bao_ve, R.mipmap.ma_soi, R.mipmap.dan_lang, R.mipmap.tho_san, R.mipmap.tien_tri, R.mipmap.phu_thuy};
    public static String[] nameRole = new String[]{"Không có", "Bao vê", "Ma sói", "Dân làng", "Thợ săn", "Tiên tri", "Phù thuy"};
    public static String[] roleRule =
            new String[]{"Mỗi đêm Bảo Vệ được phép bảo vệ một người, người đó sẽ không bị ma sói cắn chết vào đêm đó. Nhưng không được bảo vệ một người 2 trong đêm liên tục. Bảo vệ được quyền bảo vệ chính mình. Nhưng nếu trúng bình giết của phù thuỷ hoặc bị thợ săn bắn trúng, người được bảo vệ vẫn chết.",
                            "Mỗi đêm thức dậy và cắn một người. Người đó sẽ chết vào sáng hôm tiếp theo.",
                            "Chức năng quyền lực nhất trong trò chơi, tìm ra ai là sói không cần thức dậy mỗi đêm.",
                            "Khi thợ săn chết, dù là dưới bất kì hình thức nào cũng có thể chọn một người chết theo",
                            "Mỗi đêm thức dậy tiên tri chọn người để soi xem có phải là sói hay không.",
                            "Phù Thủy sẻ được sở hữu 2 bình chức năng (1 bình cứu người và 1 bình giết người). Phù Thủy chỉ được sử dụng mỗi bình 1 lần trong cả ván chơi."};

    public static int NONE = -1;
    public static int BAO_VE = 0;
    public static int MA_SOI = 1;
    public static int DAN_LANG = 2;
    public static int THO_SAN = 3;
    public static int TIEN_TRI = 4;
    public static int PHU_THUY = 5;

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
    public static boolean[] availableRole = new boolean[]{false, false, false, false, false, false};
}
