package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {
    @Override
    public void writeObject(Resume r, OutputStream destination) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(destination)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            dos.writeInt(r.getContacts().size());
            for (Map.Entry<ContactType, String> next : r.getContacts().entrySet()) {
                dos.writeUTF(next.getKey().name());
                dos.writeUTF(next.getValue());
            }
            dos.writeInt(r.getRecords().size());
            for (Map.Entry<SectionType, AbstractRecord> next : r.getRecords().entrySet()) {
                dos.writeUTF(next.getKey().name());
                switch (next.getKey()) {
                    case PERSONAL:
                    case OBJECTIVES: {
                        dos.writeUTF(next.getValue().toString());
                        break;
                    }
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS: {
                        List<String> list = ((BulletedListRecord) next.getValue()).getBulletedRecords();
                        dos.writeInt(list.size());
                        for (String listRecord : list) {
                            dos.writeUTF(listRecord);
                        }
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> list = ((OrganizationListRecord) next.getValue()).getOrganizations();
                        dos.writeInt(list.size());
                        for (Organization nextOrg : list) {
                            dos.writeUTF(nextOrg.getHomePage().getName());
                            dos.writeUTF(nextOrg.getHomePage().getUrl());
                            List<Organization.Position> positions = nextOrg.getPositions();
                            dos.writeInt(positions.size());
                            for (Organization.Position position : positions) {
                                dos.writeUTF(position.getDateStart().toString());
                                dos.writeUTF(position.getDateEnd().toString());
                                dos.writeUTF(position.getPosition());
                                dos.writeUTF(position.getJobDesc());
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Resume readObject(InputStream resource) throws IOException {
        try (DataInputStream dis = new DataInputStream(resource)) {
            Resume result = new Resume(dis.readUTF(), dis.readUTF());
            int sizeOfList = dis.readInt();
            for (int i = 0; i < sizeOfList; i++) {
                result.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            sizeOfList = dis.readInt();
            for (int i = 0; i < sizeOfList; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVES: {
                        result.setRecord(sectionType, new SimpleTextRecord(dis.readUTF()));
                    }
                    break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS: {
                        int sizeOfInnerList = dis.readInt();
                        List<String> innerList = new ArrayList<>();
                        for (int j = 0; j < sizeOfInnerList; j++) {
                            innerList.add(dis.readUTF());
                        }
                        result.setRecord(sectionType, new BulletedListRecord(innerList));
                    }
                    break;
                    case EXPERIENCE:
                    case EDUCATION: {
                        int innerSize = dis.readInt();
                        List<Organization> organizations = new ArrayList<>();
                        for (int j = 0; j < innerSize; j++) {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            int sizePositions = dis.readInt();
                            List<Organization.Position> positions = new ArrayList<>();
                            for (int k = 0; k < sizePositions; k++) {
                                positions.add(new Organization.Position(
                                        LocalDate.parse(dis.readUTF()),
                                        LocalDate.parse(dis.readUTF()),
                                        dis.readUTF(),
                                        dis.readUTF()
                                ));
                            }
                            organizations.add(new Organization(name, url, positions));
                        }
                        result.setRecord(sectionType, new OrganizationListRecord(organizations));
                    }
                    break;
                }
            }
            return result;
        }
    }

//    private void getMethods(@NotNull Class aClass, DataOutputStream dos, Object resume) throws IOException {
//        List<Method> methods = Arrays.stream(aClass.getDeclaredMethods())
//                .filter(x -> x.getName().startsWith("get")).filter(x -> x.isAnnotationPresent(DataStream.class))
//                .collect(Collectors.toList());
//        for (Method next : methods) {
//            try {
//                if (next.invoke(resume).getClass() == LocalDate.class) {
//                    dos.writeUTF(((LocalDate) next.invoke(resume)).toString());
//                } else {
//                    dos.writeUTF(next.invoke(resume).toString());
//                }
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
