package ru.topjava.basejava.model;

import java.util.List;

public class BulletedListRecord extends ListRecord {
    private final List<String> bulletedRecords;

    public BulletedListRecord(List<String> records) {
        bulletedRecords = records;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String record : bulletedRecords) {
            sb.append(" + ").append(record).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
