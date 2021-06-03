package ru.topjava.basejava.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Position implements Serializable {
    private final LocalDate dateStart;
    private final LocalDate dateEnd;
    private final String position;
    private final String jobDesc;

    public Position(LocalDate dateStart, LocalDate dateEnd, String position, String jobDesc) {
        this.dateStart = Objects.requireNonNull(dateStart);
        this.dateEnd = Objects.requireNonNull(dateEnd);
        this.position = Objects.requireNonNull(position);
        this.jobDesc = jobDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position1 = (Position) o;

        if (!dateStart.equals(position1.dateStart)) return false;
        if (!dateEnd.equals(position1.dateEnd)) return false;
        if (!position.equals(position1.position)) return false;
        return Objects.equals(jobDesc, position1.jobDesc);
    }

    @Override
    public int hashCode() {
        int result = dateStart.hashCode();
        result = 31 * result + dateEnd.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (jobDesc != null ? jobDesc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return dateStart + " - "
                + dateEnd + System.lineSeparator()
                + position + System.lineSeparator()
                + jobDesc + System.lineSeparator();
    }
}
