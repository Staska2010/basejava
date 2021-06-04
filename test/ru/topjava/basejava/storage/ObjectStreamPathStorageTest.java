package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.utils.ObjectStreamSaver;

import java.nio.file.Path;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(Path.of(STORAGE_DIR.getPath()), new ObjectStreamSaver()));
    }
}
