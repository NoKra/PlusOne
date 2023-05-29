package views;

import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;

public class BrowseView {
    private final WindowObject mainWindow;
    private final JPanel navPanel;
    private final JPanel contentPanel;
    private final SpringLayout contentLayout;
    private final Font uiFont = new Font("Meiryo UI", Font.BOLD, 14);
    private final Color backgroundGray = new Color(47, 47, 49);
    private final int padding = 20;

    //Components
    private final JPanel searchPanel;
    private final JLabel searchLabel = new JLabel("Search");

    private final JTextField searchTextField = new JTextField();
    private final JTable resultTable = new JTable();
    private final JComboBox<String> sourceTypeCombo = new JComboBox<>();
    private final JTextField sourceNameField = new JTextField();
    private final JTextField sourceUrlField = new JTextField();
    private final JTextField sentenceField = new JTextField();



    public BrowseView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        navPanel = this.mainWindow.createNavPanel();
        contentPanel = this.mainWindow.createContentPanel(navPanel);
        contentLayout = (SpringLayout) contentPanel.getLayout();

        searchPanel = createSearchPanel();

        createView();
    }

    public void createView() {

        contentLayout.putConstraint(
                SpringLayout.WEST, searchPanel, padding,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, searchPanel, padding,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(searchPanel);

        contentLayout.putConstraint(
                SpringLayout.EAST, contentPanel, padding,
                SpringLayout.EAST, searchPanel
        );
        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, padding,
                SpringLayout.SOUTH, searchPanel
        );

        mainWindow.packFrame();
        mainWindow.getMainFrame().setMinimumSize(new Dimension(navPanel.getWidth(), navPanel.getHeight()));
    }

    public JPanel createSearchPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, searchLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, searchLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(searchLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, searchTextField, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, searchTextField, 0,
                SpringLayout.SOUTH, searchLabel
        );
        returnPanel.add(searchTextField);

        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, searchTextField
        );

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, searchTextField
        );

        setSearchPanelStyling(returnPanel);

        return returnPanel;
    }

    public void setSearchPanelStyling(JPanel panel) {
        panel.setBackground(backgroundGray);
        searchTextField.setFont(uiFont);
        searchTextField.setColumns(30);
    }


}
