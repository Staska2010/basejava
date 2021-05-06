package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected final static int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int numberOfResumes = 0;

    public int size() {
        return numberOfResumes;
    }

    public void clear() {
        Arrays.fill(storage, 0, numberOfResumes, null);
        numberOfResumes = 0;
    }

    @Override
    public Resume getResume(Object index) {
        return storage[(int) index];
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, numberOfResumes);
    }

    @Override
    public void saveResume(Resume resume, Object index) {
        if (numberOfResumes >= storage.length) {
            throw new StorageException("Not enough space", resume.getUuid());
        }
        insertResumeToArray(resume, (int) index);
        numberOfResumes++;
    }

    @Override
    public void deleteResume(Object index) {
        replaceDeletedElement((int) index);
        storage[numberOfResumes - 1] = null;
        numberOfResumes--;
    }

    @Override
    public void updateResume(Resume r, Object index) {
        storage[(int) index] = r;
    }

    @Override
    protected boolean isInStorage(Object index) {
        return (int) index >= 0;
    }

    protected abstract void insertResumeToArray(Resume r, int index);

    protected abstract void replaceDeletedElement(int index);

}
