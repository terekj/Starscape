package com.cradle.starscape.utils;

import java.util.regex.*;
import java.util.regex.Matcher;

public class TimeParser {

    private static final Pattern TIME_PATTERN = Pattern.compile("([0-9]+)([wdhms])");
    private static final int
            SECOND = 1,
            MINUTE = 60,
            HOUR = 3600,
            DAY = 86400,
            WEEK = 604800;

    private static int parseStringToSeconds(String string) {
        Matcher m = TimeParser.TIME_PATTERN.matcher(string.toLowerCase());
        int total = 0;
        while (m.find()) {
            final int amount = Integer.parseInt(m.group(1));
            switch (m.group(2).charAt(0)) {
                case 's': {
                    total += amount * 1;
                    continue;
                }
                case 'm': {
                    total += amount * 60;
                    continue;
                }
                case 'h': {
                    total += amount * 3600;
                    continue;
                }
                case 'd': {
                    total += amount * 86400;
                    continue;
                }
                case 'w': {
                    total += amount * 604800;
                    continue;
                }
                default: {
                    total = 0;
                }
            }
        }
        return total;
    }

    public static long parseStringToMilliseconds(String string) {
        return parseStringToSeconds(string) * 1000L;
    }
}
