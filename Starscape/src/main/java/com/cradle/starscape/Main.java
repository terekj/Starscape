package com.cradle.starscape;

import com.cradle.starscape.listeners.ChatListener;
import com.cradle.starscape.listeners.CommandListener;
import com.cradle.starscape.listeners.ConnectionListener;
import com.cradle.starscape.listeners.ResourcePackListener;
import com.cradle.starscape.managers.*;
import com.cradle.starscape.mongo.Database;
import com.cradle.starscape.mongo.MongoAdapter;
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
    private BanManager banManager;
    private WarpManager warpManager;

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

        MongoAdapter.loadCodecs();

        playerManager = new PlayerManager(this);
        nametagManager = new NametagManager(this);
        localeManager = new LocaleManager(this);
        commandRegistrar = new CommandRegistrar(this);
        banManager = new BanManager(this);
        warpManager = new WarpManager(this);

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ResourcePackListener(), this);

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
    public BanManager getBanManager() {return banManager;}
    public WarpManager getWarpManager() {return warpManager;}

    public HashMap<UUID, PermissionAttachment> getPerms() {return perms;}
}