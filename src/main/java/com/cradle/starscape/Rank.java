package com.cradle.starscape;

import org.bukkit.ChatColor;

public enum Rank {
    OWNER(
            new String[]{"&b&lOWNER &b"},
            new String[]{""},
            'a'
    ),
    ADMIN(
            new String[]{"&c&lADMIN &c"},
            new String[]{""},
            'b'
    ),
    MOD(
            new String[]{"&e&lMOD &e"},
            new String[]{""},
            'c'
    ),
    DONOR(
            new String[]{"&d"},
            new String[]{""},
            'd'
    ),
    DEFAULT(
            new String[]{"&7"},
            new String[]{"test.command"},
            'e'
    );

    private String[] titles;
    private String[] permissions;
    private char tabOrder;

    Rank(String[] titles, String[] permissions, char tabOrder) {
        if (titles.length >= 2 && titles[2] != null) {
            this.titles = new String[] {
                    ChatColor.translateAlternateColorCodes('&', titles[0]),
                    ChatColor.translateAlternateColorCodes('&', titles[1])
            };

        } else if (titles.length >= 1 && titles[0] != null) {
            this.titles = new String[] {ChatColor.translateAlternateColorCodes('&', titles[0])};

        }
        this.permissions = permissions;
        this.tabOrder = tabOrder;
    }

    public String[] getTitles() {return titles;}
    public String[] getPermissions() {return permissions;}
    public char getTabOrder() {return tabOrder;}
    
}
