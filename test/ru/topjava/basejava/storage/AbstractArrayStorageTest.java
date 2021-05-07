package ru.topjava.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import static ru.topjava.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    public AbstractArrayStorageTest(IStorage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void ifNotEnoughSpaceToSaveResumeThenException() {
        try {
            for (int i = storage.size(); i < STORAGE_LIMIT; i++) {
                storage.save(new Resume(""));
            }
        } catch (StorageException e) {
            Assert.fail("The exception popped ahead of time");
        }
        storage.save(new Resume(""));
    }
}
