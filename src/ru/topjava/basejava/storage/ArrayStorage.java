package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected Integer getIndex(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insertResumeToArray(Resume r, int index) {
        storage[numberOfResumes] = r;
    }

    @Override
    public void replaceDeletedElement(int index) {
        storage[index] = storage[numberOfResumes - 1];
    }
}
