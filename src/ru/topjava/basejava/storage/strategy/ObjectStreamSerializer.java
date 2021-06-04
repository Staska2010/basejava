package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSerializer implements Serializer {

    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(destination)) {
            oos.writeObject(r);
        }
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        Resume result;
        try(ObjectInputStream ois = new ObjectInputStream(resource)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException exc) {
            throw new StorageException("Resume class was not found in ClassPath", null, exc);
        }
    }
}
