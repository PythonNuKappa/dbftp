package com.dbftp;

import com.dbftp.engine.STItem;
import com.dbftp.engine.STProcessor;
import com.dbftp.engine.STSetup;
import org.apache.log4j.Logger;

public class main {

    public static Logger logger = null;
    public static String root = null;

    public static void main(String[] args) {

        //<editor-fold desc="Variables">
        STItem source = new STItem(), target = new STItem();
        String fileName = null;
        //</editor-fold>

        //<editor-fold desc="Check for jdbcs">
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC driver not found.");
            e.printStackTrace();
            return;
        }
        //</editor-fold>

        //<editor-fold desc="Configuration file in the arguments (or default)">
        String configFile = "config.properties";
        if(args.length > 0) configFile = args[0];
        //</editor-fold>

        //<editor-fold desc="Root, file paths and Logger">
        // We get the root directory and set the system property (for different OS)
        root = System.getProperty("user.dir");
        System.setProperty("home.root", root);

        // Set the file paths
        String configFiles = root + "/config/" + configFile;
        String dirFiles = root + "/files/";

        // Setup the logger
        logger = Logger.getLogger(main.class);
        //</editor-fold>

        //<editor-fold desc="Main functionality">
        // --------------------------------------------------------------------------------------------

        try {

            //<editor-fold desc="Setup of the properties with default file name or set in the parameters">
            logger.debug("SETUP PROPERTIES");
            fileName = STSetup.setup(configFiles, source, target);
            //</editor-fold>

            //<editor-fold desc="Read from the source">
            logger.debug("READ SOURCE");
            logger.debug(source.getUrl() + source.getUser() + source.getPassword());
            STProcessor.readSource(source, dirFiles + fileName);
            //</editor-fold>

                //<editor-fold desc="In case there are post actions">
                logger.debug("POSTS");
                STProcessor.postSTItem(source);
                //</editor-fold>

            //<editor-fold desc="Write in the target">
            logger.debug("WRITE TARGET");
            STProcessor.writeTarget(target, dirFiles + fileName, fileName);
            //</editor-fold>

            //<editor-fold desc="Close all the connections before finishing">
            STProcessor.close(source, target);
            //</editor-fold>

        } catch (Exception e){
            logger.debug("ERROR: " + e.getMessage());
            return;
        }

        // --------------------------------------------------------------------------------------------
        //</editor-fold>

        //<editor-fold desc="Return">
        logger.debug("END");

        return;
        //</editor-fold>

    }
}
