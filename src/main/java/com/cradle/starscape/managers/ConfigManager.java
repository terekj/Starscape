package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class    ConfigManager {
    private static FileConfiguration config;

    public static void setupConfig(Main main) {
        ConfigManager.config = main.getConfig();
        main.saveDefaultConfig();
    }

    public static Location getSpawn() {
        return new Location(
                getWorld(),
                config.getDouble("spawn-location.x"),
                config.getDouble("spawn-location.y"),
                config.getDouble("spawn-location.z"),
                (float) config.getDouble("spawn-location.yaw"),
                (float) config.getDouble("spawn-location.pitch")
        );
    }

    public static World getWorld() {
        return Bukkit.getWorld(config.getString("world"));
    }

    public static String getPrefix() {
        return config.getString("server-appearance.prefix");
    }

    public static String getColorPrimary() {
        return config.getString("server-appearance.primary-color");
    }

    public static String getColorSecondary() {
        return config.getString("server-appearance.secondary-color");
    }

    public static String getColorText() {
        return config.getString("server-appearance.text-color");
    }

    public static String getColorEmoticon() {
        return config.getString("server-appearance.emoticon-color");
    }

    public static String getColorPing() {
        return config.getString("server-appearance.ping-color");
    }
}
