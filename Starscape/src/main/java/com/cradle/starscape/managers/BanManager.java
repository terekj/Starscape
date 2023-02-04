package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import com.cradle.starscape.utils.PunishmentLog;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BanManager {
    private final Main main;
    private final File file;
    private YamlConfiguration bans;
    private List<UUID> bannedUsers;

    public BanManager(Main main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "banned-users.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not load banned-users.yml");
            }
        }

        bans = YamlConfiguration.loadConfiguration(file);

        bannedUsers = new ArrayList<>();
        for (Object key : bans.getKeys(false)) {
            bannedUsers.add(UUID.fromString(key + ""));
        }
    }
    public void banPlayer(OfflinePlayer player, @Nullable Date endDate) {
        bans.set(player.getUniqueId() + ".expires", endDate);
        bannedUsers.add(player.getUniqueId());
    }
    public boolean isBanned(OfflinePlayer player) {
        return bannedUsers.contains(player.getUniqueId());
    }
    public boolean unbanPlayer(OfflinePlayer player, Player issuer, Date endDate) {
        if (isBanned(player)) {
            UUID uuid = player.getUniqueId();
            bannedUsers.remove(uuid);
            bans.set(uuid + ".expires", null);
            PunishmentLog log = new PunishmentLog("UNBAN", issuer, new Date(), "-", null);
            main.getPlayerManager().getPlayer(uuid).addToHistory(log);
            return true;
        }
        return false;
    }
    public List<UUID> getBannedUsers() { return bannedUsers;}
}
