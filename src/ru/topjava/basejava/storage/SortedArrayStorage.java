package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Sorted array based storage for Resumes
 */

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected Object getIndex(String uuid) {
        Resume dummy = new Resume(uuid, "dummy");
        return Arrays.binarySearch(storage, 0, numberOfResumes, dummy,
                Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected void insertResumeToArray(Resume r, int index) {
        int position = Math.abs((int) index);
        System.arraycopy(storage, position - 1, storage, position, numberOfResumes - position + 1);
        storage[position - 1] = r;
    }

    @Override
    protected void replaceDeletedElement(int index) {
        int position = (int) index;
        System.arraycopy(storage, position + 1, storage, position, numberOfResumes - position);
    }

    @Override
    public List<Resume> getAllSorted() {
        return Arrays.asList(storage);
    }
}
