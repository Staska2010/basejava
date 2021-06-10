package ru.topjava.basejava.model;

import ru.topjava.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private Link homePage;
    private List<Position> positions;

    public Organization() {
    }

    public Organization(String name, String url, List<Position> positions) {
        this.homePage = new Link(name, url);
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        return positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(homePage)
                .append(System.lineSeparator())
                .append(positions)
                .append(System.lineSeparator());
        return sb.toString();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dateStart;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dateEnd;
        private String position;
        private String jobDesc;

        public Position() {
        }

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
}
