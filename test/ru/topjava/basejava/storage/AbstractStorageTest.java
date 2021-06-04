package ru.topjava.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.topjava.basejava.ResumeTestData;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected final static File STORAGE_DIR = new File("C:\\Users\\user\\Documents\\basejava\\storage");
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
        RESUME_1 = ResumeTestData.fillResume(UUID_1, "1");
        RESUME_2 = ResumeTestData.fillResume(UUID_2, "2");
        RESUME_3 = ResumeTestData.fillResume(UUID_3, "3");
        RESUME_4 = ResumeTestData.fillResume(UUID_4, "4");
    }

    AbstractStorageTest(IStorage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_2);
        storage.save(RESUME_1);
        storage.save(RESUME_3);
    }

    @Test
    public void ifGetSizeThenSizeIsThree() {
        assertEquals(3, storage.size());
    }

    @Test
    public void ifGetExistedResumeThenInstancesAreCorrect() {
        assertEquals(RESUME_1, storage.get(UUID_1));
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
    //TODO implement getting a sorted list
    public void ifGetAllSortedThenNumberAndInstancesAreCorrect() {
        List<Resume> actualResumes = storage.getAllSorted();
        assertEquals(3, actualResumes.size());
        assertEquals(Arrays.asList(RESUME_1, RESUME_2, RESUME_3), actualResumes);
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
        storage.update(RESUME_3);
        System.out.println(RESUME_3);
        System.out.println(storage.get(UUID_3));
        assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test(expected = NotExistsStorageException.class)
    public void ifUpdateNotExistingResumeThenException() {
        storage.update(RESUME_4);
    }
}