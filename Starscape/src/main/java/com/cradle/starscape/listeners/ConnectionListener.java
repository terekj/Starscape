package com.cradle.starscape.listeners;

import com.cradle.starscape.Main;
import com.cradle.starscape.PlayerDocument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.UUID;

public class ConnectionListener implements Listener {

    private final Main main;
    public ConnectionListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerDocument playerData = new PlayerDocument(main, uuid);
        main.getPlayerManager().addDocument(uuid, playerData);

        // listen for bans here!

        main.getNametagManager().setNametags(player);
        main.getNametagManager().newTag(player);
        e.setJoinMessage(main.locale().msg("PLAYER_JOIN", player));

        PermissionAttachment attachment;
        if (main.getPerms().containsKey(uuid)) {
            attachment = main.getPerms().get(uuid);

        } else {
            attachment = player.addAttachment(main);
            main.getPerms().put(uuid, attachment);
        }

        for (String perm : main.getPlayerManager().getPlayer(uuid).getRank().getPermissions()) {
            attachment.setPermission(perm, true);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        main.getNametagManager().removeTag(player);
        e.setQuitMessage(main.locale().msg("PLAYER_QUIT", player));
        main.getPlayerManager().removeDocument(player.getUniqueId());
    }

}