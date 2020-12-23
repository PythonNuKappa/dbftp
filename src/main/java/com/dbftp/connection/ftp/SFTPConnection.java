package com.dbftp.connection.ftp;

import com.dbftp.main;
import com.jcraft.jsch.*;

import java.io.InputStream;

public class SFTPConnection implements IFTPConnection {

    private JSch jsch;
    private Session jschSession;
    private ChannelSftp channelSftp;

    public SFTPConnection(String host, String user, String pass) throws JSchException {
        jsch = new JSch();
        jsch.setKnownHosts(main.class.getClassLoader().getResourceAsStream("known_hosts"));
        jschSession = jsch.getSession(user, host);
        jschSession.setPassword(pass);
        jschSession.connect();
        channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
        channelSftp.connect();
    }

    @Override
    public void put(String fromFile, String toFile, boolean overwrite) throws SftpException {

        if(overwrite) {
            channelSftp.put(fromFile, toFile, ChannelSftp.OVERWRITE);
        } else {
            channelSftp.put(fromFile, toFile);
        }

    }

    @Override
    public void get(String fromFile, String toFile) throws SftpException {
        channelSftp.get(fromFile, toFile);
    }

    @Override
    public void close() {
        channelSftp.exit();
        jschSession.disconnect();
    }
}
