package com.cradle.starscape;

import com.cradle.starscape.listeners.ChatListener;
import com.cradle.starscape.listeners.CommandListener;
import com.cradle.starscape.listeners.ConnectionListener;
import com.cradle.starscape.managers.ConfigManager;
import com.cradle.starscape.managers.LocaleManager;
import com.cradle.starscape.managers.NametagManager;
import com.cradle.starscape.managers.PlayerManager;
import com.mongodb.MongoException;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;

    private Database database;
    private PlayerManager playerManager;
    private NametagManager nametagManager;
    private LocaleManager localeManager;
    private CommandRegistrar commandRegistrar;

    private HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    @Override
    public void onEnable() {
        this.instance = this;

        ConfigManager.setupConfig(this);

        database = new Database();
        try {
            database.connect();
        } catch (MongoException e) {
            e.printStackTrace();
        }

        playerManager = new PlayerManager(this);
        nametagManager = new NametagManager(this);
        localeManager = new LocaleManager(this);
        commandRegistrar = new CommandRegistrar(this);

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);

    }

    @Override
    public void onDisable() {
        database.disconnect();
    }

    public Main getInstance() {return instance;}
    public Database getDatabase() {
        return database;
    }
    public CommandRegistrar getCommandRegistrar() {return commandRegistrar;}
    public PlayerManager getPlayerManager() { return playerManager; }
    public NametagManager getNametagManager() {return nametagManager;}
    public LocaleManager locale() {return localeManager;}

    public HashMap<UUID, PermissionAttachment> getPerms() {return perms;}
}