import database.Database;
import window_object.WindowObject;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        startApp();
        //dbTest();


    }

    public static void startApp() throws SQLException {
        Database database = new Database();

        WindowObject mainWindow = new WindowObject();
        mainWindow.changeWindowSize();

        mainWindow.getNav().toMainView();

        mainWindow.setWindowVisible();
        mainWindow.centerWindow();

        database.testInsert();
        //database.testConnection();
        //database.testTableCreation();

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

    public static void dbTest() throws SQLException {
        Database newConnect = new Database();
        newConnect.testConnection();
    }
}
