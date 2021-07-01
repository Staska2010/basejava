package ru.topjava.basejava.storage;

import org.postgresql.util.PSQLState;
import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private StorageException handleException(SQLException exc) {
        //https://www.codota.com/code/java/classes/org.postgresql.util.PSQLException
        //https://www.postgresql.org/docs/12/errcodes-appendix.html (23505	unique_violation)
        return exc.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())
                ? new ExistsStorageException(null)
                : new StorageException(exc);
    }

    public interface QueryHandler<T> {
        T handle(PreparedStatement pst) throws SQLException;
    }
}
