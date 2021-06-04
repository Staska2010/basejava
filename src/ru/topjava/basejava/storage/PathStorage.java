package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.strategy.Serializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path dir;
    protected Serializer saver;

    protected PathStorage(String dir, Serializer saver) {
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
        return dir.resolve(uuid);
    }

    @Override
    protected void saveResume(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException exc) {
            throw new StorageException("IO error", path.toString(), exc);
        }
        updateResume(r, path);
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return saver.readObject(new BufferedInputStream(Files.newInputStream(path)));
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
            saver.writeObject(r, new BufferedOutputStream(Files.newOutputStream(path)));
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
        getListOfFiles(dir).forEach(this::deleteResume);
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
}
