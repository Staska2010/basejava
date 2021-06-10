package ru.topjava.basejava.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    // Unique identifier
    private final String uuid;
    private final String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractRecord> records = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = Objects.requireNonNull(uuid);
        this.fullName = Objects.requireNonNull(fullName);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public void setContact(ContactType type, String field) {
        contacts.put(type, field);
    }

    public Map<SectionType, AbstractRecord> getRecords() {
        return records;
    }

    public void setRecord(SectionType type, AbstractRecord abstractRecord) {
        records.put(type, abstractRecord);
    }

    @Override
    public String toString() {
        return uuid + ": " + fullName + System.lineSeparator()
                + contacts.toString() + System.lineSeparator()
                + records.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(records, resume.records);
    }

    /**
     * Bloch's standard receipt for getting hash-code.
     *
     * @return hash for Resume object
     */
    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, records);
    }

    /**
     * If UUID's are equal, compare fullName fields.
     * otherwise, return the result of ID's comparison
     *
     * @param resume Resume object to compare
     * @return result of Resume comparison
     */
    @Override
    public int compareTo(Resume resume) {
        return uuid.equals(resume.uuid) ? fullName.compareTo(resume.fullName) : uuid.compareTo(resume.uuid);
    }
}
