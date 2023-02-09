package com.cradle.starscape.mongo;

import com.cradle.starscape.mongo.codecs.PunishmentLogCodec;
import com.cradle.starscape.mongo.codecs.PunishmentTypeCodec;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MongoAdapter {

    private final static String DOCUMENT_FIELD  = "__adapterName__";

    private final static List<MongoCodec<?>> loadedCodecs = new ArrayList<>();
    private final static List<Class<? extends MongoCodec<?>>> availableCodecs = Arrays.asList(
            PunishmentTypeCodec.class,
            PunishmentLogCodec.class
    );
    public static void loadCodecs() {
        availableCodecs.forEach(MongoAdapter::loadCodec);
        List<String> codecs = MongoAdapter.getCodecNames();
        System.out.println("Loaded " + codecs.size() + " codecs!" +
                "\nName of the loaded codecs: " + String.join(", ", codecs) +
                "\nIf you have problems with these, do not hesitate to report them."
        );
    }

    public static void loadCodec(Class<? extends MongoCodec<?>> codec) {
        try {
            MongoCodec<?> codecInstance = codec.getConstructor().newInstance();
            Class.forName(codecInstance.getReturnType().getCanonicalName());
            loadedCodecs.add(codecInstance);
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            System.out.println("Oops, the return class of the " + codec.getName() + " codec doesn't exists! Skipping registration.");
        } catch (ReflectiveOperationException e) {
            System.out.println("Oops, cannot load the " + codec.getName() + " codec! Look at the console for more details.");
            e.printStackTrace();
        }
    }
    public static List<String> getCodecNames() {
        return loadedCodecs.stream()
                .map(MongoCodec::getName)
                .collect(Collectors.toList());
    }
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> MongoCodec<T> getCodecByName(String name) {
        return (MongoCodec<T>) loadedCodecs.stream()
                .filter(codec -> codec.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> MongoCodec<T> getCodecByClass(Class<? extends T> clazz) {
        return (MongoCodec<T>) loadedCodecs.stream()
                .filter(codec -> codec.getReturnType().isAssignableFrom(clazz))
                .findFirst()
                .orElse(null);
    }
    public static Object serialize(Object unsafeObject) {
        if (unsafeObject == null)
            return null;
        System.out.println("Searching codec for " + unsafeObject.getClass() + " class...");
        MongoCodec<Object> codec = MongoAdapter.getCodecByClass(unsafeObject.getClass());
        if (codec == null) {
            return new Document();
        }
        System.out.println("A codec has been found: " + codec.getName());
        Document serializedDocument = codec.serialize(unsafeObject);
        serializedDocument.put(DOCUMENT_FIELD, codec.getName());
        System.out.println("Result of the serialization: " +
                "\nInitial object: " + unsafeObject +
                "\nSerialized document: " + serializedDocument.toJson());
        return serializedDocument;
    }
    public static Object deserialize(Object value) {
        if (!(value instanceof Document))
            return value;
        Document doc = (Document) value;
        if (!doc.containsKey(DOCUMENT_FIELD)) {
            System.out.println("Could not retrieve codec name.");
            return new Document();
        }
        String codecName = doc.getString(DOCUMENT_FIELD);
        MongoCodec<?> codec = MongoAdapter.getCodecByName(codecName);
        if (codec == null) {
            System.out.println("No codec found for " + codecName + "!" +
                    "\nLoaded codecs: " + String.join(", ", MongoAdapter.getCodecNames()) +
                    "\nRequested codec: " + codecName
            );
            return new Document();
        }
        try {
            return codec.deserialize(doc);
        } catch (StreamCorruptedException ex) {
            System.out.println("An error occurred during the deserialization of the document: " + ex.getMessage() +
                    "\nRequested codec: " + codecName +
                    "\nOriginal value class: " + doc +
                    "\nDocument JSON: " + doc.toJson()
            );
            return new Document();
        }
    }
}
