package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory factory;
    final static QueryHandler<ResultSet> SELECT = PreparedStatement::executeQuery;
    final static QueryHandler<Integer> UPDATE = PreparedStatement::executeUpdate;

    public interface QueryHandler<T> {
        T handle(PreparedStatement pst) throws SQLException;
    }

    protected SqlHelper(ConnectionFactory connectionFactory) {
        factory = connectionFactory;
    }

    public <T> T executeStatement(String statement, QueryHandler<T> handler, String param1, String param2) {
        try (Connection connection = factory.getConnection()) {
            PreparedStatement pst = connection.prepareStatement(statement);
            if (param1 != null) pst.setString(1, param1);
            if (param2 != null) pst.setString(2, param2);
            return handler.handle(pst);
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
    }
}
