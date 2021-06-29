package ru.topjava.basejava.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }
    public StorageException(String message, Exception exc) {
        this(message, null, exc);
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public StorageException(String message, String uuid, Exception exc) {
        super(message, exc);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}
