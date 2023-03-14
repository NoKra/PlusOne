package views;

import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;

public class BrowseView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;
    private final int padding = 20;

    public BrowseView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContentContainer();
        this.layout = this.mainWindow.getLayout();

        createView();
    }

    public void createView() {

        JPanel navPanel = createNavbar();

        layout.putConstraint(
                SpringLayout.NORTH, navPanel, 0,
                SpringLayout.NORTH, container
        );
        layout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, navPanel
        );

        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, navPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        /*

        layout.putConstraint(
                SpringLayout.WEST, navPanel, 0,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.EAST, container, 0,
                SpringLayout.EAST, navPanel
        );
         */
        container.add(navPanel);


        mainWindow.packFrame();
        mainWindow.setWindowVisible();
    }

    public JPanel createNavbar() {

        JPanel buttonPanel = new JPanel();
        SpringLayout buttonLayout = new SpringLayout();
        buttonPanel.setLayout(buttonLayout);

        JButton mainButton = new JButton("Main");
        buttonLayout.putConstraint(
                SpringLayout.WEST, mainButton, padding,
                SpringLayout.WEST, buttonPanel
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, mainButton, padding,
                SpringLayout.NORTH, buttonPanel
        );
        buttonPanel.add(mainButton);

        JButton browseButton = new JButton("Browse");
        buttonLayout.putConstraint(
                SpringLayout.WEST, browseButton, padding,
                SpringLayout.EAST, mainButton
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, browseButton, padding,
                SpringLayout.NORTH, buttonPanel
        );
        buttonPanel.add(browseButton);

        JButton addButton = new JButton("Add");
        buttonLayout.putConstraint(
                SpringLayout.WEST, addButton, padding,
                SpringLayout.EAST, browseButton
        );
        buttonLayout.putConstraint(
                SpringLayout.NORTH, addButton, padding,
                SpringLayout.NORTH, buttonPanel
        );
        buttonPanel.add(addButton);

        buttonLayout.putConstraint(
                SpringLayout.EAST, buttonPanel, padding,
                SpringLayout.EAST, addButton
        );
        buttonLayout.putConstraint(
                SpringLayout.SOUTH, buttonPanel, padding,
                SpringLayout.SOUTH, browseButton
        );

        JPanel navPanel = new JPanel();
        SpringLayout navLayout = new SpringLayout();
        navPanel.setLayout(navLayout);


        navLayout.putConstraint(
                SpringLayout.WEST, buttonPanel, 0,
                SpringLayout.WEST, navPanel
        );
        navLayout.putConstraint(
                SpringLayout.EAST, navPanel, 0,
                SpringLayout.EAST, buttonPanel
        );
        navLayout.putConstraint(
                SpringLayout.NORTH, buttonPanel, 0,
                SpringLayout.NORTH, navPanel
        );

        navLayout.putConstraint(
                SpringLayout.SOUTH, navPanel, 0,
                SpringLayout.SOUTH, buttonPanel
        );
        navPanel.add(buttonPanel);

        return navPanel;
    }
}
