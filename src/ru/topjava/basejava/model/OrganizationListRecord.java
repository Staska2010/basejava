package ru.topjava.basejava.model;

import java.util.List;
import java.util.Objects;

public class OrganizationListRecord extends AbstractRecord {
    private final List<Organization> organizations;

    public OrganizationListRecord(List<Organization> records) {
        organizations = Objects.requireNonNull(records, "Empty organization list");
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization record : organizations) {
            sb.append(" + ").append(record).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
