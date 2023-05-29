import database.Database;
import settings.Settings;
import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class Main {
    private static Database database;
    private static Settings settings;

    public static void main(String[] args) throws SQLException {
        settings = new Settings();

        if(settings.verifySettingsExists()) {
            database = new Database(settings);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    WindowObject mainWindow = new WindowObject(database, settings, true);
                    mainWindow.getNav().toHome(true);
                }
            });
        } else {
            System.exit(0);
        }
    }

    //Temporary function for displaying available fonts
    private static void checkFonts() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for(String font : fonts) {
            System.out.println(font);
        }
    }
}
