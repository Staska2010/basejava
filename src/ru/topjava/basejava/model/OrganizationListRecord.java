package ru.topjava.basejava.model;

import java.util.List;

public class OrganizationListRecord extends AbstractRecord {
    private final List<Organization> organizations;

    public OrganizationListRecord(List<Organization> records) {
        organizations = records;
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
