package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.utils.ObjectStreamSaver;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamSaver()));
    }
}
