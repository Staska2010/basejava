package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.*;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SqlStorage implements IStorage {
    private static final Logger logger = Logger.getLogger(SqlStorage.class.getName());
    SqlHelper helper;

    public SqlStorage(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Error while loading DB driver", e);
        }
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        logger.info("Clear database");
        helper.executeStatement("DELETE FROM resume", PreparedStatement::executeUpdate);
    }

    @Override
    public Resume get(String uuid) {
        logger.info("Get resume by id: " + uuid);
        return helper.executeTransactionalStatement(conn -> {
            Resume result;
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM resume WHERE uuid=?")) {
                pst.setString(1, uuid);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    throw new NotExistsStorageException(uuid);
                }
                result = new Resume(rs.getString("uuid"), rs.getString("full_name"));
            }
            fillResumeData(conn, "SELECT * FROM contact WHERE resume_uuid=?", result, this::fillInContacts);
            fillResumeData(conn, "SELECT * FROM section WHERE resume_uuid=?", result, this::fillInSections);
            return result;
        });
    }

    @Override
    public void update(Resume r) {
        logger.info("Update resume: " + r);
        helper.executeTransactionalStatement(conn -> {
            String uuid = r.getUuid();
            try (PreparedStatement pst = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid =?")) {
                pst.setString(1, r.getFullName());
                pst.setString(2, uuid);
                if (pst.executeUpdate() != 1) {
                    throw new NotExistsStorageException(uuid);
                }
            }
            deleteData(conn, uuid, "DELETE  FROM contact WHERE resume_uuid=?");
            deleteData(conn, uuid, "DELETE  FROM section WHERE resume_uuid=?");
            writeContacts(r, conn);
            writeSections(r, conn);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        logger.info("Save resume: " + r);
        helper.executeTransactionalStatement(conn -> {
            try (PreparedStatement pst = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                pst.setString(1, r.getUuid());
                pst.setString(2, r.getFullName());
                pst.execute();
            }
            writeContacts(r, conn);
            writeSections(r, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        logger.info("Delete resume by id: " + uuid);
        helper.executeStatement("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() != 1) {
                throw new NotExistsStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        logger.info("Get all sorted");
        return helper.executeTransactionalStatement(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resumesResultSet = pst.executeQuery();
                while (resumesResultSet.next()) {
                    String uuid = resumesResultSet.getString("uuid");
                    Resume resume = new Resume(uuid, resumesResultSet.getString("full_name"));
                    resumes.put(uuid, resume);
                }
            }
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet contactsSet = pst.executeQuery();
                while (contactsSet.next()) {
                    fillInContacts(contactsSet, resumes.get(contactsSet.getString("resume_uuid")));
                }
            }
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet sectionsSet = pst.executeQuery();
                while (sectionsSet.next()) {
                    fillInSections(sectionsSet, resumes.get(sectionsSet.getString("resume_uuid")));
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return helper.executeStatement("SELECT COUNT(*) FROM resume", pst -> {
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void fillResumeData(Connection conn, String sqlQuery, Resume result, Executor method) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sqlQuery)) {
            pst.setString(1, result.getUuid());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                method.execute(rs, result);
            }
        }
    }

    private void deleteData(Connection conn, String uuid, String s) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(s)) {
            pst.setString(1, uuid);
            pst.execute();
        }
    }

    protected void fillInContacts(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.setContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void fillInSections(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        SectionType type = SectionType.valueOf(rs.getString("type"));
        if (value != null) {
            r.setRecord(type, convertStringToSectionData(type, value));
        }
    }

    private void writeContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                pst.setString(1, r.getUuid());
                pst.setString(2, e.getKey().name());
                pst.setString(3, e.getValue());
                pst.addBatch();
            }
            pst.executeBatch();
        }
    }

    private void writeSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractRecord> e : r.getRecords().entrySet()) {
                SectionType type = e.getKey();
                pst.setString(1, r.getUuid());
                pst.setString(2, type.name());
                pst.setString(3, convertSectionDataToString(type, r));
                pst.addBatch();
            }
            pst.executeBatch();
        }
    }

    private AbstractRecord convertStringToSectionData(SectionType type, String value) {
        switch (type) {
            case PERSONAL:
            case OBJECTIVES:
                return new SimpleTextRecord(value);
            case ACHIEVEMENTS:
            case QUALIFICATIONS: {
                return new BulletedListRecord(Arrays.stream(value.split("\n")).collect(Collectors.toList()));
            }
        }
        return null;
    }

    private String convertSectionDataToString(SectionType type, Resume r) {
        switch (type) {
            case PERSONAL:
            case OBJECTIVES:
                return ((SimpleTextRecord) r.getRecords().get(type)).getSimpleText();
            case ACHIEVEMENTS:
            case QUALIFICATIONS: {
                List<String> records = ((BulletedListRecord)r.getRecords().get(type)).getBulletedRecords();
                return String.join("\n", records);
            }
        }
        return null;
    }

    private interface Executor {
        void execute(ResultSet rs, Resume r) throws SQLException;
    }
}