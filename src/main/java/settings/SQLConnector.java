package settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;
import static settings.TestConfig.getProperty;

public class SQLConnector {
    private Connection connection;
    private String dbUrl = "jdbc:sqlserver://web.lumiglass.io:1433;databaseName=SwagScreening";
    private String dbUrlCT = "jdbc:sqlserver://54.219.253.104:1433;databaseName=CommunicationTool";
    private String username = getProperty("db.user.name");
    private String password = getProperty("db.user.password");
    public static final String LIKE = " LIKE ";
    public static final String EQUAL = " = ";
    public static final String NOT_EQUAL = " != ";

    public void executeQuery(String query) {
        try {
            if (query.contains("CommunicationTool")) {
                connection = getConnection(dbUrlCT, username, password);
            } else {
                connection = getConnection(dbUrl, username, password);
            }
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStringValueInDB(String query, String columnName) {
        String data = null;
        try {
            if (query.contains("CommunicationTool")) {
                connection = getConnection(dbUrlCT, username, password);
            } else {
                connection = getConnection(dbUrl, username, password);
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data = resultSet.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return data;
    }

    public int getIntValueInDB(String query, String columnName) {
        int data = 0;
        try {
            if (query.contains("CommunicationTool")) {
                connection = getConnection(dbUrlCT, username, password);
            } else {
                connection = getConnection(dbUrl, username, password);
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data = resultSet.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return data;
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
                connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
