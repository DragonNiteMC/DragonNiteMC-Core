package com.dragonite.mc.dnmc.core.utils;

/**
 * Normal Tools
 */
public class Tools {

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    public static String toLongString(String[] args, int startFrom) {
        StringBuilder arg = new StringBuilder(startFrom);
        for (int i = startFrom; i < args.length; i++) {
            String str = args[i];
            arg.append(' ').append(str);
        }
        return arg.toString();
    }

}
