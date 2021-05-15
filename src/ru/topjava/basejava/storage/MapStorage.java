package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage<Resume> {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isInStorage(Resume index) {
        return index != null;
    }

    @Override
    protected Resume getIndex(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void saveResume(Resume r, Resume index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResume(Resume index) {
        return index;
    }

    @Override
    protected void deleteResume(Resume index) {
        storage.remove(index.getUuid());
    }

    @Override
    protected void updateResume(Resume r, Resume index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
