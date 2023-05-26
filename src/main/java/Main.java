import database.Database;
import views.InitializationDialog;
import window_object.WindowObject;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class Main {
    private static Database database;
    private final static Path settingsJsonPath = Paths.get( "./src/main/java/user_settings.json");

    public static void main(String[] args) throws SQLException {
        if(!Files.exists(settingsJsonPath)) {
            new InitializationDialog();
        }
        if(Files.exists(settingsJsonPath)) {
            database = startDatabase();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    WindowObject mainWindow = new WindowObject(database, true);
                    mainWindow.getNav().toAddSentence();
                }
            });
        } else {
            System.exit(0);
        }
    }

    public static Database startDatabase() throws SQLException {
        Database database = new Database();

        //Ensures lock for H2 database is removed upon exiting
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
