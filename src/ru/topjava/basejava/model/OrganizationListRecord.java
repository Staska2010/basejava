package ru.topjava.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationListRecord extends AbstractRecord {
    private static final long serialVersionUID = 1L;
    private List<Organization> organizations;

    public OrganizationListRecord() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationListRecord that = (OrganizationListRecord) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }
}
