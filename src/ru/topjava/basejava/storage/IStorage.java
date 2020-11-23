package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;


public interface IStorage {
    void clear();

    void save(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    void update(Resume r);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll();

    int size();
}
