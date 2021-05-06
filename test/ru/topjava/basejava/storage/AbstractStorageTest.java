package ru.topjava.basejava.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    protected IStorage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = new Resume(UUID_1);
        RESUME_2 = new Resume(UUID_2);
        RESUME_3 = new Resume(UUID_3);
        RESUME_4 = new Resume(UUID_4);
    }

    AbstractStorageTest(IStorage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
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
        assertSame(RESUME_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistingResumeThenException() {
        storage.get(UUID_4);
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifGetNotExistedThenException() {
        storage.get(UUID_4);
    }

    @Test
    public void ifGetAllThenNumberAndInstancesAreCorrect() {
        Resume[] actualResumes = storage.getAll();
        Arrays.sort(actualResumes);
        assertEquals(3, storage.size());
        assertEquals(RESUME_1, actualResumes[0]);
        assertEquals(RESUME_2, actualResumes[1]);
        assertEquals(RESUME_3, actualResumes[2]);
    }

    @Test
    public void ifClearThenSizeIsZero() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void ifCorrectlySavedThenResumeIsInStorage() {
        storage.save(RESUME_4);
        assertEquals(4, storage.size());
        assertEquals(RESUME_4, storage.get(UUID_4));
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifDeleteNotExistingThenException() {
        storage.delete(UUID_4);
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifDeleteExistingItemThenExceptionWhileGet() {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
        storage.get(UUID_2);
    }

    @Test
    public void ifUpdateExistingResumeThenTrue() {
        Resume updateResume = new Resume(UUID_3);
        storage.update(updateResume);
        assertEquals(updateResume, storage.get(UUID_3));
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifUpdateNotExistingResumeThenException() {
        Resume updateResume = new Resume(UUID_4);
        storage.update(updateResume);
    }
}