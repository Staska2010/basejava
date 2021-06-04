package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.strategy.Serializer;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File dir;
    protected final Serializer saver;

    protected FileStorage(File dir, Serializer saver) {
        this.dir = Objects.requireNonNull(dir, "Directory must not be null");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a directory!");
        }
        if (!dir.canRead() || !dir.canWrite()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not readable/writable!");
        }
        this.saver = saver;
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
        } catch (IOException exc) {
            throw new StorageException("IO error", file.getName(), exc);
        }
        updateResume(r, file);
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return saver.readObject(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException exc) {
            throw new StorageException("File read error", file.getName(), exc);
        }
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("Error deleting a file", "");
        }
    }

    @Override
    protected void updateResume(Resume r, File file) {
        try {
            saver.writeObject(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException exc) {
            throw new StorageException("IO error", file.getName(), exc);
        }
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> result = new LinkedList<>();
        for (File next : getFileList()) {
            result.add(getResume(next));
        }
        return result;
    }

    @Override
    public void clear() {
        for (File next : getFileList()) {
            next.delete();
        }
    }

    @Override
    public int size() {
        return getFileList().length;
    }

    private File[] getFileList() {
        if (dir.listFiles() != null) {
            return dir.listFiles();
        }
        throw new StorageException("Directory access error", "");
    }
}
