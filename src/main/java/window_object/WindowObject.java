package window_object;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class WindowObject {
    private final Database database;
    private final JFrame mainFrame = new JFrame();
    private final Container container = mainFrame.getContentPane();
    private final SpringLayout layout = new SpringLayout();
    private final JMenuBar menuBar = new JMenuBar();
    private final WindowNav nav;
    private final int padding = 20;

    public WindowObject(Database database, boolean isMain) {
        this.database = database;
        if(isMain) {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setJMenuBar(menuBar);
        }
        mainFrame.setTitle("Plus One");
        container.setLayout(layout);
        //Window Object must be instantiated before nav object, as nav object requires WindowObject as parameter
        nav = new WindowNav(this);
        createMenuBar();
        centerWindow();
        setWindowVisible();
    }
    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void packFrame() {
        mainFrame.pack();
    }

    public Database getDatabase() {
        return database;
    }

    public Container getContainer() {
        return container;
    }

    public SpringLayout getLayout() {
        return layout;
    }

    public WindowNav getNav() {
        return nav;
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

    public JPanel createNavPanel() {
        JPanel navPanel = new JPanel();
        SpringLayout buttonLayout = new SpringLayout();
        navPanel.setLayout(buttonLayout);

        JButton mainButton = new JButton("Main");
        buttonLayout.putConstraint(
                SpringLayout.WEST, mainButton, padding,
                SpringLayout.WEST, navPanel
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, mainButton, padding,
                SpringLayout.NORTH, navPanel
        );
        navPanel.add(mainButton);


        JButton browseButton = new JButton("Browse");
        buttonLayout.putConstraint(
                SpringLayout.WEST, browseButton, padding,
                SpringLayout.EAST, mainButton
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, browseButton, padding,
                SpringLayout.NORTH, navPanel
        );
        navPanel.add(browseButton);

        JButton addButton = new JButton("Add");
        buttonLayout.putConstraint(
                SpringLayout.WEST, addButton, padding,
                SpringLayout.EAST, browseButton
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, addButton, padding,
                SpringLayout.NORTH, navPanel
        );
        navPanel.add(addButton);


        buttonLayout.putConstraint(
                SpringLayout.EAST, navPanel, padding,
                SpringLayout.EAST, addButton
        );
        buttonLayout.putConstraint(
                SpringLayout.SOUTH, navPanel, padding,
                SpringLayout.SOUTH, addButton
        );

        layout.putConstraint(
                SpringLayout.NORTH, navPanel, 0,
                SpringLayout.NORTH, container
        );

        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, navPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        container.add(navPanel);

        layout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, navPanel
        );

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.toMain();
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.toBrowse();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.toAddSentence();
            }
        });

        return navPanel;
    }

    public JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new SpringLayout());
        container.add(contentPanel);

        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, contentPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, contentPanel, 0,
                SpringLayout.NORTH, container
        );
        layout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, contentPanel
        );
        return contentPanel;
    }


    public JPanel createContentPanel(JPanel navPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new SpringLayout());
        container.add(contentPanel);

        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, contentPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, contentPanel, 0,
                SpringLayout.SOUTH, navPanel
        );
        layout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, contentPanel
        );
        return contentPanel;
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

    public void clearWindow() {
        container.removeAll();
        container.repaint();
    }

    public void centerWindow() {
        mainFrame.setLocationRelativeTo(null);
    }

    public void setWindowVisible() {
        mainFrame.setVisible(true);
    }

    public void destroyWindow() {
        mainFrame.dispose();
    }

    public void showNewContent() {
        container.revalidate();
    }

}
