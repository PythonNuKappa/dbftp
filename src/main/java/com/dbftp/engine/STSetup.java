package com.dbftp.engine;

import com.dbftp.engine.utils.StringUtils;
import com.dbftp.main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class STSetup {

    public static String setup(String configFiles, STItem source, STItem target) throws IOException, Exception {
        String ret = null;
        String fileName;
        Properties prop = new Properties();

        InputStream ip = new FileInputStream(new File(configFiles));
        prop.load(ip);

        ArrayList<String> sourcePosts = new ArrayList<String>();
        prop.forEach((key, value) -> {
            if(key.toString().startsWith("source.post")) {
                sourcePosts.add(value.toString());
            }
        });

        main.logger.debug("SOURCE");
        source.updateSTItem(
                prop.getProperty("source.type"),
                prop.getProperty("source.subtype"),
                prop.getProperty("source.url"),
                prop.getProperty("source.user"),
                prop.getProperty("source.password"),
                prop.getProperty("source.path"),
                StringUtils.parse(prop.getProperty("source.select")),
                sourcePosts);

        ArrayList<String> targetPosts = new ArrayList<String>();
        prop.forEach((key, value) -> {
            if(key.toString().startsWith("target.post")) {
                targetPosts.add(value.toString());
            }
        });

        main.logger.debug("TARGET");
        target.updateSTItem(
                prop.getProperty("target.type"),
                prop.getProperty("target.subtype"),
                prop.getProperty("target.url"),
                prop.getProperty("target.user"),
                prop.getProperty("target.password"),
                prop.getProperty("target.path"),
                prop.getProperty("target.select"),
                targetPosts);

        fileName = prop.getProperty("file.name");

        // Compilation
        if (fileName == null && source.getType() == "DB") throw new Exception("Attribute file.name needs to be provided for source.type = DB.");

        // Return the intermediate file name.
        // If the file name is not provided then we use the original FTP file name (only for FTP source).
        if (fileName != null) {
            ret = fileName + '_' + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        } else {
            ret = source.getSelect();
        }
        return ret;
    }
}
