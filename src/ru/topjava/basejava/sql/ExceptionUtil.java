package ru.topjava.basejava.sql;

import org.postgresql.util.PSQLState;
import ru.topjava.basejava.exception.ExistsStorageException;
import ru.topjava.basejava.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {

    }

    public static StorageException handleException(SQLException exc) {
        //https://www.codota.com/code/java/classes/org.postgresql.util.PSQLException
        //https://www.postgresql.org/docs/12/errcodes-appendix.html (23505	unique_violation)
        return exc.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())
                ? new ExistsStorageException(null)
                : new StorageException(exc);
    }
}
