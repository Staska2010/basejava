package ru.topjava.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionQueryHandler<T> {
    T handle(Connection conn) throws SQLException;
}
