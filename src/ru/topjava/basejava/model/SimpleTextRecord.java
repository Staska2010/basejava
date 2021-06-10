package ru.topjava.basejava.model;

import java.util.Objects;

public class SimpleTextRecord extends AbstractRecord {
    private static final long serialVersionUID = 1L;
    private String simpleText;

    public SimpleTextRecord() {
    }

    public SimpleTextRecord(String simpleText) {
        this.simpleText = Objects.requireNonNull(simpleText, "Text record is null");
    }

    @Override
    public String toString() {
        return simpleText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTextRecord that = (SimpleTextRecord) o;
        return Objects.equals(simpleText, that.simpleText);
    }

    @Override
    public int hashCode() {
        return simpleText.hashCode();
    }
}
