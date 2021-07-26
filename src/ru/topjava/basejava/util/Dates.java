package ru.topjava.basejava.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class Dates {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    private Dates() {
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseDate(String date) {
        YearMonth ym = YearMonth.parse(date, formatter);
        return LocalDate.of(ym.getYear(), ym.getMonth(), 1);
    }
}