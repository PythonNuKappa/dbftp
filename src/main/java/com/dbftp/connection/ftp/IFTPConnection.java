package com.dbftp.connection.ftp;

import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface IFTPConnection {

    public void put(String fromFile, String toFile, boolean overwrite) throws Exception;

    public void get(String fromFile, String toFile) throws Exception;

    public void close() throws IOException;
}
