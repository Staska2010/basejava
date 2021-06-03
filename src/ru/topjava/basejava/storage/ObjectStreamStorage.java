package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {
    public ObjectStreamStorage(File dir) {
        super(dir);
    }

    @Override
    protected void writeToFile(OutputStream os, Resume resume) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    @Override
    protected Resume readFromFile(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException exc) {
            throw new StorageException("Error while reading resume", null, exc);
        }
    }
}
