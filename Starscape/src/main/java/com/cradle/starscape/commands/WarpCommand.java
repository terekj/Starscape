package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.managers.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;

public class WarpCommand extends Command {

    private final Main main;

    public WarpCommand(Main main) {
        super(
                "warp",
                new String[]{},
                "Create, delete, and teleport to warps!",
                ""
        );
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        List<String> existingWarps = main.getWarpManager().getWarps();

        if (args.length == 0) {
            player.performCommand("warp help");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                player.sendMessage(main.locale().msg("WARP_SYNTAX"));
                break;
            case "list":
                if (existingWarps.size() == 0) {
                    player.sendMessage(main.locale().msg("WARP_NONE"));
                } else {
                    player.sendMessage(main.locale().msg("WARP_LIST", new Object[]{existingWarps.size(), String.join(" ", existingWarps)}));
                }
                break;
            case "set":
                if (args.length < 2) {
                    player.sendMessage(main.locale().msg("WARP_NOT_SPECIFIED"));
                    break;
                }
                if (existingWarps.contains(args[1])) {
                    player.sendMessage(main.locale().msg("WARP_ALREADY_EXISTS", args[1]));
                    break;
                }
                try {
                    main.getWarpManager().setWarp(args[1], player.getLocation());
                    main.getWarpManager().saveWarps();
                } catch (IOException e) {
                    player.sendMessage(main.locale().msg("WARP_SAVE_ERROR", args[1]));
                    break;
                }
                player.sendMessage(main.locale().msg("WARP_SET", args[1]));
                break;
            case "delete", "del":
                if (args.length < 2) {
                    player.sendMessage(main.locale().msg("WARP_NOT_SPECIFIED"));
                    break;
                }
                if (!existingWarps.contains(args[1])) {
                    player.sendMessage(main.locale().msg("WARP_NONEXISTENT", args[1]));
                    return;
                }
                try {
                    main.getWarpManager().deleteWarp(args[1]);
                    main.getWarpManager().saveWarps();
                } catch (IOException e) {
                    player.sendMessage(main.locale().msg("WARP_DELETE_ERROR", args[1]));
                    return;
                }
                player.sendMessage(main.locale().msg("WARP_DELETE", args[1]));
                break;
            default:
                if (!existingWarps.contains(args[0])) {
                    player.sendMessage(main.locale().msg("WARP_NONEXISTENT", args[0]));
                    break;
                }
                Player target = player;
                if (args.length >= 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        target = Bukkit.getPlayer(args[1]);
                    } else {
                        player.sendMessage(main.locale().msg("PLAYER_OFFLINE", args[1]));
                        break;
                    }
                }
                target.teleport(main.getWarpManager().getWarp(args[0]));
                if (player == target) {
                    player.sendMessage(main.locale().msg("WARP_TELEPORT_SELF", args[0]));
                    break;
                }
                player.sendMessage(main.locale().msg("WARP_TELEPORT_SENDER", new String[]{main.getPlayerManager().getPlayer(target.getUniqueId()).getDisplayName(), args[0]}));
                target.sendMessage(main.locale().msg("WARP_TELEPORT_SUBJECT", new String[]{args[0], main.getPlayerManager().getPlayer(target.getUniqueId()).getDisplayName()}));
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 1:
                List<String> suggestedArguments = new ArrayList<>();
                suggestedArguments.addAll(Arrays.asList("help", "list", "set", "delete"));
                suggestedArguments.addAll(main.getWarpManager().getWarps());
                return suggestedArguments;
            case 2:
                if (main.getWarpManager().getWarps().contains(args[0])) {
                    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                    return onlinePlayers.stream().map(Player::getName).collect(Collectors.toList());
                }
                return main.getWarpManager().getWarps();
            default:
                return null;
        }
    }
}
