package settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static settings.TestConfig.getProperty;
import static java.sql.DriverManager.getConnection;

public class SQLConnector {
    private Connection connection;
    private String dbUrl = getProperty("db.url");
    private String username = getProperty("db.user.name");
    private String password = getProperty("db.user.password");

    public ResultSet executeSelectQuery(String query) {
        ResultSet resultSet = null;
        try {
            connection = getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void executeQuery(String query) {
        try {
            connection = getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
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
