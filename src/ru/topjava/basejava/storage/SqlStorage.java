package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.model.Resume;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        return helper.executeStatement("SELECT * FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistsStorageException(uuid);
            }
            return new Resume(uuid, rs.getString(2));
        });
    }

    @Override
    public void update(Resume r) {
        logger.info("Update resume: " + r);
        helper.executeStatement("UPDATE resume SET full_name=? WHERE uuid =?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() != 1) {
                throw new NotExistsStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        logger.info("Save resume: " + r);
        helper.executeStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
            String uuid = r.getUuid();
            ps.setString(1, uuid);
            ps.setString(2, r.getFullName());
            ps.executeUpdate();
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
        return helper.executeStatement("SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Resume(rs.getString(1), rs.getString(2)));
            }
            return result;
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
}