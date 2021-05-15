package ru.topjava.basejava.model;

import java.time.LocalDate;

public class Organization {
    private String companyName;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String position;
    private String jobDesc;

    public Organization(String companyName, LocalDate dateStart, LocalDate dateEnd, String position, String jobDesc) {
        this.companyName = companyName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.position = position;
        this.jobDesc = jobDesc;
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
