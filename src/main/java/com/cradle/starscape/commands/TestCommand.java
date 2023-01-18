package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.PlayerDocument;
import com.cradle.starscape.managers.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends Command {
    private Main main;
    public TestCommand(Main main) {
        super(
                "test",
                new String[]{},
                "Test command for plugin",
                "");
        this.main = main;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).sendMessage("hi :)");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
