package com.cradle.starscape.commands;

import com.cradle.starscape.Main;
import com.cradle.starscape.managers.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PunishCommand extends Command {
    private Main main;
    public PunishCommand(Main main) {
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

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
