package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {
    String jdbcURL = "jdbc:h2:file:C:/Projects/Java/PlusOne/database/podb";
    String username = "po";
    String password = "";

    public DbConnect() {

    }

    public void testConnection() throws SQLException {
        java.sql.Connection connect = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connected to H2 embedded database");

        String sql = "SELECT * FROM STUDENTS";

        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            int ID = resultSet.getInt("ID");
            String name = resultSet.getString("name");
            System.out.println("Student: " + ID + "Name: " + name);
        }

    }
}
