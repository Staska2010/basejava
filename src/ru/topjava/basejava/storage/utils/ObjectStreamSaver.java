package ru.topjava.basejava.storage.utils;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSaver implements ObjectSaver {

    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(destination);
        oos.writeObject(r);
        oos.close();
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(resource);
        Resume result;
        try {
            result = (Resume) ois.readObject();
        } catch (ClassNotFoundException exc) {
            throw new StorageException("Resume class was not found in ClassPath", null, exc);
        }
        ois.close();
        return result;
    }
}
