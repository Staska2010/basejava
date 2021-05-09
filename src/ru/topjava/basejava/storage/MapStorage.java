package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isInStorage(Object index) {
        return index != null;
    }

    @Override
    protected Object getIndex(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void saveResume(Resume r, Object index) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume getResume(Object index) {
        return (Resume) index;
    }

    @Override
    protected void deleteResume(Object index) {
        Resume resumeToDelete = (Resume) index;
        storage.remove(resumeToDelete.getUuid());
    }

    @Override
    protected void updateResume(Resume r, Object index) {
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
