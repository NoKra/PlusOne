import database.DbConnect;
import window_object.WindowObject;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        //run();
        dbTest();


    }

    public static void run() {
        WindowObject mainWindow = new WindowObject();
        mainWindow.changeWindowSize();

        mainWindow.getNav().toMainView();

        mainWindow.setWindowVisible();
        mainWindow.centerWindow();
    }

    public static void dbTest() throws SQLException {
        DbConnect newConnect = new DbConnect();
        newConnect.testConnection();
    }
}
