package ru.topjava.basejava.model;

import java.util.List;
import java.util.Objects;

public class BulletedListRecord extends AbstractRecord {
    private final List<String> bulletedRecords;

    public BulletedListRecord(List<String> records) {
        bulletedRecords = Objects.requireNonNull(records, "Bulleted records list is empty");
    }

    public List<String> getBulletedRecords() {
        return bulletedRecords;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String record : bulletedRecords) {
            sb.append(" + ").append(record).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BulletedListRecord that = (BulletedListRecord) o;

        return Objects.equals(bulletedRecords, that.bulletedRecords);
    }

    @Override
    public int hashCode() {
        return bulletedRecords != null ? bulletedRecords.hashCode() : 0;
    }
}
