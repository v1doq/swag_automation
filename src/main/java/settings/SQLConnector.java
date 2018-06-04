package settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class SQLConnector {
    private Connection connection;
    private String username = getProperty("db.user.name");
    private String password = getProperty("db.user.password");
    public static final String LIKE = " LIKE ";
    public static final String EQUAL = " = ";
    public static final String NOT_EQUAL = " != ";

    public void executeQuery(String query) {
        try {
            openConnection(query);
            Statement statement = connection.createStatement();
            LOG.info("Execute query: " + query);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public String getStringValueInDB(String query, String columnName) {
        String data = null;
        try {
            openConnection(query);
            Statement statement = connection.createStatement();
            LOG.info("Execute query: " + query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data = resultSet.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        LOG.info("Return value: " + data);
        return data;
    }

    public int getIntValueInDB(String query, String columnName) {
        int data = 0;
        try {
            openConnection(query);
            Statement statement = connection.createStatement();
            LOG.info("Execute query:" + query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                data = resultSet.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        LOG.info("Return value: " + data);
        return data;
    }

    private void openConnection(String query) throws SQLException {
        if (query.contains("CommunicationTool")) {
            String dbUrlCT = "jdbc:sqlserver://54.219.253.104:1433;databaseName=CommunicationTool";
            connection = getConnection(dbUrlCT, username, password);
        } else {
            String dbUrl = "jdbc:sqlserver://web.lumiglass.io:1433;databaseName=SwagScreening";
            connection = getConnection(dbUrl, username, password);
        }
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
