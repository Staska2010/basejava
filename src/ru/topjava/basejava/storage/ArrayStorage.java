package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int numberOfResumes = 0;
    private int positionOfResumeInStorage = 0;

    public void clear() {
        Arrays.fill(storage, null);
        numberOfResumes = 0;
    }

    public void save(Resume r) {
        if (get(r.getUuid()) != null) {
            System.out.println("Такая запись существует");
        } else {
            if (numberOfResumes < storage.length) {
                storage[numberOfResumes++] = r;
            } else {
                System.out.println("Not enough space in storage");
            }
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                positionOfResumeInStorage = i;
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        if (get(uuid) != null) {
            System.arraycopy(storage, positionOfResumeInStorage + 1, storage, positionOfResumeInStorage, numberOfResumes - 1 - positionOfResumeInStorage);
            numberOfResumes--;
        } else {
            System.out.println("Резюме не найдено");
        }
    }

    public void update(Resume r) {
        if (get(r.getUuid()) != null) {
            storage[positionOfResumeInStorage] = r;
        } else {
            System.out.println("Резюме не найдено");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, numberOfResumes);
    }

    public int size() {
        return numberOfResumes;
    }
}
