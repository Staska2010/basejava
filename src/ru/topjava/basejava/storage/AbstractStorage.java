package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractStorage implements IStorage {
    protected final Resume[] storage = new Resume[10000];
    protected int numberOfResumes = 0;

    public int size() {
        return numberOfResumes;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            return null;
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

    protected abstract int getIndex(String uuid);
}
