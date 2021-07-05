package ru.topjava.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QueryHandler<T> {
    T handle(PreparedStatement pst) throws SQLException;
}
