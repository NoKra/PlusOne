package views;


import controllers.HomeController;
import database.Database;
import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;

public class HomeView {
    private final WindowObject mainWindow;
    private final HomeController homeController;
    private final int padding = 15;
    private final Font uiFont = new Font("Meiryo UI", Font.BOLD, 14);

    //Components
    private final JPanel navPanel;
    private final JPanel contentPanel;
    private final SpringLayout contentPanelLayout;
    private final JPanel statsPanel;
    private final JLabel entryCountLabel = new JLabel();
    private final JLabel sourceCountLabel = new JLabel();
    private final JLabel imageCountLabel = new JLabel();
    private final JLabel todayEntryCountLabel = new JLabel();
    private final JLabel averageEntriesLabel = new JLabel();

    //Constructor
    public HomeView(WindowObject mainWindow, boolean isStartup) {
        this.mainWindow = mainWindow;
        navPanel = this.mainWindow.createNavPanel();
        contentPanel = this.mainWindow.createContentPanel(navPanel);
        contentPanelLayout = (SpringLayout) contentPanel.getLayout();
        homeController = new HomeController(this);

        statsPanel = createStatsPanel();
        createHomeView();

        Dimension windowSize = new Dimension(
                (navPanel.getWidth() * 2), (int)(navPanel.getHeight() + statsPanel.getHeight() * 1.6));
        mainWindow.setWindowSize(windowSize);

        if(isStartup) {
            mainWindow.centerWindow();
        }
    }

    //Getters
    public Database getDatabase() {
        return mainWindow.getDatabase();
    }
    public JLabel getEntryCountLabel() {
        return entryCountLabel;
    }
    public JLabel getSourceCountLabel() {
        return sourceCountLabel;
    }
    public JLabel getImageCountLabel() {
        return imageCountLabel;
    }
    public JLabel getTodayEntryCountLabel() {return todayEntryCountLabel;}
    public JLabel getAverageEntriesLabel() {return averageEntriesLabel;}

    //Sets the statsPanel within the frame's content panel
    private void createHomeView() {
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, statsPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.WEST, statsPanel, 0,
                SpringLayout.WEST, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, statsPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.EAST, contentPanel, 0,
                SpringLayout.EAST, statsPanel
        );
        contentPanel.add(statsPanel);
        setStatsPanelStyling();

        mainWindow.packFrame();

    }

    //Instantiation and layout of statsPanel, displays general stats about current collection
    private JPanel createStatsPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.EAST, entryCountLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, entryCountLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(entryCountLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceCountLabel, 0,
                SpringLayout.WEST, entryCountLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceCountLabel, padding,
                SpringLayout.SOUTH, entryCountLabel
        );
        returnPanel.add(sourceCountLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, imageCountLabel, 0,
                SpringLayout.WEST, entryCountLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, imageCountLabel, padding,
                SpringLayout.SOUTH, sourceCountLabel
        );
        returnPanel.add(imageCountLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, todayEntryCountLabel, 0,
                SpringLayout.WEST, entryCountLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, todayEntryCountLabel, padding,
                SpringLayout.SOUTH, imageCountLabel
        );
        returnPanel.add(todayEntryCountLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, averageEntriesLabel, 0,
                SpringLayout.WEST, entryCountLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, averageEntriesLabel, padding,
                SpringLayout.SOUTH, todayEntryCountLabel
        );
        returnPanel.add(averageEntriesLabel);

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, averageEntriesLabel
        );

        return returnPanel;
    }

    //Styles components within statsPanel
    private void setStatsPanelStyling() {
        entryCountLabel.setFont(uiFont);
        sourceCountLabel.setFont(uiFont);
        imageCountLabel.setFont(uiFont);
        todayEntryCountLabel.setFont(uiFont);
        averageEntriesLabel.setFont(uiFont);
    }
}
