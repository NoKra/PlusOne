import database.Database;
import window_object.WindowObject;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        startDatabase();
        startWindow();


    }

    public static void startWindow() throws SQLException {

        WindowObject mainWindow = new WindowObject();
        mainWindow.changeWindowSize();

        mainWindow.getNav().toMainView();

        mainWindow.setWindowVisible();
        mainWindow.centerWindow();

    }

    public static void startDatabase() throws SQLException {
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
    }
}
