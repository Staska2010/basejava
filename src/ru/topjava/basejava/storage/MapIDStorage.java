package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapIDStorage extends AbstractStorage<String> {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isInStorage(String index) {
        return storage.containsKey(index);
    }

    @Override
    protected String getIndex(String uuid) {
        return uuid;
    }

    @Override
    protected void saveResume(Resume r, String insertPosition) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResume(String index) {
        return storage.get(index);
    }

    @Override
    protected void deleteResume(String index) {
        storage.remove(index);
    }

    @Override
    protected void updateResume(Resume r, String index) {
        storage.replace(index, r);
    }

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
