package ru.topjava.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private final String companyName;
    private final LocalDate dateStart;
    private final LocalDate dateEnd;
    private final String position;
    private final String jobDesc;

    public Organization(String companyName, LocalDate dateStart, LocalDate dateEnd, String position, String jobDesc) {
        this.companyName = companyName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.position = position;
        this.jobDesc = jobDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return companyName.equals(that.companyName) && dateStart.equals(that.dateStart) && dateEnd.equals(that.dateEnd) && position.equals(that.position) && jobDesc.equals(that.jobDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, dateStart, dateEnd, position, jobDesc);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(companyName)
                .append(System.lineSeparator())
                .append(dateStart).append(" - ").append(dateEnd).append(System.lineSeparator())
                .append(position).append(System.lineSeparator())
                .append(jobDesc)
                .append(System.getProperty(System.lineSeparator()));
        return sb.toString();
    }
}
