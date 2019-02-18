package com.spdu.bll.models.constants;

import java.io.IOException;
import java.util.Properties;

public class PathToFiles {

    public static String get() throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        String path = System.getProperty("user.home") + properties.getProperty("server.upload.docs.path");
        return path;
    }
}
