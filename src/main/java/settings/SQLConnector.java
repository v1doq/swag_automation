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
    public static final String SELECT_FROM = "SELECT * FROM ";
    public static final String DELETE_FROM = "DELETE FROM ";
    public static final String WHERE = " WHERE ";
    public static final String INSERT_INTO = "INSERT INTO ";
    public static final String VALUES = " VALUES ";

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

    public String getValueInDb(String query, String columnName) {
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
            LOG.info("Execute query: " + query);
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
            connection = getConnection("jdbc:sqlserver://54.219.253.104:1433", username, password);
        } else {
            connection = getConnection("jdbc:sqlserver://34.214.177.103:1433", username, password);
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
