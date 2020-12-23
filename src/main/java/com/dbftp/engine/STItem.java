package com.dbftp.engine;

import com.dbftp.connection.db.IDBConnection;
import com.dbftp.connection.ftp.IFTPConnection;

import java.util.ArrayList;

public class STItem {
    private String type;
    private String subtype;
    private String url;
    private String user;
    private String password;
    private String path;
    private String select;
    private ArrayList<String> posts;
    private IDBConnection dbConn;
    private IFTPConnection ftpConn;

    public STItem() {
    }

    public STItem(String type, String subtype, String url, String user, String password, String path, String select, ArrayList<String> posts) {
        this.type = type;
        this.subtype = subtype;
        this.url = url;
        this.user = user;
        this.password = password;
        this.path = path;
        this.select = select;
        this.posts = posts;
    }

    public void updateSTItem(String type, String subtype, String url, String user, String password, String path, String select, ArrayList<String> posts) {
        this.type = type;
        this.subtype = subtype;
        this.url = url;
        this.user = user;
        this.password = password;
        this.path = path;
        this.select = select;
        this.posts = posts;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPath() {
        return path;
    }

    public String getSelect() {
        return select;
    }

    public IDBConnection getDbConn() {
        return dbConn;
    }

    public void setDbConn(IDBConnection dbConn) {
        this.dbConn = dbConn;
    }

    public IFTPConnection getFtpConn() {
        return ftpConn;
    }

    public void setFtpConn(IFTPConnection ftpConn) {
        this.ftpConn = ftpConn;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }
}
