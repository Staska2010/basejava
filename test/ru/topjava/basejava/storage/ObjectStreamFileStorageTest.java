package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.utils.ObjectStreamSaver;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {
    public ObjectStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSaver()));
    }
}
