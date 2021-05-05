package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Sorted array based storage for Resumes
 */

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected int getIndex(String uuid) {
        Resume dummy = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, numberOfResumes, dummy);
    }

    @Override
    protected void insertResumeToArray(Resume r, int index) {
        index = Math.abs(index);
        System.arraycopy(storage, index - 1, storage, index, numberOfResumes - index + 1);
        storage[index - 1] = r;
    }

    @Override
    protected void replaceDeletedElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, numberOfResumes - index);
    }
}
