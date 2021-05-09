package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage implements IStorage {
    @Override
    public void save(Resume r) {
        Object insertPosition = getSupposedIndex(r.getUuid());
        saveResume(r, insertPosition);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(getIndexIfPresent(uuid));
    }

    @Override
    public void delete(String uuid) {
        Object index = getIndexIfPresent(uuid);
        deleteResume(index);
    }

    @Override
    public void update(Resume r) {
        Object index = getIndexIfPresent(r.getUuid());
        updateResume(r, index);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> result = getAll();
        Collections.sort(result);
        return result;
    }

    private Object getIndexIfPresent(String uuid) {
        Object index = getIndex(uuid);
        if (!isInStorage(index)) {
            throw new NotExistsStorageException(uuid);
        }
        return index;
    }

    /**
     * @param uuid Resume record ID
     * @return index for index-based storages.
     * * For derivatives that use the BinarySearch method
     * * to find the index to insert element it returns supposed index.
     */
    private Object getSupposedIndex(String uuid) {
        Object index = getIndex(uuid);
        if (isInStorage(index)) {
            throw new ExistsStorageException(uuid);
        }
        return index;
    }

    protected abstract boolean isInStorage(Object index);

    protected abstract Object getIndex(String uuid);

    protected abstract void saveResume(Resume r, Object insertPosition);

    protected abstract Resume getResume(Object index);

    protected abstract void deleteResume(Object index);

    protected abstract void updateResume(Resume r, Object index);

    protected abstract List<Resume> getAll();
}
