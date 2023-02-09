package com.cradle.starscape.mongo.codecs;

import com.cradle.starscape.mongo.MongoAdapter;
import com.cradle.starscape.mongo.MongoCodec;
import com.cradle.starscape.utils.PunishmentLog;
import com.cradle.starscape.utils.PunishmentType;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.StreamCorruptedException;
import java.util.UUID;

public class PunishmentLogCodec implements MongoCodec<PunishmentLog> {
    @NotNull
    @Override
    public PunishmentLog deserialize(Document document) throws StreamCorruptedException {
        UUID punisher = UUID.fromString(document.getString("punisher"));
        String reason = document.getString("reason");
        PunishmentType type = (PunishmentType) MongoAdapter.deserialize(document.get("type"));
        boolean pardonStatus = document.getBoolean("pardonStatus");
        long issueDate = document.getLong("issueDate");
        long expiration = document.getLong("expiration");

        if (reason == null || type == null) {
            throw new StreamCorruptedException("The punisher, type, and/or reason could not be retrieved from the punishment log!");
        }
        return new PunishmentLog(type, punisher, issueDate, reason, pardonStatus, expiration);
    }

    @NotNull
    @Override
    public Document serialize(PunishmentLog object) {
        Document serializedType = (Document) MongoAdapter.serialize(object.getType());
        return new Document()
                .append("type", serializedType)
                .append("punisher", object.getPunisher())
                .append("issueDate", object.getIssueDate())
                .append("reason", object.getReason())
                .append("pardonStatus", object.getPardonStatus())
                .append("expiration", object.getExpiration());
    }

    @NotNull
    @Override
    public Class<? extends PunishmentLog> getReturnType() {
        return PunishmentLog.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "PunishmentLog";
    }
}
