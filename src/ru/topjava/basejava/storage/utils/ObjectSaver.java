package ru.topjava.basejava.storage.utils;

import ru.topjava.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ObjectSaver {
    void writeObject(Resume r, OutputStream destination) throws IOException;

    Resume readObject(InputStream resource) throws IOException;
}
