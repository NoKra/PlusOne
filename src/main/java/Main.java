import database.Database;
import window_object.WindowObject;

import java.sql.SQLException;


public class Main {
    private static Database database;

    public static void main(String[] args) throws SQLException {
        database = startDatabase();
        WindowObject mainWindow = new WindowObject(database, WindowObject.WindowSize.AddSentenceView);
        mainWindow.getNav().toAddSentence();


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
