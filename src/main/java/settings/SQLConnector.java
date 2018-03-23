package settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;
import static settings.TestConfig.getProperty;

public class SQLConnector {
    private Connection connection;
    private String dbUrl = getProperty("db.url");
    private String username = getProperty("db.user.name");
    private String password = getProperty("db.user.password");
    public static final String LIKE = " LIKE ";
    public static final String EQUAL = " = ";

    public void executeQuery(String query) {
        try {
            connection = getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStringValueInDB(String query, String columnName){
        String data = null;
        try {
            connection = getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            data = resultSet.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return data;
    }

    public int getIntValueInDB(String query, String columnName){
        int data = 0;
        try {
            connection = getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            data = resultSet.getInt(columnName);
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
