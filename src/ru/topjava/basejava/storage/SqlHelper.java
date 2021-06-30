package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory factory;

    public interface QueryHandler<T> {
        T handle(PreparedStatement pst) throws SQLException;
    }

    protected SqlHelper(ConnectionFactory connectionFactory) {
        factory = connectionFactory;
    }

    public <T> T executeStatement(String statement, QueryHandler<T> handler) {
        try (Connection connection = factory.getConnection();
             PreparedStatement pst = connection.prepareStatement(statement)) {
            return handler.handle(pst);
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
    }
}
