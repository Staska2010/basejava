package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.NotExistsStorageException;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ru.topjava.basejava.storage.SqlHelper.SELECT;
import static ru.topjava.basejava.storage.SqlHelper.UPDATE;


public class SqlStorage implements IStorage {
    private static final Logger logger = Logger.getLogger(SqlStorage.class.getName());
    ConnectionFactory connectionFactory;
    SqlHelper helper;

    public SqlStorage(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        helper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        logger.info("Clear database");
        helper.executeStatement("DELETE FROM resume",
                UPDATE,
                null, null);
    }

    @Override
    public Resume get(String uuid) {
        logger.info("Get resume by id: " + uuid);
        ResultSet rs = helper.executeStatement("SELECT * FROM resume r WHERE r.uuid =?",
                SELECT,
                uuid, null);
        try {
            if (!rs.next()) {
                throw new NotExistsStorageException(uuid);
            }
            String fullName = rs.getString("full_name");
            rs.close();
            return new Resume(uuid, fullName);
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
    }

    @Override
    public void update(Resume r) {
        logger.info("Update resume: " + r);
        int res = helper.executeStatement("UPDATE resume SET full_name=? WHERE uuid =?",
                UPDATE,
                r.getFullName(), r.getUuid());
        if (res != 1) {
            throw new NotExistsStorageException(r.getUuid());
        }
    }

    @Override
    public void save(Resume r) {
        logger.info("Save resume: " + r);
        int res = helper.executeStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                UPDATE,
                r.getUuid(), r.getFullName());
        if (res != 1) {
            throw new ExistsStorageException(r.getUuid());
        }
    }

    @Override
    public void delete(String uuid) {
        logger.info("Delete resume by id: " + uuid);
        int res = helper.executeStatement("DELETE FROM resume r WHERE r.uuid =?",
                UPDATE,
                uuid, null);
        if (res != 1) {
            throw new NotExistsStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        logger.info("Get all sorted");
        List<Resume> result = new ArrayList<>();
        ResultSet rs = helper.executeStatement("SELECT * FROM resume ORDER BY uuid",
                SELECT,
                null, null);
        try {
            while (rs.next()) {
                result.add(new Resume(rs.getString(1).trim(), rs.getString(2).trim()));
            }
            rs.close();
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
        return result;
    }

    @Override
    public int size() {
        ResultSet rs = helper.executeStatement("SELECT COUNT(*) FROM resume",
                SELECT,
                null, null);
        try {
            rs.next();
            int size = rs.getInt(1);
            rs.close();
            return size;
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
    }
}
