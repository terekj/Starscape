package com.cradle.starscape;

import com.cradle.starscape.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class CommandRegistrar {
    private Main main;
    private HashMap<String, String> aliasMap;
    public CommandRegistrar(Main main) {
        this.main = main;

        new RankCommand(this.main);
        new GamemodeCommand(this.main);
        new PunishCommand(this.main);
        new TestCommand(this.main);
        new WarpCommand(this.main);

        aliasMap = new HashMap<>();
        aliasMap.put("/kick", "punish kick");
        aliasMap.put("/mute", "punish mute");
        aliasMap.put("/ban", "punish ban");
        aliasMap.put("/gma", "gamemode adventure");
        aliasMap.put("/gms", "gamemode survival");
        aliasMap.put("/gmc", "gamemode creative");
        aliasMap.put("/gmsp", "gamemode spectator");

        for (String alias : aliasMap.keySet()) {
            try {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                CommandMap map = (CommandMap) field.get(Bukkit.getServer());
                map.register("starscape", new Command(alias.substring(1), "Alias for / " + aliasMap.get(alias), "Alias command.", Collections.singletonList("")) {
                    @Override
                    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                        return false;
                    }
                    @Override
                    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                        return null;
                    }
                });
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public HashMap<String, String> getAliasMap() {
        return aliasMap;
    }
}