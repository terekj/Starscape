package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.PlayerDocument;
import com.cradle.starscape.managers.Command;
import com.cradle.starscape.utils.PunishmentLog;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PunishCommand extends Command {
    private Main main;
    public PunishCommand(Main main) {
        super(
                "punish",
                new String[]{"pun"},
                "Set a user's rank.",
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
        /*
            /punish help
            /punish kick <player> <reason>
            /punish mute <player> <reason> [timespan]
            /punish ban <player> <reason> [timespan]
            /punish unmute <player>
            /punish unban <player>
         */
        if (args.length == 0) {
            player.performCommand("punish help");
            return;
        }

        if (args[0].equalsIgnoreCase("help") || !Arrays.asList("kick", "mute", "ban", "unmute", "unban").contains(args[0].toLowerCase())) {
            player.sendMessage(main.locale().msg("PUNISH_SYNTAX"));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(main.locale().msg("COMMAND_MISSING_PLAYER"));
            return;
        }

        if (main.getPlayerManager().getPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()) == null) {
            player.sendMessage(main.locale().msg("PLAYER_NONEXISTENT", args[1]));
            return;
        }

        if (Arrays.asList("kick", "mute", "ban").contains(args[0].toLowerCase()) && args.length < 3) {
            player.sendMessage(main.locale().msg("PUNISH_MISSING_REASON"));
            return;
        }


        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
/*
        if (target == player) {
            player.sendMessage(main.locale().msg("PUNISH_REJECT_SELF"));
            return;
        }
        if (target.isOp()) {
            player.sendMessage(main.locale().msg("PUNISH_REJECT_OP"));
            return;
        }
 */
        PlayerDocument targetWrapper = main.getPlayerManager().getPlayer(target.getUniqueId());
        Date endDate = null;
        String reason = "";
        if (!Arrays.asList("unmute", "unban").contains(args[0])) {
            endDate = new Date();
            reason = String.join(" ", args).split(" ", 3)[2];
        }
        Player subject = null;
        if (target.isOnline()) {
            subject = (Player) target;
        }

        switch (args[0].toLowerCase()) {
            case "kick":
                if (subject != null) {
                    //subject.kickPlayer(main.locale().msg("PUNISH_KICKED_SUBJECT", new String[]{player.getName(), reason}));
                    subject.sendMessage(main.locale().msg("PUNISH_KICKED_SUBJECT", new String[]{player.getName(), reason}));
                    // ^ for testing purposes only, delete & replace with kick
                }
                player.sendMessage(main.locale().msg("PUNISH_TIMELESS_SENDER", new String[]{target.getName(), "KICK", reason}));
                break;
            case "mute":
                if (subject != null) {
                    subject.sendMessage(main.locale().msg("PUNISH_MUTED_SUBJECT", new String[]{player.getName(), reason, "&4&nDURATION"}));
                }
                player.sendMessage(main.locale().msg("PUNISH_TIMED_SENDER", new String[]{target.getName(), "MUTE", reason, "&4&nDURATION"}));
                break;
            case "ban":
                if (subject != null) {
                    //subject.kickPlayer(main.locale().msg("PUNISH_BAN_SUBJECT", new String[]{player.getName(), reason, "&4&nDURATION"}));
                    subject.sendMessage(main.locale().msg("PUNISH_BANNED_SUBJECT", new String[]{player.getName(), reason, "&4&nDURATION"}));
                    // ^ for testing purposes only, delete & replace with kick
                }
                player.sendMessage(main.locale().msg("PUNISH_TIMED_SENDER", new String[]{target.getName(), "BAN", reason, "&4&nDURATION"}));
                //main.getBanManager().banPlayer(target, endDate);
                break;
            case "unmute":
                player.sendMessage("You unmuted the player!");
                break;
            case "unban":
                player.sendMessage("You unbanned the player!");
                break;
            default:
                player.performCommand("punish help");
                return;
        }
/*
        if (Arrays.asList("kick", "mute", "ban").contains(args[0].toLowerCase())) {
            targetWrapper.addToHistory(new PunishmentLog(args[0].toUpperCase(), player, new Date(), reason, endDate));
        } else if (Arrays.asList("unmute", "unban").contains(args[0].toLowerCase())) {
            targetWrapper.addToHistory(new PunishmentLog(args[0].toUpperCase(), player, new Date(), "", endDate));
        }

 */
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 1:
                return Arrays.asList("kick", "mute", "ban", "unmute", "unban");
            case 2:
                Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                return onlinePlayers.stream().map(Player::getName).collect(Collectors.toList());
            default:
                return null;
        }
    }
}