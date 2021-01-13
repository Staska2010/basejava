package ru.topjava.basejava.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.*;
import static ru.topjava.basejava.storage.AbstractStorage.STORAGE_LIMIT;

public abstract class AbstractStorageTest {

    private IStorage storage;
    Resume r1 = new Resume("uuid1");
    Resume r2 = new Resume("uuid2");
    Resume r3 = new Resume("uuid3");
    Resume r4 = new Resume("uuid4");

    AbstractStorageTest(IStorage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @After
    public void tearDown() {
        storage.clear();
    }

    @Test
    public void ifGetSizeThenSizeIsThree() {
        assertEquals(3, storage.size());
    }

    @Test
    public void ifGetExistedResumeThenInstancesAreCorrect() {
        assertSame(storage.get("uuid1"), r1);
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistingResumeThenException() {
        storage.get("dummy");
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistedThenException() {
        storage.get("dummy");
    }

    @Test
    public void ifGetAllThenNumberAndInstancesAreCorrect() {
        Resume[] actualResumes = storage.getAll();
        assertEquals(3, storage.size());
        assertEquals(r1, actualResumes[0]);
        assertEquals(r2, actualResumes[1]);
        assertEquals(r3, actualResumes[2]);
    }

    @Test
    public void ifClearThenSizeIsZero() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void ifCorrectlySavedThenResumeIsInStorage() {
        storage.save(r4);
        assertEquals(4, storage.size());
        assertEquals(r4, storage.get(r4.getUuid()));
    }

    @Test(expected = StorageException.class)
    public void ifNotEnoughSpaceToSaveResumeThenException() {
        try {
            for (int i = storage.size(); i < STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("The exception popped ahead of time");
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifDeleteNotExistingThenException() {
        storage.delete("dummy");
    }

    @Test
    public void ifDeleteExistingItemThenSizeIsTwo() {
        storage.delete(r2.getUuid());
        assertEquals(2, storage.size());
    }

    @Test
    public void ifUpdateExistingResumeThenTrue() {
        Resume updateResume = new Resume("uuid3");
        storage.update(updateResume);
        assertEquals(updateResume, storage.get("uuid3"));
    }

    @Test
    public void ifUpdateNotExistingResumeThenException() {
        Resume updateResume = new Resume("uuid3");
        storage.update(updateResume);
        assertEquals(updateResume, storage.get("uuid3"));
    }
}