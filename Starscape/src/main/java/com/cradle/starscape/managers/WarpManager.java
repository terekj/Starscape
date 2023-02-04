package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpManager {

    private Main main;
    private File file;
    private YamlConfiguration warps;


    public WarpManager(Main main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "warps.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not load warps.yml");
            }
        }

        warps = YamlConfiguration.loadConfiguration(file);
    }


    public void saveWarps() throws IOException {
        warps.save(file);
    }

    public void setWarp(String name, Location location) {
        warps.set(name + ".location", location);
    }
    public void deleteWarp(String name) {
        warps.set(name, null);
    }

    public Location getWarp(String name) {
        return warps.getLocation(name + ".location");
    }

    public List<String> getWarps() {
        List<String> existingWarps = new ArrayList<>();
        for (Object key : warps.getKeys(false)) {
            existingWarps.add(key + "");
        }
        return existingWarps;
    }
}
