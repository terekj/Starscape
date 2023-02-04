package com.cradle.starscape.listeners;

import com.cradle.starscape.utils.ColorCode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ResourcePackListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setResourcePack("https://download.mc-packs.net/pack/255808725721e0b757f3d7bdc9ffea689f876973.zip");
    }
    @EventHandler
    public void resourcePackStatus(PlayerResourcePackStatusEvent e) {
        if (e.getStatus().equals(Status.DECLINED)) {
            e.getPlayer().kickPlayer(ColorCode.translate("&cYou must accept the resource pack to play!\n&eFor support, join our Discord @ &6starscape.gg/discord"));
        } else if (e.getStatus().equals(Status.FAILED_DOWNLOAD)) {
            e.getPlayer().kickPlayer(ColorCode.translate("&cThe resource pack failed to download!\n&eFor support, join our Discord @ &6starscape.gg/discord"));
        }
    }
}
