package ru.topjava.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.topjava.basejava.model.AbstractRecord;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractRecord.class, new JsonRecordAdapter<>())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        GSON.toJson(object, writer);
    }

    public static <T> T read(String value, Class<T> tClass) {
        return GSON.fromJson(value, tClass);
    }

    public static <T> String write(T object, Class<T> tClass) {
        return GSON.toJson(object, tClass);
    }
}
