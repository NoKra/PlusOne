package controllers;

import settings.Settings;
import views.InitializationDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitializationController {
    private final InitializationDialog initializationDialog;
    private final Settings settings;

    //Components
    private final JButton defaultButton;
    private final JButton customButton;
    private final JButton databaseLocationButton;
    private final JTextField databaseLocationPathField;
    private final JCheckBox sameSaveLocationCheck;
    private final JButton imageLocationButton;
    private final JTextField imageLocationPathField;
    private final JButton cancelButton;
    private final JButton confirmButton;


    //Constructor
    public InitializationController(InitializationDialog parentView, Settings inSettings) {
        initializationDialog = parentView;
        settings = inSettings;
        defaultButton = initializationDialog.getDefaultButton();
        customButton = initializationDialog.getCustomButton();
        databaseLocationButton = initializationDialog.getDatabaseLocationButton();
        databaseLocationPathField = initializationDialog.getDatabaseLocationPathField();
        sameSaveLocationCheck = initializationDialog.getSameSaveLocationCheck();
        imageLocationButton = initializationDialog.getImageLocationButton();
        imageLocationPathField = initializationDialog.getImageLocationPathField();
        cancelButton = initializationDialog.getCancelButton();
        confirmButton = initializationDialog.getConfirmButton();

        setPromptPanelActions();
        setCustomPanelActions();
    }

    //Creates user settings using default locations (local file)
    private void createDefaultUserSettings() {
        String imagePath = settings.getDefaultDatabaseLocation() + "images/";
        settings.initializeSettingsJSON(settings.getDefaultDatabaseLocation(), imagePath);
        initializationDialog.destroyInitializationDialog();
    }

    //Component actions for prompt panel
    private void setPromptPanelActions() {
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDefaultUserSettings();
            }
        });

        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializationDialog.createCustomView();
            }
        });
    }

    //Component actions for custom panel
    private void setCustomPanelActions() {
        databaseLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDatabasePath();
            }
        });

        sameSaveLocationCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sameSaveLocationCheck.isSelected()) {
                    imageLocationButton.setEnabled(false);
                    imageLocationPathField.setText(databaseLocationPathField.getText());
                    imageLocationPathField.setEditable(false);
                }
                else {
                    imageLocationButton.setEnabled(true);
                    imageLocationPathField.setEditable(true);
                }
            }
        });

        imageLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setImagePath();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializationDialog.createPromptView();
                if(sameSaveLocationCheck.isSelected()) {
                    sameSaveLocationCheck.doClick();
                }
                databaseLocationPathField.setText(initializationDialog.getEmptyPathMessage());
                imageLocationPathField.setText(initializationDialog.getEmptyPathMessage());
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCustomUserSettings();
            }
        });
    }

    //Provides JFileChooser to set database JTextField as target directory
    private void setDatabasePath() {
        File databasePath;
        JFileChooser databaseLocationChooser = new JFileChooser();
        databaseLocationChooser.setDialogTitle("Choose Database Save Location");
        databaseLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int chooserVal = databaseLocationChooser.showDialog(initializationDialog.getCustomPanel(), "Select");

        if(chooserVal == JFileChooser.APPROVE_OPTION) {
            databasePath = databaseLocationChooser.getSelectedFile();
            databaseLocationPathField.setText(databasePath.toString());
            if(sameSaveLocationCheck.isSelected()) {
                imageLocationPathField.setText(databasePath.toString());
            }
        }
    }

    //Provides JFileChooser to set image JTextField as target directory
    private void setImagePath() {
        File imagePath;
        JFileChooser imageLocationChooser = new JFileChooser();
        imageLocationChooser.setDialogTitle("Choose Image Save Location");
        imageLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int chooserVal = imageLocationChooser.showDialog(initializationDialog.getCustomPanel(), "Select");

        if(chooserVal == JFileChooser.APPROVE_OPTION) {
            imagePath = imageLocationChooser.getSelectedFile();
            imageLocationPathField.setText(imagePath.toString());
        }
    }

    //Saves paths provided in JTextFields as setting path for database and image path
    private void createCustomUserSettings() {
        if(verifyPaths()) {
            String databasePath = databaseLocationPathField.getText() + "/";
            String imagePath;
            if(sameSaveLocationCheck.isSelected()) {
                imagePath = databasePath + "images/";
            } else {
                imagePath = imageLocationPathField.getText() + "/";
            }

            settings.initializeSettingsJSON(databasePath, imagePath);
            initializationDialog.destroyInitializationDialog();
        }
    }

    //Ensures that paths provided in JTextFields are valid directory paths
    public boolean verifyPaths() {
        boolean goodPaths = true;
        Path databasePath = Paths.get(databaseLocationPathField.getText());
        Path imagePath = Paths.get(imageLocationPathField.getText());
        if(!Files.exists(databasePath)) {
            goodPaths = false;
            initializationDialog.databasePathWarning(true);
        } else {
            initializationDialog.databasePathWarning(false);
        }
        if(!Files.exists(imagePath)) {
            goodPaths = false;
            initializationDialog.imagePathWarning(true);
        } else {
            initializationDialog.imagePathWarning(false);
        }
        return goodPaths;
    }
}
