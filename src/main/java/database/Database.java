package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String jdbcURL = "jdbc:h2:file:./database/podb";
    private final String username = "po";
    private final String password = "";
    private final Connection dbConnection;

    public Database() throws  SQLException {
        dbConnection = DriverManager.getConnection(jdbcURL, username, password);
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void testConnection() throws SQLException {

        System.out.println("Connected to H2 embedded database");

        String sql = "SELECT * FROM STUDENTS";

        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            int ID = resultSet.getInt("ID");
            String name = resultSet.getString("name");
            System.out.println("Student: " + ID + "Name: " + name);
        }
    }
    public void testInsert() throws SQLException {
        String sql = "INSERT INTO NOTSTUDENTS VALUES (9, 'stop being dumb');";
        dbConnection.createStatement().execute(sql);
        System.out.println("something inserted");
    }

    public void testTableCreation() throws SQLException {
        String sql = "CREATE TABLE NOTSTUDENTS (" +
                "ID INT PRIMARY KEY," +
                "NAME VARCHAR(255))";
        dbConnection.createStatement().execute(sql);
        System.out.println("table maybe created");
    }
}
