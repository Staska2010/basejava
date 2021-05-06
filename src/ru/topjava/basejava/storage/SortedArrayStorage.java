package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Sorted array based storage for Resumes
 */

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected Object getIndex(String uuid) {
        Resume dummy = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, numberOfResumes, dummy);
    }

    @Override
    protected void insertResumeToArray(Resume r, Object index) {
        int position = Math.abs((int) index);
        System.arraycopy(storage, position - 1, storage, position, numberOfResumes - position + 1);
        storage[position - 1] = r;
    }

    @Override
    protected void replaceDeletedElement(Object index) {
        int position = (int) index;
        System.arraycopy(storage, position + 1, storage, position, numberOfResumes - position);
    }
}
