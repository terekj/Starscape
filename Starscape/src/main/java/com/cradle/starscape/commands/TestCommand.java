package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.PlayerDocument;
import com.cradle.starscape.managers.Command;
import com.cradle.starscape.mongo.MongoAdapter;
import com.cradle.starscape.utils.ColorCode;
import com.cradle.starscape.utils.PunishmentLog;
import com.cradle.starscape.utils.PunishmentType;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        PlayerDocument pDoc = main.getPlayerManager().getPlayer(player.getUniqueId());
        if (args.length < 1) {
            player.sendMessage(ColorCode.translate("&cToo few arguments!"));
            return;
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ColorCode.translate("&c" + args[0] + " is not integer!"));
            return;
        }
        if (Integer.parseInt(args[0]) == 0) {
            pDoc.addPunishment(new PunishmentLog(
                    PunishmentType.BAN,
                    player.getUniqueId(),
                    "test punishment",
                    -1l
            ));
            player.sendMessage(ChatColor.RED + "Added punishment.");
            player.performCommand("test 2");
        } else if (Integer.parseInt(args[0]) == 1){
            pDoc.pardonBan();
            player.sendMessage(ChatColor.GREEN + "Pardoned punishment!");
            player.performCommand("test 2");
        } else if (Integer.parseInt(args[0]) == 2) {
            player.sendMessage(ColorCode.translate("&7Here are your active punishments:"));
            List<PunishmentLog> active = pDoc.getActivePunishments();
            player.sendMessage(active.toString());
        } else if (Integer.parseInt(args[0]) == 3) {
            player.sendMessage(ColorCode.translate("&7Here are all of your punishments:"));
            List<PunishmentLog> active = pDoc.getAllPunishments();
            player.sendMessage(active.toString());
        }
    }

        /*


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

         */

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
