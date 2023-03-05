package window_object;

import database.Database;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;

public class WindowObject {
    private final Database database;
    private final JFrame mainFrame;
    private final Container contentContainer;
    private final SpringLayout layout = new SpringLayout();
    private final WindowNav nav;
    private int windowHeight = 850;
    private int windowWidth = 600;


    public WindowObject(Database database) {
        this.database = database;
        mainFrame = new JFrame();
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle("Plus One");

        contentContainer = mainFrame.getContentPane();
        contentContainer.setLayout(layout);

        //Must be instantiated after since nav goes to new window on new nav object, which also clears the container
        //but the container needs to be made first
        nav = new WindowNav(this);
    }

    public Database getDatabase() {
        return database;
    }

    public Container getContentContainer() {
        return contentContainer;
    }

    public SpringLayout getLayout() {
        return layout;
    }

    public WindowNav getNav() {
        return nav;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void changeWindowSize() {
        //TODO: include some common dimensions here
        mainFrame.setSize(windowWidth, windowHeight);
    }

    public void clearWindow() {
        contentContainer.removeAll();
        contentContainer.repaint();
    }

    public void centerWindow() {
        mainFrame.setLocationRelativeTo(null);
    }

    public void setWindowVisible() {
        mainFrame.setVisible(true);
    }

    public void showNewContent() {
        contentContainer.revalidate();
    }

}
