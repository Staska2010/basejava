package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.utils.ObjectStreamSaver;

public class ObjectStreamStorageTest extends AbstractStorageTest {
    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR, new ObjectStreamSaver()));
    }
}
