package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractStorage {

    @Override
    public void insertResume(Resume r, int index) {
        storage[numberOfResumes] = r;
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void deleteResume(int index) {
        storage[index] = storage[numberOfResumes - 1];
    }
}
