package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

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
        return helper.executeStatement("SELECT * FROM resume AS r " +
                "LEFT JOIN contact AS c ON r.uuid=c.resume_uuid " +
                "WHERE r.uuid=?", pst -> {
            pst.setString(1, uuid);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                throw new NotExistsStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString(2));
            do {
                String value = rs.getString("value");
                if (value != null) {
                    r.setContact(ContactType.valueOf(rs.getString("type")), value);
                }
            } while (rs.next());
            return r;
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
            try (PreparedStatement pst = conn.prepareStatement("DELETE  FROM contact WHERE resume_uuid=?")) {
                pst.setString(1, uuid);
                if (pst.executeUpdate() == 0) {
                    throw new NotExistsStorageException(uuid);
                }
            }
            writeContacts(r, conn);
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
        return helper.executeStatement("SELECT * FROM resume ORDER BY full_name, uuid", pst -> {
            ResultSet resumesResultSet = pst.executeQuery();
            Map<String, Resume> resumes = new LinkedHashMap<>();
            while (resumesResultSet.next()) {
                String uuid = resumesResultSet.getString("uuid");
                Resume resume = new Resume(uuid, resumesResultSet.getString("full_name"));
                resumes.put(uuid, resume);
            }
            fillInContacts(resumes);
            return new ArrayList<>(resumes.values());
        });
    }

    private void fillInContacts(Map<String, Resume> resumes) {
        helper.executeStatement("SELECT * FROM contact", ps -> {
            ResultSet contactsSet = ps.executeQuery();
            while (contactsSet.next()) {
                String value = contactsSet.getString("value");
                if (value != null) {
                    resumes.get(contactsSet.getString("resume_uuid"))
                            .setContact(ContactType.valueOf(contactsSet.getString("type")), value);
                }
            }
            return null;
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
}