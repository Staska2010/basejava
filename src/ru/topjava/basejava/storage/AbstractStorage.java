package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements IStorage {
    @Override
    public void save(Resume r) {
        int index = getSupposedIndex(r.getUuid());
        saveResume(r, index);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(getIndexIfPresent(uuid));
    }

    @Override
    public void delete(String uuid) {
        int index = getIndexIfPresent(uuid);
        deleteResume(index);
    }

    @Override
    public void update(Resume r) {
        int index = getIndexIfPresent(r.getUuid());
        updateResume(r, index);
    }

    private int getIndexIfPresent(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistsStorageException(uuid);
        }
        return index;
    }

    /**
     * @param uuid
     * @return supposed index for element inserting
     * works for derivatives that use the BinarySearch method
     * to find the index to insert element
     */
    private int getSupposedIndex(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            throw new ExistsStorageException(uuid);
        }
        return index;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void saveResume(Resume r, int index);

    protected abstract Resume getResume(int index);

    protected abstract void deleteResume(int index);

    protected abstract void updateResume(Resume r, int index);
}
