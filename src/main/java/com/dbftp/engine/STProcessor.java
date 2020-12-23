package com.dbftp.engine;

import com.dbftp.connection.db.IDBConnection;
import com.dbftp.connection.db.DBConnection;
import com.dbftp.connection.ftp.FTPConnection;
import com.dbftp.connection.ftp.SFTPConnection;
import com.dbftp.connection.ftp.IFTPConnection;
import com.dbftp.main;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class STProcessor {

    public static void readSource (STItem source, String fileUrl) throws Exception {
        ResultSet ret = null;
        switch (source.getType().toUpperCase()) {
            case "DB" :
                IDBConnection conn = null;
                conn = new DBConnection(source.getUrl(), source.getUser(), source.getPassword());
                source.setDbConn(conn);
                ret = conn.select(source.getSelect());

                if (ret.isBeforeFirst()) {
                    // File in the middle
                    main.logger.debug("RESULT TO CSV");
                    resultToCsv(ret, fileUrl);
                } else throw new Exception("No data found.");

            break;
            case "FTP" :
                IFTPConnection ftpConn = null;
                switch (source.getSubtype().toUpperCase()) {
                    case "SFTP" :
                        ftpConn = new SFTPConnection(source.getUrl(), source.getUser(), source.getPassword());
                        break;
                    case "FTP" :
                        ftpConn = new FTPConnection(source.getUrl(), source.getUser(), source.getPassword());
                        break;
                }

                source.setFtpConn(ftpConn);
                ftpConn.get(source.getPath() + "/" + source.getSelect(), fileUrl);
        }

    }

    public static void postSTItem (STItem st) throws SQLException {
        switch (st.getType().toUpperCase()){
            case "DB" :
                for (String s : st.getPosts()){
                    st.getDbConn().execute(s);
                }
                break;
        }
    }

    public static void resultToCsv (ResultSet rs, String fileUrl) throws Exception {
        if (rs.isBeforeFirst()){
            FileWriter fWriter = new FileWriter(fileUrl);
            CSVWriter csvWriter = new CSVWriter(fWriter, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
            csvWriter.writeAll(rs, true);
            csvWriter.close();
        }
    }

    public static void writeTarget (STItem target, String fileUrl, String fileName) throws Exception {
        switch (target.getType().toUpperCase()) {
            case "DB" :
                break;
            case "FTP" :
                IFTPConnection ftpConn = null;
                switch (target.getSubtype().toUpperCase()) {
                    case "SFTP" :
                        ftpConn = new SFTPConnection(target.getUrl(), target.getUser(), target.getPassword());
                        break;
                    case "FTP" :
                        ftpConn = new FTPConnection(target.getUrl(), target.getUser(), target.getPassword());
                        break;
                }

                target.setFtpConn(ftpConn);
                ftpConn.put(fileUrl, target.getPath() + "/" + fileName, true);
                break;
        }
    }

    public static void close(STItem source, STItem target) throws SQLException, IOException {
        if(source.getDbConn() != null) source.getDbConn().close();
        if(source.getFtpConn() != null) source.getFtpConn().close();

        if(target.getDbConn() != null) target.getDbConn().close();
        if(target.getFtpConn() != null) target.getFtpConn().close();
    }

}
