package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.List;


public interface IStorage {
    void clear();

    void save(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    void update(Resume r);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    List<Resume> getAllSorted();

    int size();
}
