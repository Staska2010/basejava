package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File dir;

    protected AbstractFileStorage(File dir) {
        this.dir = Objects.requireNonNull(dir, "Directory must not be null");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory!");
        }
        if (!dir.canRead() || !dir.canWrite()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not readable/writable!");
        }
    }

    @Override
    protected boolean isInStorage(File file) {
        return file.exists();
    }

    @Override
    protected File getIndex(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected void saveResume(Resume r, File file) {
        try {
            file.createNewFile();
            writeToFile(file, r);
        } catch (IOException exc) {
            throw new StorageException("IO error", file.getName(), exc);
        }
    }

    @Override
    protected Resume getResume(File file) {
        return readFromFile(file);
    }

    @Override
    protected void deleteResume(File file) {
        file.delete();
    }

    @Override
    protected void updateResume(Resume r, File file) {
        try{
            file.delete();
            file.createNewFile();
            writeToFile(file, r);
        } catch (IOException exc) {
            throw new StorageException("IO error", file.getName(), exc);
        }

    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> result = new LinkedList<>();
        for(File next : Objects.requireNonNull(dir.listFiles())) {
            result.add(readFromFile(next));
        }
        return result;
    }

    @Override
    public void clear() {
        for(File next : Objects.requireNonNull(dir.listFiles())) {
            next.delete();
        }
    }

    @Override
    public int size() {
        return Objects.requireNonNull(dir.listFiles()).length;
    }

    protected abstract void writeToFile(File file, Resume resume) throws IOException;

    protected abstract Resume readFromFile(File file);
}
