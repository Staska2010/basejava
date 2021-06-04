package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.strategy.ObjectStreamSerializer;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamSerializer()));
    }
}
