package ru.topjava.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private final Link homePage;
    private final LocalDate dateStart;
    private final LocalDate dateEnd;
    private final String position;
    private final String jobDesc;

    public Organization(String name, String url, LocalDate dateStart, LocalDate dateEnd, String position, String jobDesc) {
        this.homePage = new Link(name, url);
        this.dateStart = Objects.requireNonNull(dateStart, "dateStart is null!");
        this.dateEnd = Objects.requireNonNull(dateEnd, "dateEnd is null!");
        this.position = Objects.requireNonNull(position, "position is null!");
        this.jobDesc = jobDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        if (!dateStart.equals(that.dateStart)) return false;
        if (!dateEnd.equals(that.dateEnd)) return false;
        if (!position.equals(that.position)) return false;
        return jobDesc != null ? jobDesc.equals(that.jobDesc) : that.jobDesc == null;
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + dateStart.hashCode();
        result = 31 * result + dateEnd.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (jobDesc != null ? jobDesc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(homePage)
                .append(System.lineSeparator())
                .append(dateStart).append(" - ").append(dateEnd).append(System.lineSeparator())
                .append(position).append(System.lineSeparator())
                .append(jobDesc)
                .append(System.getProperty(System.lineSeparator()));
        return sb.toString();
    }
}
