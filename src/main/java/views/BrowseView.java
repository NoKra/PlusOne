package views;

import controllers.BrowseController;
import settings.Settings;
import window_object.WindowObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BrowseView {
    private final WindowObject mainWindow;
    private final Settings settings;
    private final JPanel navPanel;
    private final JPanel contentPanel;
    private final SpringLayout contentLayout;
    private final int generalPadding = 20;
    private final int labelPadding = 5;
    private final int columnSize = 30;
    private final DefaultTableModel resultTableModel = new DefaultTableModel(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    //Components
    private final JPanel searchPanel;
    private final JLabel searchLabel = new JLabel("Search");

    private final JTextField searchTextField = new JTextField();
    private final JTable resultTable = new JTable(resultTableModel);
    private final JScrollPane resultScroll = new JScrollPane(resultTable);
    private final JPanel editPanel;
    private final JLabel sourceTypeLabel = new JLabel("Source Type");
    private final JComboBox<String> sourceTypeCombo;
    private final JLabel sourceNameLabel = new JLabel("Source Name");
    private final JTextArea sourceNameArea = new JTextArea();
    private final JLabel sourceUrlLabel = new JLabel("Source URL");
    private final JTextArea sourceUrlArea = new JTextArea();
    private final JLabel sentenceLabel = new JLabel("Sentence");
    private final JTextArea sentenceArea = new JTextArea();



    public BrowseView(WindowObject passedWindow) {
        mainWindow = passedWindow;
        settings = mainWindow.getSettings();
        navPanel = mainWindow.createNavPanel();
        sourceTypeCombo = new JComboBox<>(settings.getSourceTypes());
        contentPanel = mainWindow.createContentPanel(navPanel);
        contentLayout = (SpringLayout) contentPanel.getLayout();
        new BrowseController(this, mainWindow.getDatabase());

        searchPanel = createSearchPanel();
        editPanel = createEditPanel();


        createView();
        setSearchPanelStyling(searchPanel);
        setEditPanelStyling(editPanel);
    }

    public DefaultTableModel getResultTableModel() {return resultTableModel;}
    public JTable getResultTable() {return resultTable;}
    public JComboBox<String> getSourceTypeCombo() {return sourceTypeCombo;}


    private void createView() {

        JPanel topPanel = new JPanel();
        contentLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, topPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, topPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(topPanel);

        contentLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, searchPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, searchPanel, generalPadding,
                SpringLayout.SOUTH, topPanel

        );
        contentPanel.add(searchPanel);

        contentLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, editPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, editPanel, generalPadding,
                SpringLayout.SOUTH, searchPanel
        );
        contentPanel.add(editPanel);

        JPanel bottomPanel = new JPanel();
        contentLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, bottomPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, bottomPanel, 0,
                SpringLayout.SOUTH, editPanel
        );
        contentPanel.add(bottomPanel);

        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, generalPadding,
                SpringLayout.SOUTH, bottomPanel
        );
        mainWindow.packFrame();

        Dimension windowDimension = new Dimension((int)(contentPanel.getWidth() * 1.2), (int)(contentPanel.getHeight()));
        mainWindow.setWindowSize(windowDimension);
    }

    private JPanel createSearchPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, searchLabel, generalPadding,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, searchLabel, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(searchLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, searchTextField, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, searchTextField, labelPadding,
                SpringLayout.SOUTH, searchLabel
        );
        returnPanel.add(searchTextField);

        panelLayout.putConstraint(
                SpringLayout.WEST, resultScroll, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, resultScroll, generalPadding,
                SpringLayout.SOUTH, searchTextField
        );
        returnPanel.add(resultScroll);

        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, generalPadding,
                SpringLayout.EAST, searchTextField
        );

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, generalPadding,
                SpringLayout.SOUTH, resultScroll
        );

        return returnPanel;
    }

    private void setSearchPanelStyling(JPanel panel) {
        panel.setBackground(settings.pickColor(Settings.Colors.backgroundGray));

        searchLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        searchLabel.setForeground(Color.WHITE);

        searchTextField.setFont(settings.pickFont(Settings.Fonts.uiFont));
        searchTextField.setColumns(columnSize + 5);
        searchTextField.setMaximumSize(new Dimension()); //Prevents vertical stretching

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension resultScrollDim = new Dimension(searchTextField.getWidth(), navPanel.getHeight() * 2);
                resultScroll.setMinimumSize(resultScrollDim);
                resultScroll.setPreferredSize(resultScrollDim);

                searchPanel.revalidate();
                searchPanel.repaint();
                editPanel.revalidate();
                editPanel.repaint();
            }
        });
    }

    private JPanel createEditPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceTypeLabel, generalPadding,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sourceTypeLabel, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sourceTypeLabel);

        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceTypeCombo, labelPadding,
                SpringLayout.SOUTH, sourceTypeLabel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sourceTypeCombo, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sourceTypeCombo);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceNameLabel, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceNameLabel, generalPadding,
                SpringLayout.SOUTH, sourceTypeCombo
        );
        returnPanel.add(sourceNameLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceNameArea, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceNameArea, labelPadding,
                SpringLayout.SOUTH, sourceNameLabel
        );
        returnPanel.add(sourceNameArea);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceUrlLabel, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceUrlLabel, generalPadding,
                SpringLayout.SOUTH, sourceNameArea
        );
        returnPanel.add(sourceUrlLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceUrlArea, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceUrlArea, labelPadding,
                SpringLayout.SOUTH, sourceUrlLabel
        );
        returnPanel.add(sourceUrlArea);

        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceLabel, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceLabel, generalPadding,
                SpringLayout.SOUTH, sourceUrlArea
        );
        returnPanel.add(sentenceLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceArea, generalPadding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceArea, labelPadding,
                SpringLayout.SOUTH, sentenceLabel
        );
        returnPanel.add(sentenceArea);

        //South and West panel constraints
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, generalPadding,
                SpringLayout.EAST, sourceNameArea
        );

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, generalPadding,
                SpringLayout.SOUTH, sentenceArea
        );

        return returnPanel;
    }

    private void setEditPanelStyling(JPanel panel) {
        panel.setBackground(settings.pickColor(Settings.Colors.backgroundGray));

        sourceTypeLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sourceTypeLabel.setForeground(Color.WHITE);

        sourceTypeCombo.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sourceTypeCombo.setMaximumSize(new Dimension()); //Prevents vertical stretching

        sourceNameLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sourceNameLabel.setForeground(Color.WHITE);

        sourceNameArea.setFont(settings.pickFont(Settings.Fonts.jpFont));
        sourceNameArea.setColumns(columnSize);
        sourceNameArea.setMaximumSize(new Dimension()); //Prevents vertical stretching

        sourceUrlLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sourceUrlLabel.setForeground(Color.WHITE);

        sourceUrlArea.setFont(settings.pickFont(Settings.Fonts.jpFont));
        sourceUrlArea.setColumns(columnSize);
        sourceUrlArea.setMaximumSize(new Dimension()); //Prevents vertical stretching

        sentenceLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sentenceLabel.setForeground(Color.WHITE);

        sentenceArea.setFont(settings.pickFont(Settings.Fonts.jpFont));
        sentenceArea.setColumns(columnSize);
        sentenceArea.setMaximumSize(new Dimension());

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
