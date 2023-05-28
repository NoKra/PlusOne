package views;

import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;

public class BrowseView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;
    private final int padding = 20;

    //Components
    private final JTextField searchTextField = new JTextField();
    private final JTable resultTable = new JTable();
    private final JComboBox<String> sourceTypeCombo = new JComboBox<>();
    private final JTextField sourceNameField = new JTextField();
    private final JTextField sourceUrlField = new JTextField();
    private final JTextField sentenceField = new JTextField();



    public BrowseView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContainer();
        this.layout = this.mainWindow.getLayout();

        createView();
    }

    public void createView() {
        JPanel navPanel = mainWindow.createNavPanel();
        JPanel contentPanel = mainWindow.createContentPanel(navPanel);
        SpringLayout contentLayout = (SpringLayout)contentPanel.getLayout();

        JLabel something = new JLabel("Something here");
        contentLayout.putConstraint(
                SpringLayout.WEST, something, padding,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, something, padding,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(something);

        contentLayout.putConstraint(
                SpringLayout.EAST, contentPanel, padding,
                SpringLayout.EAST, something
        );
        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, padding,
                SpringLayout.SOUTH, something
        );

        mainWindow.packFrame();
        mainWindow.getMainFrame().setMinimumSize(new Dimension(navPanel.getWidth(), navPanel.getHeight()));
    }

}
