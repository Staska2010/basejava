package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected Object getIndex(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insertResumeToArray(Resume r, Object index) {
        storage[numberOfResumes] = r;
    }

    @Override
    public void replaceDeletedElement(Object index) {
        storage[(int) index] = storage[numberOfResumes - 1];
    }
}
