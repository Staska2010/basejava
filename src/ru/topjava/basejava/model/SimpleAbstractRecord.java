package ru.topjava.basejava.model;

public class SimpleAbstractRecord extends AbstractRecord {
    private final String simpleText;

    public SimpleAbstractRecord(String simpleText) {
        this.simpleText = simpleText;
    }

    @Override
    public String toString() {
        return simpleText;
    }
}
