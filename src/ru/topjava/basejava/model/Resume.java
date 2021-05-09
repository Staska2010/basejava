package ru.topjava.basejava.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private String uuid;
    private String fullName;

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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return uuid + ": " + fullName;
    }

    // TODO Check in future: uuid.equalsIgnoreCase(resume.uuid) && fullName.equalsIgnoreCase(resume.fullName)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    /**
     * Bloch's standard receipt for getting hash-code.
     *
     * @return hash for Resume object
     */
    //TODO  String fields would be case-sensitive
    //     * equalsIgnoreCase in overridden equals method works in toLowerCase way
    //     *
    //     *   int result = Objects.hash(uuid.toLowerCase(Locale.ROOT));
    //     *   result = 31 * result + Objects.hashCode(fullName.toLowerCase(Locale.ROOT));
    //     *
    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    /**
     * If UUID's are equal, compare fullName fields.
     * otherwise, return the result of ID's comparison
     *
     * @param resume Resume object to compare
     * @return result of Resume comparison
     */
    //TODO CHECK: compareToIgnoreCase
    @Override
    public int compareTo(Resume resume) {
        if (uuid.equals(resume.uuid)) {
            return fullName.compareTo(resume.fullName);
        }
        return uuid.compareTo(resume.uuid);
    }
}
