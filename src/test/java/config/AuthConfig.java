package config;

import java.io.IOException;

public class AuthConfig {

    public static String username, password, token, xsrfToken, projectId, treeId;

    public void getAuthConfig() throws IOException {

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config/remote.properties"));

        username = System.getProperty("username");
        password = System.getProperty("password");
        token = System.getProperty("token");
        xsrfToken = System.getProperty("xsrfToken");
        projectId = System.getProperty("projectId");
        treeId = System.getProperty("treeId");

    }
}
