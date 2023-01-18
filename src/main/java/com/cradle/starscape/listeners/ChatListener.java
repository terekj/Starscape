package com.cradle.starscape.listeners;

import com.cradle.starscape.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private Main main;

    public ChatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String name = main.getPlayerManager().getPlayer(player.getUniqueId()).getDisplayName();
        if (player.hasPermission("chat.colored")) {
            e.setFormat(main.locale().msg("CHAT_FORMAT_COLORED", new String[]{name, e.getMessage()}));
        } else {
            e.setFormat(main.locale().msg("CHAT_FORMAT_DEFAULT", new String[]{name, e.getMessage()}));
        }
    }
}
