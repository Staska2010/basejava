package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.utils.ObjectSaver;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {

    public ObjectStreamStorage(File dir, ObjectSaver<OutputStream, InputStream> saver) {
        super(dir, saver);
    }

    @Override
    protected void writeToFile(OutputStream os, Resume resume) throws IOException {
        saver.writeObject(resume, os);
    }

    @Override
    protected Resume readFromFile(InputStream is) throws IOException {
        try {
            return saver.readObject(is);
        } catch (ClassNotFoundException exc) {
            throw new NotSerializableException("Resume class was not found in ClassPath");
        }
    }
}
