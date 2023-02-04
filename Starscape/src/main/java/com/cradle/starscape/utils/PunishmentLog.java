package com.cradle.starscape.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class PunishmentLog {
    private String type;
    private String punisher;
    private Date issueDate;
    private String reason;
    private Date endDate;

    public PunishmentLog(String type, Player punisher, Date issueDate, String reason, @Nullable Date endDate) {
        this.type = type;
        this.punisher = punisher.getUniqueId().toString();
        this.issueDate = issueDate;
        this.reason = reason;
        if (!type.equalsIgnoreCase("kick") && endDate != null) {
            this.endDate = endDate;
        }
    }
    @Override
    public String toString() {
        if (!type.equalsIgnoreCase("kick") && endDate != null) {
            return "{" + type + ": {punisher: \"" + punisher + "\", issued: \"" + issueDate.toString() + "\", reason: \"" + reason + "\", expires: \"" + endDate + "\"}}";
        }
        return "{" + type + ": {punisher: \"" + punisher + "\", issued: \"" + issueDate.toString() + "\", reason: \"" + reason + "\"}}";
    }

}
