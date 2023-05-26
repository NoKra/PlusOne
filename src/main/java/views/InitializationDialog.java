package views;

import controllers.InitializationController;

import javax.swing.*;
import java.awt.*;

public class InitializationDialog {
    private final InitializationController initializationController;
    private final JDialog initializationDialog = new JDialog(
            null, "Initialization", Dialog.ModalityType.APPLICATION_MODAL);
    private final Container container = initializationDialog.getContentPane();
    private final SpringLayout containerLayout = new SpringLayout();
    private final JPanel contentPanel = new JPanel();
    private final SpringLayout contentLayout = new SpringLayout();
    private int padding = 15;
    private final Font uiFont = new Font("Meiryo UI", Font.BOLD, 14);
    private final Font buttonFont = new Font("Verdana", Font.BOLD, 16);
    private final Color problemColor = new Color( 244, 81, 30 );

    //Components
    private JPanel promptPanel;
    private final String emptyPathMessage = "Choose Location";
    private final JLabel welcomeLabel = new JLabel("Welcome to Plus One");
    private final JLabel savingInfoLabel = new JLabel("Plus One saves databases and uploaded images locally");
    private final JLabel savingChoiceLabel = new JLabel("Please choose where to save");
    private final JButton defaultButton = new JButton("Default");
    private final JButton customButton = new JButton("Custom");
    private final JLabel defaultLabel = new JLabel("(Application folder)");
    private final JLabel customLabel = new JLabel("(Choose folder)");
    private JPanel customPanel;
    private final JLabel locationPrompt = new JLabel("Choose a location:");
    private final JButton databaseLocationButton = new JButton("Database");
    private final JTextField databaseLocationPathField = new JTextField(emptyPathMessage);
    private final JLabel badDatabasePathLabel = new JLabel("Database path doesn't exist");
    private final JCheckBox sameSaveLocationCheck = new JCheckBox("Database and images same location?");
    private final JButton imageLocationButton = new JButton("Images");
    private final JTextField imageLocationPathField = new JTextField(emptyPathMessage);
    private final JLabel badImagePathLabel = new JLabel("Image path doesn't exist");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton confirmButton = new JButton("Confirm");

    //Constructor
    public InitializationDialog() {
        initializationController = new InitializationController(this);
        container.setLayout(containerLayout);
        promptPanel = createPromptPanel();
        customPanel = createCustomPathPanel();
        setContentPanel();
        createPromptView();

        initializationDialog.setLocationRelativeTo(null);
        initializationDialog.setVisible(true);
        initializationDialog.setResizable(false);
        initializationDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    //Getters
    public JButton getDefaultButton() {return  defaultButton;}
    public JButton getCustomButton() {return customButton;}
    public String getEmptyPathMessage() {return emptyPathMessage;}
    public JPanel getCustomPanel() {
        return customPanel;
    }
    public JButton getDatabaseLocationButton() {return databaseLocationButton;}
    public JTextField getDatabaseLocationPathField() {return databaseLocationPathField;}
    public JCheckBox getSameSaveLocationCheck() {return sameSaveLocationCheck;}
    public JButton getImageLocationButton() {return imageLocationButton;}
    public JTextField getImageLocationPathField() {return imageLocationPathField;}
    public JButton getCancelButton() {return cancelButton;}
    public JButton getConfirmButton() {return confirmButton;}

    //Destroys this window
    public void destroyInitializationDialog() {
        initializationDialog.dispose();
    }

    //Creates base JPanel (contentPanel) to insert other JPanels into
    private void setContentPanel() {
        contentPanel.setLayout(contentLayout);
        containerLayout.putConstraint(
                SpringLayout.WEST, contentPanel, 0,
                SpringLayout.WEST, container
        );
        containerLayout.putConstraint(
                SpringLayout.EAST, container, 0,
                SpringLayout.EAST, contentPanel
        );
        containerLayout.putConstraint(
                SpringLayout.NORTH, contentPanel, 0,
                SpringLayout.NORTH, container
        );
        containerLayout.putConstraint(
                SpringLayout.SOUTH, container, 0,
                SpringLayout.SOUTH, contentPanel
        );
        container.add(contentPanel);
    }

    //Sets promptPanel within customPanel's container, relevant sizing/styling is applied
    public void createPromptView() {
        contentPanel.remove(customPanel);
        contentLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, promptPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, promptPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(promptPanel);

        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, promptPanel
        );

        setPromptPanelStyling();
        initializationDialog.pack();

        Dimension windowSize = new Dimension((int)(promptPanel.getWidth() * 1.25), (int)(promptPanel.getHeight() * 1.25));
        initializationDialog.setMinimumSize(windowSize);
        initializationDialog.setPreferredSize(windowSize);
        initializationDialog.setSize(windowSize);
    }

