package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements IStorage {
    private static final Logger LOGGER = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void save(Resume r) {
        LOGGER.info("Save " + r);
        T insertPosition = getSupposedIndex(r.getUuid());
        saveResume(r, insertPosition);
    }

    @Override
    public Resume get(String uuid) {
        LOGGER.info("Get " + uuid);
        return getResume(getIndexIfPresent(uuid));
    }

    @Override
    public void delete(String uuid) {
        LOGGER.info("Delete " + uuid);
        T index = getIndexIfPresent(uuid);
        deleteResume(index);
    }

    @Override
    public void update(Resume r) {
        LOGGER.info("Update " + r);
        T index = getIndexIfPresent(r.getUuid());
        updateResume(r, index);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOGGER.info("getAllSorted");
        List<Resume> result = getAll();
        Collections.sort(result);
        return result;
    }

    private T getIndexIfPresent(String uuid) {
        T index = getIndex(uuid);
        if (!isInStorage(index)) {
            LOGGER.warning("Resume " + uuid + " not exists");
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
    private T getSupposedIndex(String uuid) {
        T index = getIndex(uuid);
        if (isInStorage(index)) {
            LOGGER.warning("Resume " + uuid + " already exists");
            throw new ExistsStorageException(uuid);
        }
        return index;
    }

    protected abstract boolean isInStorage(T index);

    protected abstract T getIndex(String uuid);

    protected abstract void saveResume(Resume r, T insertPosition);

    protected abstract Resume getResume(T index);

    protected abstract void deleteResume(T index);

    protected abstract void updateResume(Resume r, T index);

    protected abstract List<Resume> getAll();
}
