package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        return helper.executeTransactionalStatement(conn -> {
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM resume r " +
                    "  LEFT JOIN contact c ON r.uuid=c.resume_uuid" +
                    "    WHERE r.uuid=?")) {
                pst.setString(1, uuid);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    throw new NotExistsStorageException(uuid);
                }
                Resume r = new Resume(uuid, rs.getString(2));
                do {
                    String value = rs.getString("value");
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    r.setContact(type, value);
                } while (rs.next());
                return r;
            }
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
            insertContacts(r, conn);
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
            insertContacts(r, conn);
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
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resumesResultSet = pst.executeQuery();
                List<Resume> result = new ArrayList<>();
                while (resumesResultSet.next()) {
                    Resume resume = new Resume(resumesResultSet.getString("uuid"),
                            resumesResultSet.getString("full_name"));
                    getContactsBySeparateQuery(conn, resume);
                    result.add(resume);
                }
                return result;
            }
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

    private void insertContacts(Resume r, Connection conn) throws SQLException {
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

    private void getContactsBySeparateQuery(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, resume.getUuid());
            ResultSet contactsSet = ps.executeQuery();
            while (contactsSet.next()) {
                resume.setContact(ContactType.valueOf(contactsSet.getString("type")),
                        contactsSet.getString("value"));
            }
        }
    }
}