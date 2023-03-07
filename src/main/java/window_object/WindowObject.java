package window_object;

import database.Database;

import javax.swing.*;
import java.awt.*;

public class WindowObject {
    private final Database database;
    private final JFrame mainFrame;
    private final Container contentContainer;
    private final SpringLayout layout = new SpringLayout();
    private final WindowNav nav;
    private int windowHeight;
    private int windowWidth;

    public enum WindowSize {
        AddSentenceView(600, 850, true),
        SetLinkView(800, 500, false);

        private final int windowWidth;
        private final int windowHeight;
        private final Boolean isMain;

        WindowSize(int windowWidth, int windowHeight, boolean isMain) {
            this.windowWidth = windowWidth;
            this.windowHeight = windowHeight;
            this.isMain = isMain;
        }

        public int getWindowWidth() {
            return windowWidth;
        }

        public int getWindowHeight() {
            return windowHeight;
        }

        public boolean getIsMain() {
            return isMain;
        }
    }

    public WindowObject(Database database, WindowSize windowSize) {
        mainFrame = new JFrame();
        this.database = database;
        this.windowWidth = windowSize.windowWidth;
        this.windowHeight = windowSize.windowHeight;
        changeWindowSize();
        mainFrame.setResizable(false);
        if(windowSize.isMain) {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        mainFrame.setTitle("Plus One");

        contentContainer = mainFrame.getContentPane();
        contentContainer.setLayout(layout);

        //Must be instantiated after since nav goes to new window on new nav object, which also clears the container
        //but the container needs to be made first
        nav = new WindowNav(this);
        setWindowVisible();
        centerWindow();
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