    // Instantiates promptPanel and positions all relevant components within promptPanel
    // promptPanel lets user decide between using the default saving scheme or
    // navigates customPanel for custom saving scheme
    private JPanel createPromptPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, welcomeLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(welcomeLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, savingInfoLabel, 0, //Widest component
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, savingInfoLabel, padding,
                SpringLayout.SOUTH, welcomeLabel
        );
        returnPanel.add(savingInfoLabel);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, savingChoiceLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, savingChoiceLabel, padding,
                SpringLayout.SOUTH, savingInfoLabel
        );
        returnPanel.add(savingChoiceLabel);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, defaultButton, 0,
                SpringLayout.WEST, savingChoiceLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, defaultButton, padding,
                SpringLayout.SOUTH, savingChoiceLabel
        );
        returnPanel.add(defaultButton);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, customButton, 0,
                SpringLayout.EAST, savingChoiceLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, customButton, padding,
                SpringLayout.SOUTH, savingChoiceLabel
        );
        returnPanel.add(customButton);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, defaultLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, defaultButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, defaultLabel, 0,
                SpringLayout.SOUTH, defaultButton
        );
        returnPanel.add(defaultLabel);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, customLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, customButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, customLabel, 0,
                SpringLayout.SOUTH, customButton
        );
        returnPanel.add(customLabel);

        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, savingInfoLabel
        );

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, defaultLabel
        );


        return returnPanel;
    }

    //Styles components within promptPanel
    private void setPromptPanelStyling() {
        welcomeLabel.setFont(uiFont);
        savingInfoLabel.setFont(uiFont);
        savingChoiceLabel.setFont(uiFont);
        defaultButton.setFont(buttonFont);
        customButton.setFont(buttonFont);
        defaultLabel.setFont(uiFont);
        customLabel.setFont(uiFont);
    }

    //Sets customPanel within customPanel's container, relevant sizing/styling is applied
    public void createCustomView() {

        contentLayout.putConstraint(
                SpringLayout.WEST, customPanel, 0,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.EAST, contentPanel, 0,
                SpringLayout.EAST, customPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, customPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, customPanel
        );
        contentPanel.add(customPanel);
        contentPanel.remove(promptPanel);
        initializationDialog.pack();

        setCustomPanelStyling();

        Dimension windowSize = new Dimension((int)(customPanel.getWidth()), (int)(customPanel.getHeight() * 1.55));
        initializationDialog.setMinimumSize(windowSize);
        initializationDialog.setPreferredSize(windowSize);

        contentPanel.repaint();
        contentPanel.revalidate();
    }

    // Instantiates customPanel and positions all relevant components within customPanel
    // Lets user decide where to save the database and any images associated with sentences
    private JPanel createCustomPathPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, locationPrompt, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, locationPrompt, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(locationPrompt);

        panelLayout.putConstraint(
                SpringLayout.WEST, databaseLocationButton, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, databaseLocationButton, padding,
                SpringLayout.SOUTH, locationPrompt
        );
        returnPanel.add(databaseLocationButton);

        panelLayout.putConstraint(
                SpringLayout.WEST, databaseLocationPathField, padding,
                SpringLayout.EAST, databaseLocationButton
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, databaseLocationPathField, 0,
                SpringLayout.VERTICAL_CENTER, databaseLocationButton
        );
        returnPanel.add(databaseLocationPathField);

        panelLayout.putConstraint(
                SpringLayout.WEST, badDatabasePathLabel, 0,
                SpringLayout.WEST, databaseLocationPathField
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, badDatabasePathLabel, 0,
                SpringLayout.SOUTH, databaseLocationPathField
        );
        returnPanel.add(badDatabasePathLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sameSaveLocationCheck, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sameSaveLocationCheck, padding,
                SpringLayout.SOUTH, databaseLocationButton
        );
        returnPanel.add(sameSaveLocationCheck);

        panelLayout.putConstraint(
                SpringLayout.WEST, imageLocationButton, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, imageLocationButton, padding,
                SpringLayout.SOUTH, sameSaveLocationCheck
        );
        returnPanel.add(imageLocationButton);

        panelLayout.putConstraint(
                SpringLayout.WEST, imageLocationPathField, padding,
                SpringLayout.EAST, imageLocationButton
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, imageLocationPathField, 0,
                SpringLayout.VERTICAL_CENTER, imageLocationButton
        );
        returnPanel.add(imageLocationPathField);

        panelLayout.putConstraint(
                SpringLayout.WEST, badImagePathLabel, 0,
                SpringLayout.WEST, imageLocationPathField
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, badImagePathLabel, 0,
                SpringLayout.SOUTH, imageLocationPathField
        );
        returnPanel.add(badImagePathLabel);

        panelLayout.putConstraint(
                SpringLayout.NORTH, cancelButton, padding,
                SpringLayout.SOUTH, badImagePathLabel
        );
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, cancelButton, -padding,
                SpringLayout.WEST, locationPrompt
        );
        returnPanel.add(cancelButton);

        panelLayout.putConstraint(
                SpringLayout.NORTH,confirmButton, padding,
                SpringLayout.SOUTH, badImagePathLabel
        );
        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, confirmButton, padding,
                SpringLayout.EAST, locationPrompt
        );
        returnPanel.add(confirmButton);


        return returnPanel;
    }

    //Styles components within customPanel
    private void setCustomPanelStyling() {
        int pathColumns = 23;
        databaseLocationPathField.setColumns(pathColumns);
        imageLocationPathField.setColumns(pathColumns);

        Dimension buttonDimension = new Dimension(
                (int)(initializationDialog.getWidth() * 0.24), (int)(initializationDialog.getHeight() * 0.15));
        databaseLocationButton.setPreferredSize(buttonDimension);
        imageLocationButton.setPreferredSize(buttonDimension);
        cancelButton.setPreferredSize(buttonDimension);
        confirmButton.setPreferredSize(buttonDimension);


        locationPrompt.setFont(uiFont);

        databaseLocationButton.setFont(buttonFont);
        databaseLocationPathField.setFont(uiFont);
        badDatabasePathLabel.setFont(uiFont);
        badDatabasePathLabel.setForeground(problemColor);
        badDatabasePathLabel.setVisible(false);


        sameSaveLocationCheck.setFont(uiFont);

        imageLocationButton.setFont(buttonFont);
        imageLocationPathField.setFont(uiFont);
        badImagePathLabel.setFont(uiFont);
        badImagePathLabel.setForeground(problemColor);
        badImagePathLabel.setVisible(false);

        cancelButton.setFont(buttonFont);
        confirmButton.setFont(buttonFont);
    }

    //Toggles the visibility status of bad database path warning
    public void databasePathWarning(boolean visibleStatus) {
        badDatabasePathLabel.setVisible(visibleStatus);
    }

    //Toggles the visibility status of bad image path warning
    public void imagePathWarning(boolean visibleStatus) {
        badImagePathLabel.setVisible(visibleStatus);
    }

}
