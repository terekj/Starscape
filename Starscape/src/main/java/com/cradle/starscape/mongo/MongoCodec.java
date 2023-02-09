package com.cradle.starscape.mongo;

import org.bson.Document;

import javax.annotation.Nonnull;
import java.io.StreamCorruptedException;

public interface MongoCodec<T> {

    @Nonnull
    T deserialize(Document document) throws StreamCorruptedException;

    @Nonnull
    Document serialize(T object);

    @Nonnull
    Class<? extends T> getReturnType();

    @Nonnull
    String getName();

}
