package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.managers.Command;
import com.cradle.starscape.utils.PunishmentLog;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestCommand extends Command {
    private final Main main;

    public TestCommand(Main main) {
        super(
                "test",
                new String[]{},
                "Tests different stuff.",
                ""
        );
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            player.sendMessage("Your UUID is: " + ChatColor.GRAY + uuid);
            player.sendMessage("Size of playerDocuments: " + main.getPlayerManager().getAllPlayers().size());
            player.sendMessage("All keys:");
            for (UUID key : main.getPlayerManager().getAllPlayers().keySet()) {
                player.sendMessage(key.toString());
            }
            if (main.getPlayerManager().getPlayer(uuid) != null) {
                player.sendMessage("Document matching your UUID was found.");
            } else {
                player.sendMessage("Document not found.");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
