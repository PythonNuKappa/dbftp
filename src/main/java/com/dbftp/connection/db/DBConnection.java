package com.dbftp.connection.db;

import java.sql.*;
import java.util.Arrays;

public class DBConnection implements IDBConnection {

    private Connection conn = null;

    public DBConnection(String jdbc, String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(jdbc, user, pass);
    }

    public DBConnection(String connection) throws SQLException {
        conn = DriverManager.getConnection(connection);
    }

    @Override
    public ResultSet select(String strStm) throws SQLException{
        Statement stm = conn.createStatement();
        return stm.executeQuery(strStm);
    }

    @Override
    public int execute(String strStm) throws SQLException {
        Statement stm = conn.createStatement();
        return stm.executeUpdate(strStm);
    }

    @Override
    public Boolean insertTyped(ResultSet rs, String tableName, String[] columnNames, Boolean typeCast) throws SQLException {
        DatabaseMetaData dbmd = conn.getMetaData();
        int sourceColumnType, targetColumnType;
        ResultSet targetResultSet;

        if(columnNames.length <= 0)
            throw new SQLException("Controlled exception. You must select 1 or more columns.");

        String strColums = "?";
        for (int i = 1; i < columnNames.length; i++){
            strColums.concat(",?");
        }

        if(rs.getMetaData().getColumnCount() != columnNames.length)
            throw new SQLException("Controlled exception. Source columns number and Target columns number must be the same.");

        // Setup the prepared statement. It will be executed at the end of the method.
        String query = " insert into " + tableName + " (" + Arrays.toString(columnNames).replace("[","").replace("]","") +
                       ") values (" + strColums + ")";
        PreparedStatement preparedStmt = conn.prepareStatement(query);

        // Assign each source column to target column.
        for (int i = 1; i <= columnNames.length; i++){
            targetResultSet = dbmd.getColumns(null,null, tableName, columnNames[i-1]);
            if (!targetResultSet.next() || !rs.next())
                throw new SQLException();

            sourceColumnType = rs.getMetaData().getColumnType(i);
            targetColumnType = targetResultSet.getInt("DATA_TYPE");

            if(sourceColumnType != targetColumnType && !typeCast)
                throw new SQLException("Controlled exception. source and target datatypes missmatch. You can change the setup to do a casting.");

            switch(targetColumnType){
                case Types.VARCHAR : preparedStmt.setString (i, rs.getString(i));
                    break;
                case Types.DATE : preparedStmt.setDate (i, rs.getDate(i));
                    break;
                case Types.INTEGER : preparedStmt.setInt (i, rs.getInt(i));
                    break;
                case Types.BOOLEAN : preparedStmt.setBoolean (i, rs.getBoolean(i));
                    break;
            }
        }

        // Run the query.
        return preparedStmt.execute();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
