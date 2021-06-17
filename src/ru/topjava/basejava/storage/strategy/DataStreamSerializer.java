package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.LocalDate.parse;

public class DataStreamSerializer implements Serializer {
    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(destination)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Set<Map.Entry<ContactType, String>> contacts = r.getContacts().entrySet();
            dos.writeUTF(String.valueOf(contacts.size()));
            writeWithException(contacts, contact -> {
                dos.writeUTF(contact.getKey().name());
                dos.writeUTF(contact.getValue());
            });
            Map<SectionType, AbstractRecord> records = r.getRecords();
            dos.writeUTF(String.valueOf(records.size()));
            SectionIterator it = new SectionIterator(records);
            while (it.hasNext()) {
                List<String> nextRecord = it.next();
                writeWithException(nextRecord, dos::writeUTF);
            }
        }
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        try (DataInputStream dis = new DataInputStream(resource)) {
            Resume result = new Resume(dis.readUTF(), dis.readUTF());
            readWithException(dis, () -> result.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVES:
                        result.setRecord(sectionType, new SimpleTextRecord(dis.readUTF()));
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        result.setRecord(sectionType, new BulletedListRecord(readSection(dis, dis::readUTF)));
                        break;
                    case EXPERIENCE:
                    case EDUCATION: {
                        result.setRecord(sectionType, new OrganizationListRecord(readSection(dis, () -> {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            if (url.equals("")) {
                                url = null;
                            }
                            Organization next = new Organization(name, url,
                                    readSection(dis, () -> {
                                        String jobDesc;
                                        return new Organization.Position(
                                                parse(dis.readUTF(), DateTimeFormatter.ofPattern("yy/MM/dd")),
                                                parse(dis.readUTF(), DateTimeFormatter.ofPattern("yy/MM/dd")),
                                                dis.readUTF(),
                                                (jobDesc = dis.readUTF()).equals("") ? null : jobDesc
                                        );
                                    }));
                            return next;
                        })));
                    }
                    break;
                }
            });
            return result;
        }
    }

    private interface DataWriteable<T> {
        void handle(T record) throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataWriteable<T> handler) throws IOException {
        for (T next : collection) {
            handler.handle(next);
        }
    }

    private interface DataReadable<T> {
        void handle() throws IOException;
    }

    private <T> void readWithException(DataInputStream dis, DataReadable<T> handler) throws IOException {
        int size = Integer.parseInt(dis.readUTF());
        for (int i = 0; i < size; i++) {
            handler.handle();
        }
    }

    private interface Readable<T> {
        T get() throws IOException;
    }

    private <T> List<T> readSection(DataInputStream dis, Readable<T> reader) throws IOException {
        int size = Integer.parseInt(dis.readUTF());
        List<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(reader.get());
        }
        return result;
    }

    private String dateFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yy/MM/dd"));
    }

    private class SectionIterator implements Iterator<List<String>> {
        private final Map<SectionType, AbstractRecord> records;
        Iterator<Map.Entry<SectionType, AbstractRecord>> mapIterator;

        SectionIterator(Map<SectionType, AbstractRecord> records) {
            this.records = records;
            mapIterator = records.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return mapIterator.hasNext();
        }

        @Override
        public List<String> next() {
            List<String> result = new ArrayList<>();
            Map.Entry<SectionType, AbstractRecord> record = mapIterator.next();
            SectionType type = record.getKey();
            switch (type) {
                case PERSONAL:
                case OBJECTIVES: {
                    result.add(type.name());
                    result.add(((SimpleTextRecord) records.get(type)).getSimpleText());
                    break;
                }
                case ACHIEVEMENTS:
                case QUALIFICATIONS: {
                    result.add(type.name());
                    List<String> bulletedRecords = (((BulletedListRecord) records.get(type))
                            .getBulletedRecords());
                    result.add(String.valueOf(bulletedRecords.size()));
                    result.addAll(bulletedRecords);
                    break;
                }
                case EXPERIENCE:
                case EDUCATION: {
                    result.add(type.name());
                    List<Organization> organizations = ((OrganizationListRecord) records.get(type))
                            .getOrganizations();
                    result.add(String.valueOf(organizations.size()));
                    for (Organization organization : organizations) {
                        Link homePage = organization.getHomePage();
                        result.add(homePage.getName());
                        String url = homePage.getUrl();
                        result.add(url != null ? url : "");
                        List<Organization.Position> positions = organization.getPositions();
                        result.add(String.valueOf(positions.size()));
                        for (Organization.Position position : positions) {
                            result.add(dateFormat(position.getDateStart()));
                            result.add(dateFormat(position.getDateEnd()));
                            result.add(position.getPosition());
                            String jobDesc = position.getJobDesc();
                            result.add(jobDesc != null ? jobDesc : "");
                        }
                    }
                    break;
                }
            }
            return result;
        }
    }
}