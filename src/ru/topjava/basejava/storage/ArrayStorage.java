package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractStorage {

    public void save(Resume r) {
        if (get(r.getUuid()) != null) {
            System.out.println("Resume already exists");
        } else {
            if (numberOfResumes < storage.length) {
                storage[numberOfResumes++] = r;
            } else {
                System.out.println("Not enough space in storage");
            }
        }
    }


    protected int getIndex(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("Резюме не найдено");
        } else {
            storage[index] = storage[numberOfResumes];
            numberOfResumes--;
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index == -1) {
            System.out.println("Резюме не найдено");
            return;
        } else {
            storage[index] = r;
        }
    }
}
