package com.dbftp.connection.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDBConnection {
    java.sql.Connection conn = null;

    public ResultSet select(String stm) throws SQLException;

    public int execute(String stm) throws SQLException;

    public Boolean insertTyped(ResultSet rs, String tableName, String[] columnNames, Boolean typeCast) throws SQLException;

    public void close() throws SQLException;
}
