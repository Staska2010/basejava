package ru.topjava.basejava.exception;

public class NotExistsStorageException extends StorageException {
    public NotExistsStorageException(String uuid) {
        super("Resume " + uuid + "not exists", uuid);
    }
}
