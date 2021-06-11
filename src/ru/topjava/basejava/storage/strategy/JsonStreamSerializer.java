package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamSerializer implements Serializer {
    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        try (Writer writer = new OutputStreamWriter(destination, StandardCharsets.UTF_8)) {
            JsonParser.write(r, writer);
        }
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}
