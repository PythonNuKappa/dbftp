package com.dbftp.connection.ftp;

import com.dbftp.main;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class FTPConnection implements IFTPConnection {

    private FTPClient ftp;

    public FTPConnection(String host, String user, String pass) throws IOException {
        ftp = new FTPClient();
        ftp.connect(host);
        ftp.login(user, pass);
    }

    @Override
    public void put(String fromFile, String toFile, boolean overwrite) throws IOException {

        if(overwrite) {
            File file = new File (fromFile);
            InputStream is = new FileInputStream(file);
            ftp.storeFile(toFile, is);
            is.close();
        } else {
            File file = new File (fromFile);
            InputStream is = new FileInputStream(file);
            ftp.storeFile(toFile, is);
            is.close();
        }

    }

    @Override
    public void get(String fromFile, String toFile) throws IOException {
        File file = new File (toFile);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        ftp.retrieveFile(fromFile, os);
        os.close();
    }

    @Override
    public void close() throws IOException {
        ftp.disconnect();
    }
}
