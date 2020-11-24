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

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            System.out.println("Resume  " + r.getUuid() + " already exists");
        } else {
            if (numberOfResumes >= storage.length) {
                System.out.println("Not enough space");
            } else {
                insertResume(r, index);
                numberOfResumes++;
            }
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("Резюме " + uuid + " не найдено");
        } else {
            deleteResume(index);
            storage[numberOfResumes - 1] = null;
            numberOfResumes--;
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            System.out.println("Резюме не найдено");
        } else {
            index = Math.abs(index);
            storage[index] = r;
        }
    }

    protected abstract int getIndex(String uuid);
    protected abstract void insertResume(Resume r, int index);
    protected abstract void deleteResume(int index);

}
