package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.sql.ConnectionFactory;
import ru.topjava.basejava.sql.QueryHandler;
import ru.topjava.basejava.sql.TransactionQueryHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ru.topjava.basejava.sql.ExceptionUtil.handleException;

public class SqlHelper {
    private final ConnectionFactory factory;

    protected SqlHelper(ConnectionFactory connectionFactory) {
        factory = connectionFactory;
    }

    public <T> T executeStatement(String statement, QueryHandler<T> handler) {
        try (Connection connection = factory.getConnection();
             PreparedStatement pst = connection.prepareStatement(statement)) {
            return handler.handle(pst);
        } catch (SQLException exc) {
            throw handleException(exc);
        }
    }

    public <T> T executeTransactionalStatement(TransactionQueryHandler<T> handler) {
        try (Connection connection = factory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T result = handler.handle(connection);
                connection.commit();
                return result;
            } catch (SQLException exc) {
                connection.rollback();
                throw handleException(exc);
            }
        } catch (SQLException exc) {
            throw new StorageException(exc);
        }
    }
}