package controllers;

import org.json.simple.JSONObject;
import settings.Settings;
import views.InitializationDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitializationController {
    private final InitializationDialog initializationDialog;
    private final Settings settings;
    private final String defaultPath = "./src/main/resources/onePlusDatabase/";

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
        defaultButton = parentView.getDefaultButton();
        customButton = parentView.getCustomButton();
        databaseLocationButton = parentView.getDatabaseLocationButton();
        databaseLocationPathField = parentView.getDatabaseLocationPathField();
        sameSaveLocationCheck = parentView.getSameSaveLocationCheck();
        imageLocationButton = parentView.getImageLocationButton();
        imageLocationPathField = parentView.getImageLocationPathField();
        cancelButton = parentView.getCancelButton();
        confirmButton = parentView.getConfirmButton();

        setPromptPanelComponentActions();
        setCustomPanelComponentActions();
    }

    //Creates user settings using default locations (local file)
    private void createDefaultUserSettings() {
        String imagePath = defaultPath + "images/";
        settings.initializeSettingsJSON(defaultPath, imagePath);
        initializationDialog.destroyInitializationDialog();
    }

    //Component actions for prompt panel
    private void setPromptPanelComponentActions() {
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
    private void setCustomPanelComponentActions() {
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
