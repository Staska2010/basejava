package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.utils.ObjectSaver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    protected ObjectStreamPathStorage(Path dir, ObjectSaver<OutputStream, InputStream> saver) {
        super(dir.toString(), saver);
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
            throw new StorageException("Resume class was not found in ClassPath", null, exc);
        }
    }
}
