package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractStorage {
    @Override
    protected int getIndex(String uuid) {
        Resume dummy = new Resume();
        dummy.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, numberOfResumes, dummy);
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            System.out.println("Resume already exists");
        } else {
            if (numberOfResumes >= storage.length) {
                System.out.println("Not enough space");
            } else {
                index = Math.abs(index);
                System.arraycopy(storage, index - 1, storage, index, numberOfResumes - index + 1);
                storage[index - 1] = r;
                numberOfResumes++;
            }
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Resume is not exists");
        } else {
            index = Math.abs(index);
            System.arraycopy(storage, index + 1, storage, index , numberOfResumes - index);
            numberOfResumes--;
        }
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            System.out.println("Resume is not found");
        } else {
            index = Math.abs(index);
            storage[index] = r;
        }
    }
}
