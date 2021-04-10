package com.adrianwowk.hypixel.client.formatting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class RankFomatter {

    public static HashMap<String, String> ranks = new HashMap<>();
    public static HashMap<String, String> colors = new HashMap<>();

    static {
        ranks.put("ADMIN", "§c[ADMIN]");
        ranks.put("MODERATOR", "§2[MOD]");
        ranks.put("HELPER", "§9[HELPER]");
        ranks.put("YOUTUBER", "§c[§fYOUTUBER§c]");
        ranks.put("SUPERSTAR", "§%r[MVP§%p++§%r]");
        ranks.put("MVP_PLUS", "§b[MVP§%p+§b]");
        ranks.put("MVP", "§b[MVP]");
        ranks.put("VIP_PLUS", "§a[VIP§6+§a]");
        ranks.put("VIP", "§a[VIP]");
        ranks.put("DEFAULT", "§7");

        colors.put("BLACK", "0");
        colors.put("DARK_BLUE", "1");
        colors.put("DARK_GREEN", "2");
        colors.put("DARK_AQUA", "3");
        colors.put("DARK_RED", "4");
        colors.put("DARK_PURPLE", "5");
        colors.put("GOLD", "6");
        colors.put("GRAY", "7");
        colors.put("DARK_GRAY", "8");
        colors.put("BLUE", "9");
        colors.put("GREEN", "a");
        colors.put("AQUA", "b");
        colors.put("RED", "c");
        colors.put("LIGHT_PURPLE", "d");
        colors.put("YELLOW", "e");
        colors.put("WHITE", "f");
    }

    public static String getFormattedRank(JsonObject player){
        JsonElement _packageRank = player.get("packageRank");
        JsonElement _newPackageRank = player.get("newPackageRank");
        JsonElement _monthlyPackageRank = player.get("monthlyPackageRank");
        JsonElement _rankPlusColor = player.get("rankPlusColor");
        JsonElement _monthlyRankColor = player.get("monthlyRankColor");
        JsonElement _rank = player.get("rank");
        JsonElement _prefix = player.get("prefix");

        String packageRank = (_packageRank != null ? _packageRank.getAsString() : "");
        String newPackageRank = (_newPackageRank != null ? _newPackageRank.getAsString() : "");
        String monthlyPackageRank =(_monthlyPackageRank != null ? _monthlyPackageRank.getAsString() : "");
        String rankPlusColor = (_rankPlusColor != null ? _rankPlusColor.getAsString() : "");
        String monthlyRankColor =(_monthlyRankColor != null ? _monthlyRankColor.getAsString() : "");
        String rank = (_rank != null ? _rank.getAsString() : "");
        String prefix = (_prefix != null ? _prefix.getAsString() : "");

        if(rank.equalsIgnoreCase("NORMAL")) rank = ""; // Don't care about normies
        if(monthlyPackageRank.equalsIgnoreCase("NONE")) monthlyPackageRank = ""; // Don't care about cheapos
        if(packageRank.equalsIgnoreCase("NONE")) packageRank = "";
        if(newPackageRank.equalsIgnoreCase("NONE")) newPackageRank = "";

        if(!prefix.equalsIgnoreCase("")) return prefix;

        if (!rank.equalsIgnoreCase("") || !monthlyPackageRank.equalsIgnoreCase("") || !newPackageRank.equalsIgnoreCase("") || !packageRank.equalsIgnoreCase("")) {
            String theRank = "";
            if (!rank.equalsIgnoreCase(""))
                theRank = rank;
            else if (!monthlyPackageRank.equalsIgnoreCase(""))
                theRank = monthlyPackageRank;
            else if (!newPackageRank.equalsIgnoreCase(""))
                theRank = newPackageRank;
            else if (!packageRank.equalsIgnoreCase(""))
                theRank = packageRank;

            return replaceCustomColors(ranks.get(theRank), colors.get(rankPlusColor), colors.get(monthlyRankColor));
        }

        return replaceCustomColors(ranks.get("DEFAULT"), null, null);
    }

    public static String replaceCustomColors(String _rank, String p, String r){
        String rank = _rank;

        if (p != null)
            rank = rank.replace("%p", p);
        else
            rank = rank.replace("%p", "c");
        if (r != null)
            rank = rank.replace("%r", r);
        else
            rank = rank.replace("%r", "6");

        return rank;
    }
}
