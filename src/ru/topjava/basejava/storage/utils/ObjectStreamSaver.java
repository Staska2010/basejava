package ru.topjava.basejava.storage.utils;

import ru.topjava.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSaver implements ObjectSaver<OutputStream, InputStream> {

    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(destination);
        oos.writeObject(r);
        oos.close();
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(resource);
        Resume result = (Resume) ois.readObject();
        ois.close();
        return result;
    }
}
