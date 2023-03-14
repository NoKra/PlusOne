import database.Database;
import window_object.WindowObject;

import javax.swing.*;
import java.sql.SQLException;


public class Main {
    private static Database database;

    public static void main(String[] args) throws SQLException {
        database = startDatabase();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WindowObject mainWindow = new WindowObject(database, WindowObject.WindowSize.BrowseView);
                mainWindow.getNav().toBrowse();
            }
        });
    }

    public static Database startDatabase() throws SQLException {
        Database database = new Database();

        //Ensures lock for H2 database is removed before exiting
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    database.getDbConnection().close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return database;
    }
}
