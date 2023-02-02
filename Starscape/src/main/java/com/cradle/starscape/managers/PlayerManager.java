package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import com.cradle.starscape.PlayerDocument;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private static final HashMap<UUID, PlayerDocument> playerDocuments = new HashMap<>();
    private Main main;

    public PlayerManager(Main main) {
        this.main = main;
    }

    public PlayerDocument getPlayer(UUID uuid) {
        if (!playerDocuments.containsKey(uuid) && Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
            addDocument(uuid, new PlayerDocument(main, uuid));
        }
        return playerDocuments.get(uuid);
    }

    public HashMap<UUID, PlayerDocument> getAllPlayers() {
        return playerDocuments;
    }

    public void addDocument(UUID uuid, PlayerDocument document) {
        playerDocuments.put(uuid, document);
    }
    public void removeDocument(UUID uuid) {
        playerDocuments.remove(uuid);
    }

}