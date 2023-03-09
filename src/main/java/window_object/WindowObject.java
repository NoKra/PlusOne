package window_object;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class WindowObject {
    private final Database database;
    private final JFrame mainFrame;
    private final Container contentContainer;
    private final SpringLayout layout = new SpringLayout();
    private final JMenuBar menuBar = new JMenuBar();
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
        mainFrame.setJMenuBar(menuBar);

        contentContainer = mainFrame.getContentPane();
        contentContainer.setLayout(layout);

        createMenuBar();
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

    private void createMenuBar() {
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem printSentenceCount = new JMenuItem("Print Count");
        fileMenu.add(printSentenceCount);
        printSentenceCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(database.getMaxSentenceIndex());
            }
        });

        JMenuItem purgeSentenceTable = new JMenuItem("Purge Sentences");
        fileMenu.add(purgeSentenceTable);

        purgeSentenceTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyPurge("SENTENCES");
            }
        });

        JMenu databaseMenu = new JMenu("Database");
        menuBar.add(databaseMenu);

        JMenuItem backupSave = new JMenuItem("Backup Database");
        databaseMenu.add(backupSave);
        backupSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.backupDatabase();
            }
        });

        JMenuItem loadBackup = new JMenuItem("Load Backup");
        databaseMenu.add(loadBackup);
        loadBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.loadBackupDatabase();
            }
        });
    }

    private void verifyPurge(String targetTable) {
        JDialog confirmPurge = new JDialog(mainFrame, "Confirm Purge");
        SpringLayout confirmLayout = new SpringLayout();
        confirmPurge.setLayout(confirmLayout);

        JLabel confirmMessage = new JLabel("Confirm Sentence Purge?");
        confirmLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, confirmMessage, 0,
                SpringLayout.HORIZONTAL_CENTER, confirmPurge.getContentPane()
        );
        confirmLayout.putConstraint(
                SpringLayout.NORTH, confirmMessage, 20,
                SpringLayout.NORTH, confirmPurge.getContentPane()
        );
        confirmPurge.add(confirmMessage);

        JButton confirmButton = new JButton("Confirm");
        confirmLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, confirmButton, -50,
                SpringLayout.HORIZONTAL_CENTER, confirmPurge.getContentPane()
        );
        confirmLayout.putConstraint(
                SpringLayout.NORTH, confirmButton, 20,
                SpringLayout.SOUTH, confirmMessage
        );
        confirmPurge.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        confirmLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, cancelButton, 50,
                SpringLayout.HORIZONTAL_CENTER, confirmPurge.getContentPane()
        );
        confirmLayout.putConstraint(
                SpringLayout.NORTH, cancelButton, 20,
                SpringLayout.SOUTH, confirmMessage
        );
        confirmPurge.add(cancelButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    database.purgeTable(targetTable);

                    confirmPurge.dispose();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmPurge.dispose();
            }
        });

        confirmPurge.setSize(250, 150);
        confirmPurge.setLocationRelativeTo(null);
        confirmPurge.setVisible(true);
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
