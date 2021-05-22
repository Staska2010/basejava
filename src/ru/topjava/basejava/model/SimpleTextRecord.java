package ru.topjava.basejava.model;

public class SimpleTextRecord extends AbstractRecord {
    private final String simpleText;

    public SimpleTextRecord(String simpleText) {
        this.simpleText = simpleText;
    }

    @Override
    public String toString() {
        return simpleText;
    }
}
