package com.cradle.starscape;

import com.cradle.starscape.utils.ColorCode;

public enum Rank {
    FOUNDER(
            new String[]{"\uE000"},
            ColorCode.translate("&#33DA87"),
            new String[]{""},
            'a'
    ),
    ADMIN(
            new String[]{"\uE001"},
            ColorCode.translate("&#E83200"),
            new String[]{""},
            'b'
    ),
    MOD(
            new String[]{"\uE002"},
            ColorCode.translate("&#FFDE38"),
            new String[]{""},
            'c'
    ),
    ARTIST(
            new String[]{"\uE003"},
            ColorCode.translate("&#EC73EA"),
            new String[]{""},
            'c'
            ),
    DONOR(
            new String[]{"\uE004"},
            ColorCode.translate("&#99C2FF"),
            new String[]{""},
            'd'
    ),
    DEFAULT(
            new String[]{"\uE005"},
            "&7",
            new String[]{"test.command"},
            'e'
    );

    private String[] titles;
    private String nameColor;
    private String[] permissions;
    private char tabOrder;

    Rank(String[] titles, String nameColor, String[] permissions, char tabOrder) {
        if (titles.length >= 2 && titles[2] != null) {
            this.titles = new String[] {
                    ColorCode.translate(titles[0]),
                    ColorCode.translate(titles[1])
            };

        } else if (titles.length >= 1 && titles[0] != null) {
            this.titles = new String[] {ColorCode.translate(titles[0])};

        }
        this.nameColor = nameColor;
        this.permissions = permissions;
        this.tabOrder = tabOrder;
    }

    public String[] getTitles() {return titles;}
    public String getNameColor() {return nameColor;}
    public String[] getPermissions() {return permissions;}
    public char getTabOrder() {return tabOrder;}
    
}
