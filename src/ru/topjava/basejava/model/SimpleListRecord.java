package ru.topjava.basejava.model;

public class SimpleListRecord extends ListRecord {
    private final String simpleText;

    public SimpleListRecord(String simpleText) {
        this.simpleText = simpleText;
    }

    @Override
    public String toString() {
        return simpleText;
    }
}
