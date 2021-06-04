package ru.topjava.basejava.storage.utils;

import ru.topjava.basejava.model.Resume;

import java.io.IOException;

public interface ObjectSaver<T, P> {
    void writeObject(Resume r, T destination) throws IOException;

    Resume readObject(P resource) throws IOException, ClassNotFoundException;
}
