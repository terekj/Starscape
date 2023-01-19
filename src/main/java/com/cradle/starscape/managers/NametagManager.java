package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import com.cradle.starscape.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NametagManager {
    private Main main;

    public NametagManager (Main main) {
        this.main = main;
    }

    public void setNametags(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        for (Rank rank : Rank.values()) {
            Team team = player.getScoreboard().registerNewTeam(getTeamName(rank));
            if (rank.getTitles().length >= 1 && rank.getTitles()[0] != null) {team.setPrefix(rank.getTitles()[0] + " ");}
            if (rank.getTitles().length >= 2 && rank.getTitles()[1] != null) {team.setSuffix(rank.getTitles()[1]);}
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId() != target.getUniqueId()) {
                String name = getTeamName(main.getPlayerManager().getPlayer(target.getUniqueId()).getRank());
                player.getScoreboard().getTeam(name).addEntry(target.getName());
            }
        }

    }
    public void newTag(Player player) {
        Rank rank = main.getPlayerManager().getPlayer(player.getUniqueId()).getRank();
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.getScoreboard().getTeam(getTeamName(rank)).addEntry(player.getName());
        }
    }
    public void removeTag(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.getScoreboard().getEntryTeam(player.getName()).removeEntry(player.getName());
        }
    }
    public String getTeamName(Rank rank) {
        return rank.getTabOrder() + rank.name();
    }
}
