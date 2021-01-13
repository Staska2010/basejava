package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractStorage implements IStorage {
    protected int numberOfResumes = 0;
    protected final static int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    public int size() {
        return numberOfResumes;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistsStorageException(uuid);
        }
        return storage[index];
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, numberOfResumes);
    }

    public void clear() {
        Arrays.fill(storage, 0, numberOfResumes, null);
        numberOfResumes = 0;
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistsStorageException(resume.getUuid());
        } else if (numberOfResumes >= storage.length) {
            throw new StorageException("Not enough space", resume.getUuid());
        }
        insertResume(resume, index);
        numberOfResumes++;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            throw new NotExistsStorageException(uuid);
        } else {
            deleteResume(index);
            storage[numberOfResumes - 1] = null;
            numberOfResumes--;
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistsStorageException(r.getUuid());
        } else {
            storage[index] = r;
        }
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertResume(Resume r, int index);

    protected abstract void deleteResume(int index);
}
