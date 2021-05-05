package ru.topjava.basejava.exception;

public class ExistsStorageException extends StorageException {

    public ExistsStorageException(String uuid) {
        super("Resume " + uuid + " already exists", uuid);
    }
}
