package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.managers.Command;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommand extends Command {
    private Main main;
    public GamemodeCommand(Main main) {
        super(
                "gamemode",
                new String[]{"gm"},
                "Change a user's gamemode.",
                ""
        );
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String gamemode = null;
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure")) {
                    gamemode = "adventure";
                } else if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")) {
                    gamemode = "survival";
                } else if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")) {
                    gamemode = "creative";
                } else if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")) {
                    gamemode = "spectator";
                }
            }
            if (gamemode != null) {
                if (sender.hasPermission("starscape.gamemode.*") || sender.hasPermission("starscape.gamemode." + gamemode)) {
                    Player target = player;
                    if (args.length >= 2) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(main.locale().msg("PLAYER_NONEXISTENT"), args[1]);
                            return;
                        } else {
                            target = Bukkit.getPlayer(args[1]);
                            if (target != player && !player.hasPermission("starscape.gamemode.others")) {
                                player.sendMessage(main.locale().msg("NO_PERMISSION"));
                                return;
                            }
                        }
                    }
                    target.setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
                    if (target == player) {
                        player.sendMessage(main.locale().msg("GAMEMODE_CHANGED_SELF", gamemode.toUpperCase()));
                    } else {
                        player.sendMessage(main.locale().msg("GAMEMODE_CHANGED_SENDER", new Object[]{target.getName(), gamemode.toUpperCase()}));
                        target.sendMessage(main.locale().msg("GAMEMODE_CHANGED_SUBJECT", new Object[]{player.getName(), gamemode.toUpperCase()}));
                    }
                } else {
                    player.sendMessage(main.locale().msg("NO_PERMISSION"));
                }
            } else {
                player.sendMessage(main.locale().msg("GAMEMODE_SYNTAX"));
            }
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}