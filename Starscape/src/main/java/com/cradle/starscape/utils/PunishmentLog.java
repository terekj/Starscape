package com.cradle.starscape.utils;

import java.util.UUID;

public class PunishmentLog {
    private PunishmentType type;
    private String punisher;
    private long issueDate;
    private String reason;
    private boolean pardonStatus;
    private Long expiration;


    public PunishmentLog(PunishmentType type, UUID punisher, String reason, Long expiration) {
        this.type = type;
        this.punisher = punisher.toString();
        this.issueDate = System.currentTimeMillis();
        this.reason = reason;
        this.pardonStatus = false;
        if (type.equals(PunishmentType.KICK)) {
            this.expiration = -1L;
        } else {
            this.expiration = expiration;
        }
    }
    public PunishmentLog(PunishmentType type, UUID punisher, long issueDate, String reason, boolean pardonStatus, Long expiration) {
        this.type = type;
        this.punisher = punisher.toString();
        this.issueDate = issueDate;
        this.reason = reason;
        this.pardonStatus = pardonStatus;
        if (type.equals(PunishmentType.KICK)) {
            this.expiration = -1L;
        } else {
            this.expiration = expiration;
        }
    }
    public void pardon() {pardonStatus = true;}
    public PunishmentType getType() {
        return type;
    }
    public String getPunisher() {
        return punisher;
    }
    public long getIssueDate() {
        return issueDate;
    }
    public String getReason() { return reason; }
    public boolean getPardonStatus() {
        return pardonStatus;
    }

    public long getExpiration() {
        return expiration;
    }

    public boolean isOver() {
        return (expiration < System.currentTimeMillis() && expiration != -1L) || pardonStatus;
    }

    @Override
    public String toString() {
        return "{" + type + ": {punisher: \"" + punisher + "\", issued: \"" + issueDate + ", reason: \"" + reason + "\", pardonStatus: " + pardonStatus + ", expires: \"" + expiration + "}}";
    }

}
