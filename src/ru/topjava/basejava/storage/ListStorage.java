package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> storage = new LinkedList<>();

    @Override
    protected boolean isInStorage(Integer index) {
        return index >= 0;
    }

    @Override
    public Integer getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void saveResume(Resume r, Integer index) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(Integer index) {
        return storage.get(index);
    }

    @Override
    protected void deleteResume(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    protected void updateResume(Resume r, Integer index) {
        storage.set(index, r);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
