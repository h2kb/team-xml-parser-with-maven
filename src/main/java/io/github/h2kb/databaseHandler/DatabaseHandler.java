package io.github.h2kb.databaseHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHandler {

    private static Properties getConnectionProperties() {
        String databaseProperties = "./src/main/java/io/github/h2kb/databaseHandler/database.properties";
        Properties properties = new Properties();

        try (FileInputStream in = new FileInputStream(databaseProperties)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static Connection getDbConnection() throws SQLException {
        Properties properties = getConnectionProperties();

        String url = properties.getProperty("dbUrl");
        String user = properties.getProperty("dbUser");
        String password = properties.getProperty("dbPassword");

        return DriverManager.getConnection(url, user, password);
    }
}
