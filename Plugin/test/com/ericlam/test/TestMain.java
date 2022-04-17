package com.ericlam.test;

import java.util.regex.Pattern;

public class TestMain {

    public static void main(String[] args) {
        final var cloud = "6.7.2";
        final var local = "6.7.1";
        final var newer = versionNewer(local, cloud, false);
        System.out.println("cloud: " + cloud + ", local: " + local + "; newer: " + newer);
    }

    private static final Pattern pt = Pattern.compile("(^[\\d\\.]+)");

    public static boolean versionNewer(String versionCurrent, String versionLatest, boolean unequal) {
        if (unequal) return versionCurrent.equals(versionLatest);
        else {
            if (versionCurrent.equals(versionLatest)) return true;
            var currentMatcher = pt.matcher(versionCurrent);
            var latestMatcher = pt.matcher(versionLatest);
            String[] current;
            String[] latest;
            if (currentMatcher.find()) {
                current = currentMatcher.group().split("\\.");
            } else {
                return false;
            }
            if (latestMatcher.find()) {
                latest = latestMatcher.group().split("\\.");
            } else {
                return true;
            }
            int length = Math.max(current.length, latest.length);
            for (int i = 0; i < length; i++) {
                int currentNum = i < current.length ? Integer.parseInt(current[i]) : 0;
                int latestNum = i < latest.length ? Integer.parseInt(latest[i]) : 0;
                if (currentNum > latestNum) return true;
                else if (currentNum < latestNum) return false;
            }
            return true;
        }
    }
}
