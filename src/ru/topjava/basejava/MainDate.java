package ru.topjava.basejava;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class MainDate {
    public static void main(String[] args) {
        LocalDate dt = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd");
        System.out.println(dtf.format(dt));
        Date date = new Date();
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        System.out.println(clock.millis());
        System.out.println(date.getTime());

        for (String next : ZoneId.getAvailableZoneIds()) {
            System.out.println(next);
        }
        ZoneId zone1 = ZoneId.of("America/El_Salvador");
        ZoneId zone2 = ZoneId.of("Africa/Monrovia");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());
     //   System.out.println(LocalTime.MAX);
        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);
        System.out.println(now1.isBefore(now2));
        System.out.println(ChronoUnit.HOURS.between(now1, now2));
//        for (Locale loc : Locale.getAvailableLocales()) {
//            System.out.println(loc);
//        }

        LocalDate ld = LocalDate.now();
        LocalDate tom = ld.plus(1, ChronoUnit.DAYS);
        LocalDate monp = ld.plus(1, ChronoUnit.MONTHS);
        System.out.println(ld);
        System.out.println(tom);
        System.out.println(monp);

        DateTimeFormatter dtff =
                DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse("24.12.2014", dtff);
        System.out.println(xmas);
        long duration = Duration.between(now1, now2).toHours();
        System.out.println(duration);

        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plus(22, ChronoUnit.DAYS);

        Period period = Period.between(date1, date2);
        System.out.println(period);
    }
}
