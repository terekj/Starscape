package com.cradle.starscape;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDocument {

    private final Main main;
    private Document document;

    private final UUID uuid;
    private String name;
    private String nickname;
    private String rank;
    private ArrayList<String> titles;

    private int xp;
    private int level;
    private double balance;

    private ArrayList<String> banInfo;
    private ArrayList<String> muteInfo;
    private ArrayList<String> punishmentHistory;

    public PlayerDocument(Main main, UUID uuid) {
        this.main = main;
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();

        document = getDocument(uuid);

        if (document != null) {

            nickname = document.getString("nickname");
            rank = document.getString("rank").toUpperCase();
            titles = (ArrayList<String>) document.getList("titles", String.class);

            xp = document.getInteger("xp");
            level = document.getInteger("level");
            balance = document.getDouble("balance");

            banInfo = (ArrayList<String>) document.getList("banInfo", String.class);
            muteInfo = (ArrayList<String>) document.getList("muteInfo", String.class);
            punishmentHistory = (ArrayList<String>) document.getList("punishmentHistory", String.class);
        } else {
            rank = "DEFAULT";

            xp = 0;
            level = 0;
            balance = 0;
            Document newDocument = new Document()
                    .append("uuid", uuid)
                    .append("dateJoined", new Date())
                    .append("name", name)
                    .append("nickname", nickname)
                    .append("rank", rank.toUpperCase())
                    .append("titles", new ArrayList<String>())

                    .append("xp", xp)
                    .append("level", level)
                    .append("balance", balance)

                    .append("banInfo", new ArrayList<String>())
                    .append("muteInfo", new ArrayList<String>())
                    .append("punishmentHistory", new ArrayList<String>());

            document = newDocument;
            main.getDatabase().getPlayers().insertOne(newDocument);
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        Document newValue = new Document().append("nickname", nickname);
        Document update = new Document("$set", newValue);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setRank(String rank, boolean firstJoin) {
        if (Bukkit.getOfflinePlayer(uuid).isOnline() && !firstJoin) {
            Player player = Bukkit.getPlayer(uuid);
            HashMap<UUID, PermissionAttachment> perms = main.getPerms();
            PermissionAttachment attachment;
            if (perms.containsKey(uuid)) {
                attachment = perms.get(uuid);

            } else {
                attachment = player.addAttachment(main);
                perms.put(uuid, attachment);
            }

            for (String perm : getRank().getPermissions()) {
                if (player.hasPermission(perm)) {
                    attachment.unsetPermission(perm);
                }
            }

            for (String perm : Rank.valueOf(rank).getPermissions()) {
                attachment.setPermission(perm, true);
            }
        }


        this.rank = rank.toUpperCase();
        Document newValue = new Document().append("rank", rank);
        Document update = new Document("$set", newValue);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);

        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Player player = Bukkit.getPlayer(uuid);
            main.getNametagManager().removeTag(player);
            main.getNametagManager().newTag(player);
        }
    }

    public void setTitles(String prefix, String suffix) {
        titles.set(0, prefix);
        titles.set(1, suffix);
        Document newValues = new Document().append("titles", titles);
        Document update = new Document("$set", newValues);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setXP(int xp) {
        this.xp = xp;
        Document newValue = new Document().append("xp", xp);
        Document update = new Document("$set", newValue);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setLevel(int level) {
        this.level = level;
        Document newValue = new Document().append("level", level);
        Document update = new Document("$set", newValue);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setBalance(double balance) {
        this.balance = balance;
        Document newValue = new Document().append("balance", balance);
        Document update = new Document("$set", newValue);
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public String getNickname() {
        return nickname;
    }

    public String getDisplayName() {
        String prefix = "";
        String suffix = "";
        String nickname = Bukkit.getOfflinePlayer(uuid).getName();
        Rank rank = main.getPlayerManager().getPlayer(uuid).getRank();
        if (rank.getTitles().length >= 1 && rank.getTitles()[0] != null) {
            prefix = rank.getTitles()[0];
        }
        if (rank.getTitles().length >= 2 && rank.getTitles()[1] != null) {
            suffix = rank.getTitles()[1];
        }
        if (getTitles().size() >= 1 && getTitles().get(0) != null) {
            prefix = getTitles().get(0);
        }
        if (getTitles().size() >= 2 && getTitles().get(1) != null) {
            suffix = getTitles().get(1);
        }
        if (getNickname() != null) {
            nickname = getNickname();
        }
        if (suffix == "") {
            return "&r" + prefix + " " + rank.getNameColor() + nickname;
        } else {
            return "&r" + prefix + " " + rank.getNameColor() + nickname + " " + suffix;
        }
    }
    public String getNameSansTitles() {
        String nickname = Bukkit.getOfflinePlayer(uuid).getName();
        Rank rank = main.getPlayerManager().getPlayer(uuid).getRank();
        if (getNickname() != null) {
            nickname = getNickname();
        }
        return "&r" + rank.getNameColor() + nickname;
    }

    public Rank getRank() {
        return Rank.valueOf(rank);
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public double getXP() {
        return xp;
    }

    public double getLevel() {
        return level;
    }

    public double getBalance() {
        return balance;
    }

    public Document getDocument(UUID uuid) {
        try (MongoCursor cursor = main.getDatabase().getPlayers().find(Filters.eq("uuid", uuid)).cursor()) {
            if (cursor.hasNext()) {
                return (Document) cursor.next();
            }
            // close cursor???
        }
        return null;
    }
}
