package ru.topjava.basejava.storage;

import ru.topjava.basejava.Config;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}
