package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.managers.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            /*
                /punish kick <player> <reason>
                /punish mute <player> <reason> [timespan]
                /punish ban <player> <reason> [timespan]
                /punish unmute <player>
                /punish unban <player>
             */
            if (args.length >= 1) {
                if (args.length >= 2 && Bukkit.getPlayer(args[1]) != null) {
                    Player target = Bukkit.getPlayer(args[1]);
                    player.sendMessage("You have punished (" + args[0] + ") " + target.getName() + "! Oh no!");
                } else {
                    player.sendMessage(main.locale().msg("COMMAND_MISSING_PLAYER"));
                }
            } else {
                player.sendMessage(main.locale().msg("PUNISH_SYNTAX"));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}