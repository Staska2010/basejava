package ru.topjava.basejava;

import ru.topjava.basejava.model.*;

import java.time.LocalDate;
import java.util.List;

public class ResumeTestData {
    public static Resume makeTestResume(String uuid, String fullName) {
        Resume testResume = new Resume(uuid, fullName);
        testResume.setContact(ContactType.EMAIL, "gkislin@yandex.ru");
        testResume.setContact(ContactType.GITHUB, "https://github.com/gkislin");
        testResume.setContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        testResume.setContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        testResume.setContact(ContactType.HOMEPAGE, "http://gkislin.ru/");
        testResume.setRecord(SectionType.OBJECTIVES,
                new SimpleTextRecord("Ведущий стажировок и корпоративного обучения по " +
                        "Java Web и Enterprise технологиям"));
        testResume.setRecord(SectionType.PERSONAL,
                new SimpleTextRecord("Аналитический склад ума, сильная логика, креативность, " +
                        "инициативность. Пурист кода и архитектуры."));
        testResume.setRecord(SectionType.ACHIEVEMENTS, new BulletedListRecord(List.of(
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\"",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. ",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC," +
                        " GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов"
        )));
        testResume.setRecord(SectionType.QUALIFICATIONS, new BulletedListRecord(List.of(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle",
                "MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy"
        )));
        testResume.setRecord(SectionType.EXPERIENCE, new OrganizationListRecord(List.of(
                new Organization("Java Online Projects",
                        "javaops.ru",
                        List.of(new Position(LocalDate.of(2013, 10, 1),
                                LocalDate.now(),
                                "Автор проекта",
                                "Создание, организация и проведение Java онлайн проектов и стажировок"))),
                new Organization("Wrike", "www.wrike.com",
                        List.of(new Position(LocalDate.of(2014, 10, 1),
                                LocalDate.of(2016, 01, 01),
                                "Старший разработчик (backend)",
                                "Проектирование и разработка онлайн платформы управления проектами Wrike " +
                                        "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis).")))
        )));
        return testResume;
    }

    private static void printData(Resume testResume) {
        System.out.println(testResume.getFullName());
        outputContacts(testResume);
        outputRecords(testResume);
    }

    private static void outputContacts(Resume resume) {
        resume.getContacts().forEach((key, value) -> System.out.println(key.getTitle() + " : " + value));
    }

    private static void outputRecords(Resume testResume) {
        testResume.getRecords().forEach((key, value) -> System.out.println(key.getTitle() + System.lineSeparator() + value));
    }
}
