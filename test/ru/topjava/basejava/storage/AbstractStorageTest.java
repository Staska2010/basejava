package ru.topjava.basejava.storage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.lang.reflect.Field;

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
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @After
    public void tearDown() {
        storage = null;
    }

    @Test
    public void ifGetSizeThenSizeIsThree() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void ifGetExistedResumeThenInstancesAreCorrect() {
        Assert.assertSame(storage.get("uuid1"), r1);
        Assert.assertSame(storage.get("uuid3"), r3);
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistedThenException() {
        storage.get("dummy");
    }

    @Test
    public void ifGetAllThenNumberAndInstancesAreCorrect() {
        Resume[] tempArray = storage.getAll();
        Assert.assertEquals(3, storage.size());
        Assert.assertEquals(r1, tempArray[0]);
        Assert.assertEquals(r2, tempArray[1]);
        Assert.assertEquals(r3, tempArray[2]);
    }

    @Test
    public void ifClearThenSizeIsZero() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void ifCorrectlySavedThenResumeIsInStorage() {
        storage.save(r4);
        Assert.assertEquals(4, storage.size());
        Assert.assertEquals(r4, storage.get(r4.getUuid()));
    }

    @Test(expected = StorageException.class)
    public void ifNotEnoughSpaceToSaveResumeThenException() {
        Class superclass = storage.getClass().getSuperclass();
        int sizeOfStorage = 0;
        try {
            Field field = superclass.getDeclaredField("STORAGE_LIMIT");
            sizeOfStorage = field.getInt(storage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            for (int i = storage.size(); i < sizeOfStorage; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("The exception popped ahead of time");
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifDeleteNotExistingThenException() {
        storage.delete(r2.getUuid());
        Assert.assertEquals(2, storage.size());
        storage.get(r2.getUuid());
    }

    @Test
    public void ifUpdateExistingResumeThenTrue() {
        Resume updateResume = new Resume("uuid3");
        storage.update(updateResume);
        Assert.assertEquals(updateResume, storage.get("uuid3"));
    }

    @Test
    public void ifUpdateNotExistingResumeThenException() {
        Resume updateResume = new Resume("uuid3");
        storage.update(updateResume);
        Assert.assertEquals(updateResume, storage.get("uuid3"));
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistingResumeThenException() {
        storage.get("dummy");
    }
}