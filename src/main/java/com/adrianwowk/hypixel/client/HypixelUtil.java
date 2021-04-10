package com.adrianwowk.hypixel.client;

public class HypixelUtil {

    public static double BASE = 10_000;
    public static double GROWTH = 2_500;

    /* Constants to generate the total amount of XP to complete a level */
    public static double HALF_GROWTH = 0.5 * GROWTH;

    /* Constants to look up the level from the total amount of XP */
    public static double REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH;
    public static double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
    public static double GROWTH_DIVIDES_2 = 2 / GROWTH;

    public static double getLevel(double exp) {
        return exp < 0 ? 1 : Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + GROWTH_DIVIDES_2 * exp));
    }

    public static double getExactLevel(double exp) {
        return getLevel(exp) + getPercentageToNextLevel(exp);
    }

    public static double getTotalExpToLevel(double level) {
        double lv = Math.floor(level), x0 = getTotalExpToFullLevel(lv);
        if (level == lv) return x0;
        return (getTotalExpToFullLevel(lv + 1) - x0) * (level % 1) + x0;
    }

    public static double getTotalExpToFullLevel(double level) {
        return (HALF_GROWTH * (level - 2) + BASE) * (level - 1);
    }

    public static double getPercentageToNextLevel(double exp) {
        double lv = getLevel(exp), x0 = getTotalExpToLevel(lv);
        return (exp - x0) / (getTotalExpToLevel(lv + 1) - x0);
    }

}
