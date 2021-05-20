package ru.topjava.basejava.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

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
        return uuid + ": " + fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid)
                && fullName.equals(resume.fullName)
                && Objects.equals(this.contacts, resume.contacts)
                && Objects.equals(this.records, resume.records);
    }

    /**
     * Bloch's standard receipt for getting hash-code.
     *
     * @return hash for Resume object
     */
    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + Objects.hashCode(contacts);
        result = 31 * result + Objects.hashCode(records);
        return result;
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
