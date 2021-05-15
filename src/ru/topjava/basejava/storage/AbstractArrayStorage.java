package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

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
    public Resume getResume(Integer index) {
        return storage[index];
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public List<Resume> getAll() {
        return Arrays.stream(storage, 0, numberOfResumes).collect(Collectors.toList());
    }

    @Override
    public void saveResume(Resume resume, Integer index) {
        if (numberOfResumes >= storage.length) {
            throw new StorageException("Not enough space", resume.getUuid());
        }
        insertResumeToArray(resume, index);
        numberOfResumes++;
    }

    @Override
    public void deleteResume(Integer index) {
        replaceDeletedElement(index);
        storage[numberOfResumes - 1] = null;
        numberOfResumes--;
    }

    @Override
    public void updateResume(Resume r, Integer index) {
        storage[index] = r;
    }

    @Override
    protected boolean isInStorage(Integer index) {
        return index >= 0;
    }

    protected abstract void insertResumeToArray(Resume r, int index);

    protected abstract void replaceDeletedElement(int index);

}
