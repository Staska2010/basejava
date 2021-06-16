package ru.topjava.basejava.storage.strategy;

import ru.topjava.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            for (Map.Entry<ContactType, String> nextContact : r.getContacts().entrySet()) {
                dos.writeUTF(nextContact.getKey().name());
                dos.writeUTF(nextContact.getValue());
            }
            dos.writeInt(r.getRecords().size());
            for (Map.Entry<SectionType, AbstractRecord> nextRecord : r.getRecords().entrySet()) {
                dos.writeUTF(nextRecord.getKey().name());
                switch (nextRecord.getKey()) {
                    case PERSONAL:
                    case OBJECTIVES: {
                        dos.writeUTF(((SimpleTextRecord) nextRecord.getValue()).getSimpleText());
                        break;
                    }
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS: {
                        List<String> list = ((BulletedListRecord) nextRecord.getValue()).getBulletedRecords();
                        dos.writeInt(list.size());
                        for (String listRecord : list) {
                            dos.writeUTF(listRecord);
                        }
                        break;
                    }
                    case EXPERIENCE:
                    case EDUCATION: {
                        List<Organization> list = ((OrganizationListRecord) nextRecord.getValue()).getOrganizations();
                        dos.writeInt(list.size());
                        for (Organization nextOrg : list) {
                            Link homePage = nextOrg.getHomePage();
                            dos.writeUTF(homePage.getName());
                            String url = homePage.getUrl();
                            dos.writeUTF(url != null ? url : "");
                            List<Organization.Position> positions = nextOrg.getPositions();
                            dos.writeInt(positions.size());
                            for (Organization.Position position : positions) {
                                dos.writeUTF(dateFormat(position.getDateStart()));
                                dos.writeUTF(dateFormat(position.getDateEnd()));
                                dos.writeUTF(position.getPosition());
                                String jobDesc = position.getJobDesc();
                                dos.writeUTF(jobDesc != null ? jobDesc : "");
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
                            if (url.equals("")) {
                                url = null;
                            }
                            int sizePositions = dis.readInt();
                            List<Organization.Position> positions = new ArrayList<>();
                            for (int k = 0; k < sizePositions; k++) {
                                String jobDesc;
                                positions.add(new Organization.Position(
                                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yy/MM/dd")),
                                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yy/MM/dd")),
                                        dis.readUTF(),
                                        (jobDesc = dis.readUTF()).equals("") ? null : jobDesc
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

    private String dateFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yy/MM/dd"));
    }


//*    Alternative way through reflection/in progress/just for history *//
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
