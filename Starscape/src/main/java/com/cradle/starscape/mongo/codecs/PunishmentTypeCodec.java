package com.cradle.starscape.mongo.codecs;

import com.cradle.starscape.mongo.MongoCodec;
import com.cradle.starscape.utils.PunishmentType;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.StreamCorruptedException;

public class PunishmentTypeCodec implements MongoCodec<PunishmentType> {
    @NotNull
    @Override
    public PunishmentType deserialize(Document document) throws StreamCorruptedException {
        PunishmentType type = PunishmentType.valueOf(document.getString("punishmentType"));
        if (type != null) return type;
        return null;
    }

    @NotNull
    @Override
    public Document serialize(PunishmentType object) {
        Document document = new Document().append("punishmentType", object.name());
        return document;
    }

    @NotNull
    @Override
    public Class<? extends PunishmentType> getReturnType() {
        return PunishmentType.class;
    }

    @NotNull
    @Override
    public String getName() {
        return "PunishmentType";
    }
}
