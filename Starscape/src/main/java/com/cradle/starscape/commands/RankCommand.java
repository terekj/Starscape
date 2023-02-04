package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.Rank;
import com.cradle.starscape.managers.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RankCommand extends Command {

    private Main main;

    public RankCommand(Main main) {
        super(
                "rank",
                new String[]{},
                "Set a user's rank.",
                ""
        );
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 2) {
                    if (main.getPlayerManager().getPlayer(Bukkit.getOfflinePlayer(args[0]).getUniqueId()) != null) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                        for (Rank rank : Rank.values()) {
                            if (rank.name().equalsIgnoreCase(args[1])) {
                                player.sendMessage(
                                        main.locale().msg("RANK_CHANGED_SENDER", new Object[]{target, args[1].toUpperCase()})
                                );
                                if (target.isOnline() && target != player) {
                                    target.getPlayer().sendMessage(
                                            main.locale().msg("RANK_CHANGED_SUBJECT", new Object[]{player, args[1].toUpperCase()})
                                    );
                                }
                                main.getPlayerManager().getPlayer(target.getUniqueId()).setRank(args[1].toUpperCase(), false);
                                return;
                            }
                        }
                        player.sendMessage(main.locale().msg("RANK_NONEXISTENT", args[1].toUpperCase()));
                    } else {
                        player.sendMessage(main.locale().msg("PLAYER_NONEXISTENT", args[0]));
                    }
                } else {
                    player.sendMessage(main.locale().msg("RANK_SYNTAX"));
                }
            } else {
                player.sendMessage(main.locale().msg("NO_PERMISSION"));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}