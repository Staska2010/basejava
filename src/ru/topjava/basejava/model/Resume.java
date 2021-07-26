package ru.topjava.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    // Unique identifier
    private String uuid;
    private String fullName;

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractRecord> records = new EnumMap<>(SectionType.class);

    public Resume() {
    }

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

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public Map<SectionType, AbstractRecord> getRecords() {
        return records;
    }

    public AbstractRecord getRecord(SectionType type) {
        return records.get(type);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContact(ContactType type, String field) {
        contacts.put(type, field);
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
        int compResult = fullName.compareTo(resume.getFullName());
        return compResult != 0 ? compResult : uuid.compareTo(resume.uuid);
    }
}
