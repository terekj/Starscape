package com.cradle.starscape;

import com.cradle.starscape.mongo.MongoAdapter;
import com.cradle.starscape.utils.ColorCode;
import com.cradle.starscape.utils.PunishmentLog;
import com.cradle.starscape.utils.PunishmentType;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerDocument {

    private final Main main;
    private Document document;

    private final UUID uuid;
    private String nickname;
    private String rank;
    private ArrayList<String> titles;

    private int xp;
    private int level;
    private double balance;

    private ArrayList<PunishmentLog> punishments;

    public PlayerDocument(Main main, UUID uuid) {
        this.main = main;
        this.uuid = uuid;
        String name = Bukkit.getOfflinePlayer(uuid).getName();

        document = getDocument();

        if (document != null) {

            nickname = document.getString("nickname");
            rank = document.getString("rank").toUpperCase();
            titles = (ArrayList<String>) document.getList("titles", String.class);

            xp = document.getInteger("xp");
            level = document.getInteger("level");
            balance = document.getDouble("balance");

            List<Document> serializedPunishments = document.getList("punishments", Document.class);
            ArrayList<PunishmentLog> deserializedPunishments = new ArrayList<>();
            serializedPunishments.forEach( e -> {
                deserializedPunishments.add((PunishmentLog) MongoAdapter.deserialize(e));
            });
            punishments = deserializedPunishments;

        } else {
            nickname = null;
            rank = "DEFAULT";
            titles = new ArrayList<>();

            xp = 0;
            level = 0;
            balance = 0;

            punishments = new ArrayList<>();
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

                    .append("punishments", new ArrayList<String>());

            document = newDocument;
            main.getDatabase().getPlayers().insertOne(newDocument);
        }
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
        Document update = new Document("$set", new Document().append("nickname", nickname));
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
        Document update = new Document(
                "$set",
                new Document().append("rank", rank)
        );
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
        Document update = new Document(
                "$set",
                new Document().append("titles", titles)
        );
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setXP(int xp) {
        this.xp = xp;
        Document update = new Document(
                "$set",
                new Document().append("xp", xp));
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setLevel(int level) {
        this.level = level;
        Document update = new Document(
                "$set",
                new Document().append("level", level)
        );
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }

    public void setBalance(double balance) {
        this.balance = balance;
        Document update = new Document(
                "$set",
                new Document().append("balance", balance)
        );
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
        if (suffix.equals("")) {
            return ColorCode.translate("&r" + prefix + " " + rank.getNameColor() + nickname);
        } else {
            return ColorCode.translate("&r" + prefix + " " + rank.getNameColor() + nickname + " " + suffix);
        }
    }
    public String getNameSansTitles() {
        String nickname = Bukkit.getOfflinePlayer(uuid).getName();
        Rank rank = main.getPlayerManager().getPlayer(uuid).getRank();
        if (getNickname() != null) {
            nickname = getNickname();
        }
        return ColorCode.translate("&r" + rank.getNameColor() + nickname);
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
    public void addPunishment(PunishmentLog punishment) {
        punishments.add(punishment);
        Document update = new Document(
                "$push",
                new Document().append("punishments", MongoAdapter.serialize(punishment))
        );
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }
    public void pardonBan() {

        PunishmentLog punishment = getActivePunishments().stream().filter(c -> c.getType().equals(PunishmentType.BAN)).findFirst().orElse(null);
        punishment.pardon();

        List<Document> updatedPunishments = punishments.stream().map(e -> (Document) MongoAdapter.serialize(e)).collect(Collectors.toList());

        Document update = new Document(
                "$set",
                new Document().append("punishments", updatedPunishments)
        );
        main.getDatabase().getPlayers().updateOne(Filters.eq("uuid", uuid), update);
    }
    public List<PunishmentLog> getAllPunishments() {
        return new ArrayList<>(punishments);
    }
    public List<PunishmentLog> getActivePunishments() {
        return punishments.stream().filter(log -> !log.isOver()).collect(Collectors.toList());
    }

    public Document getDocument() {
        try (MongoCursor cursor = main.getDatabase().getPlayers().find(Filters.eq("uuid", uuid)).cursor()) {
            if (cursor.hasNext()) {
                return (Document) cursor.next();
            }
            // close cursor???
        }
        return null;
    }
}