package com.cradle.starscape.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends BukkitCommand {

    public Command(String command, String[] aliases, String description, String permission) {
        super(command, description, "Starscape command.", Arrays.asList(aliases));
/*
        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatColor.RED + "You cannot do this!");
*/
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register("starscape", this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // do any conditions besides permission here
        execute(sender, args);
        return false;
    }
    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);


}