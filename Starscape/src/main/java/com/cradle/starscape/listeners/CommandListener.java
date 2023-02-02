package com.cradle.starscape.listeners;

import com.cradle.starscape.CommandRegistrar;
import com.cradle.starscape.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CommandListener implements Listener {
    private List<String> disabledCommands;
    private Main main;
    private CommandRegistrar commandRegistrar;
    public CommandListener(Main main) {
        this.main = main;
        disabledCommands = Arrays.asList("/minecraft:me", "/me", "/say");
        commandRegistrar = main.getCommandRegistrar();
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String[] command = e.getMessage().split(" ");
        if (disabledCommands.contains(command[0])) {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(main.locale().msg("COMMAND_DISABLED", command[0]));
            }
        }
        if (commandRegistrar.getAliasMap().get(command[0]) != null) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            String translatedCommand = e.getMessage().replace(command[0], commandRegistrar.getAliasMap().get(command[0]));
            player.performCommand(translatedCommand);
        }
    }
}