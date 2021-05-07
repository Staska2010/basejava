package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

public class MapIDStorage extends MapStorage {
    @Override
    protected boolean isInStorage(Object index) {
        return storage.containsKey((String) index);
    }

    @Override
    protected String getIndex(String uuid) {
        return uuid;
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((String) index);
    }

    @Override
    protected void deleteResume(Object index) {
        storage.remove((String) index);
    }
}
