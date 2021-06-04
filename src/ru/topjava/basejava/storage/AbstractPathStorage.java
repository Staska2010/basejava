package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.utils.ObjectSaver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path dir;
    protected ObjectSaver<OutputStream, InputStream> saver;

    protected AbstractPathStorage(String dir, ObjectSaver<OutputStream, InputStream> saver) {
        Path directory = Paths.get(dir);
        this.dir = Objects.requireNonNull(directory, "Directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + " is not a directory!");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not readable/writable!");
        }
        this.saver = saver;
    }

    @Override
    protected boolean isInStorage(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getIndex(String uuid) {
        return Path.of(dir.toString(), uuid);
    }

    @Override
    protected void saveResume(Resume r, Path path) {
        try {
            Files.createFile(path);
            writeToFile(new BufferedOutputStream(Files.newOutputStream(path)), r);
        } catch (IOException exc) {
            throw new StorageException("IO error", path.toString(), exc);
        }
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return readFromFile(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException exc) {
            throw new StorageException("File read error", path.toString(), exc);
        }
    }

    @Override
    protected void deleteResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException exc) {
            throw new StorageException("Error while delete", path.toString(), exc);
        }
    }

    @Override
    protected void updateResume(Resume r, Path path) {
        try {
            Files.delete(path);
            Files.createFile(path);
            writeToFile(new BufferedOutputStream(Files.newOutputStream(path)), r);
        } catch (IOException exc) {
            throw new StorageException("IO error", path.toString(), exc);
        }
    }

    @Override
    protected List<Resume> getAll() {
        return getListOfFiles(dir).map(this::getResume).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getListOfFiles(dir).forEach(this::deleteFile);
    }

    @Override
    public int size() {
        return (int) getListOfFiles(dir).count();
    }

    private Stream<Path> getListOfFiles(Path directory) {
        try {
            return Files.list(directory);
        } catch (IOException exc) {
            throw new StorageException("IO error while retrieving list of files", null, exc);
        }
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException exc) {
            throw new StorageException("IO error while delete file", null, exc);
        }
    }

    protected abstract void writeToFile(OutputStream os, Resume resume) throws IOException;

    protected abstract Resume readFromFile(InputStream is) throws IOException;
}
