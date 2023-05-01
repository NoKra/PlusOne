package views;

import javax.swing.*;
import java.awt.*;

public class InitializationView {
    private final JFrame initialFrame = new JFrame();
    private final Container container = initialFrame.getContentPane();
    private final SpringLayout layout = new SpringLayout();
    private final JPanel contentPanel = new JPanel();
    private int padding = 20;

    public InitializationView() {
        container.setLayout(layout);
        //TODO: Using global_settings, figure out how to save fonts ect. to a json file
        createContentPanel();
        createView();

        initialFrame.setTitle("Initialization");
        initialFrame.setLocationRelativeTo(null);
        initialFrame.setVisible(true);
        initialFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createContentPanel() {
        contentPanel.setLayout(new SpringLayout());
        layout.putConstraint(
                SpringLayout.WEST, contentPanel, 0,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.EAST, container, 0,
                SpringLayout.EAST, contentPanel
        );
        layout.putConstraint(
                SpringLayout.NORTH, contentPanel, 0,
                SpringLayout.NORTH, container
        );
        layout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, contentPanel
        );
        container.add(contentPanel);
    }

    private void createView() {
        SpringLayout panelLayout = (SpringLayout) contentPanel.getLayout();
        JLabel welcomeLabel = new JLabel("Welcome to Plus One");

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, welcomeLabel, padding,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(welcomeLabel);

        JLabel savingInfoLabel = new JLabel("Plus One saves databases and uploaded images locally");

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, savingInfoLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, savingInfoLabel, padding,
                SpringLayout.SOUTH, welcomeLabel
        );
        contentPanel.add(savingInfoLabel);

        JLabel savingChoiceLabel = new JLabel("Please choose where to save");
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, savingChoiceLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, savingChoiceLabel, padding,
                SpringLayout.SOUTH, savingInfoLabel
        );
        contentPanel.add(savingChoiceLabel);

        JButton defaultButton = new JButton("Default");
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, defaultButton, 0,
                SpringLayout.WEST, savingChoiceLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, defaultButton, padding,
                SpringLayout.SOUTH, savingChoiceLabel
        );
        contentPanel.add(defaultButton);

        JButton customButton = new JButton("Custom");
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, customButton, 0,
                SpringLayout.EAST, savingChoiceLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, customButton, padding,
                SpringLayout.SOUTH, savingChoiceLabel
        );
        contentPanel.add(customButton);

        JLabel defaultLabel = new JLabel("(Application folder)");
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, defaultLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, defaultButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, defaultLabel, 0,
                SpringLayout.SOUTH, defaultButton
        );
        contentPanel.add(defaultLabel);

        JLabel customLabel = new JLabel("(Choose folder)");
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, customLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, customButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, customLabel, 0,
                SpringLayout.SOUTH, customButton
        );
        contentPanel.add(customLabel);

        panelLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, padding,
                SpringLayout.SOUTH, defaultLabel
        );

        initialFrame.pack();

        Dimension windowSize = new Dimension((int)(savingInfoLabel.getWidth() * 1.25), welcomeLabel.getHeight() * 2);
        initialFrame.setMinimumSize(windowSize);
        initialFrame.setPreferredSize(windowSize);

    }
}
