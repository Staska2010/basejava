package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.*;
import java.util.stream.Collectors;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new LinkedList<>();

    @Override
    protected boolean isInStorage(Object index) {
        return (int) index >= 0;
    }

    @Override
    public Object getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void saveResume(Resume r, Object index) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((int) index);
    }

    @Override
    protected void deleteResume(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected void updateResume(Resume r, Object index) {
        storage.set((int) index, r);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return storage.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
