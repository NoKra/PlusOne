package views;

import controllers.AddSentenceControl;
import controllers.SetLinkController;
import window_object.WindowObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SetLinkView {
    private final AddSentenceControl addSentenceControl;
    private final WindowObject setWindow;
    private final Container container;
    private final SpringLayout layout;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    private final int padding = 20;
    //Disables editing for all cells
    DefaultTableModel linkTableModel = new DefaultTableModel(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable sentenceTable;
    private final SetLinkController linkController;

    public SetLinkView(WindowObject mainWindow, AddSentenceControl addSentenceControl) {
        this.addSentenceControl = addSentenceControl;
        setWindow = new WindowObject(mainWindow.getDatabase(), false);
        container = setWindow.getContainer();
        layout = setWindow.getLayout();

        setWindow.setWindowVisible();
        linkController = new SetLinkController(mainWindow.getDatabase(), this);
        createView();
    }

    public DefaultTableModel getLinkTableModel() {
        return linkTableModel;
    }

    private void createView() {
        //SearchBar
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, searchLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, searchLabel, padding,
                SpringLayout.NORTH, container
        );
        container.add(searchLabel);

        JTextField searchField = new JTextField();
        searchField.setFont(jpFont);
        //TODO: Set column count correctly later
        searchField.setColumns(30);
        layout.putConstraint(
                SpringLayout.WEST, searchField, padding / 2,
                SpringLayout.EAST, searchLabel
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, searchField, 0,
                SpringLayout.VERTICAL_CENTER, searchLabel
        );
        container.add(searchField);


        //Table
        sentenceTable = new JTable(linkTableModel);
        sentenceTable.setFont(jpFont);
        ListSelectionModel selectionModel = sentenceTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sentenceTable.getTableHeader().setReorderingAllowed(false);
        sentenceTable.setCellSelectionEnabled(false);
        sentenceTable.setRowSelectionAllowed(true);
        sentenceTable.setDefaultRenderer(Object.class, new NoBorderRenderer());
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    linkController.selectionChanged();
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    linkController.sentenceSearch(searchField.getText());
                }
            }
        });

        JScrollPane tableScroll = new JScrollPane(sentenceTable);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, tableScroll, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, tableScroll, padding,
                SpringLayout.SOUTH, searchLabel
        );
        container.add(tableScroll);

        //Selected info
        //Selected Id
        JLabel idFrontLabel = new JLabel("Selected Id: ");
        idFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, idFrontLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, idFrontLabel, padding,
                SpringLayout.SOUTH, tableScroll
        );
        container.add(idFrontLabel);

        JLabel idValueLabel = new JLabel("null id");
        idValueLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, idValueLabel, padding,
                SpringLayout.EAST, idFrontLabel
                );
        layout.putConstraint(
                SpringLayout.NORTH, idValueLabel, padding,
                SpringLayout.SOUTH, tableScroll
        );
        container.add(idValueLabel);

        //Selected source type
        JLabel typeFrontLabel = new JLabel("Source Type: ");
        typeFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, typeFrontLabel, padding,
                SpringLayout.EAST, idValueLabel
        );
        layout.putConstraint(
                SpringLayout.NORTH, typeFrontLabel, padding,
                SpringLayout.SOUTH, tableScroll
        );
        container.add(typeFrontLabel);

        JLabel typeValueLabel = new JLabel("null type");
        typeValueLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, typeValueLabel, padding,
                SpringLayout.EAST, typeFrontLabel
        );
        layout.putConstraint(
                SpringLayout.NORTH, typeValueLabel, padding,
                SpringLayout.SOUTH, tableScroll
        );
        container.add(typeValueLabel);

        //Selected source name
        JLabel nameFrontLabel = new JLabel("Source Name: ");
        nameFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, nameFrontLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, nameFrontLabel, padding,
                SpringLayout.SOUTH, typeFrontLabel
        );
        container.add(nameFrontLabel);

        JLabel nameValueLabel = new JLabel("null name");
        nameValueLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, nameValueLabel, padding,
                SpringLayout.EAST, nameFrontLabel
        );
        layout.putConstraint(
                SpringLayout.NORTH, nameValueLabel, padding,
                SpringLayout.SOUTH, typeFrontLabel
        );
        container.add(nameValueLabel);

        //Selected source url?
        //TODO: Maybe set some logic to remove url if none?
        JLabel urlFrontLabel = new JLabel("Source URL: ");
        urlFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, urlFrontLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, urlFrontLabel, padding,
                SpringLayout.SOUTH, nameValueLabel
        );
        container.add(urlFrontLabel);

        JLabel urlValueLabel = new JLabel("null url");
        urlValueLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, urlValueLabel, padding,
                SpringLayout.EAST, urlFrontLabel
        );
        layout.putConstraint(
                SpringLayout.NORTH, urlValueLabel, padding,
                SpringLayout.SOUTH, nameValueLabel
        );
        container.add(urlValueLabel);

        //Selected source sentence
        JLabel sentenceFrontLabel = new JLabel("Sentence: ");
        sentenceFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sentenceFrontLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sentenceFrontLabel, padding,
                SpringLayout.SOUTH, urlFrontLabel
        );
        container.add(sentenceFrontLabel);

        JTextArea sentenceValueArea = new JTextArea("null sentence");
        sentenceValueArea.setEditable(false);
        sentenceValueArea.setBackground(null);
        sentenceValueArea.setWrapStyleWord(true);
        sentenceValueArea.setLineWrap(true);
        //TODO: Find an equation for auto column width
        sentenceValueArea.setColumns(45);
        sentenceValueArea.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sentenceValueArea, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sentenceValueArea, padding,
                SpringLayout.SOUTH, sentenceFrontLabel
        );
        container.add(sentenceValueArea);

        //Selected source backlink
        JLabel linkFrontLabel = new JLabel("Backlink: ");
        linkFrontLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, linkFrontLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, linkFrontLabel, padding,
                SpringLayout.SOUTH, sentenceValueArea
        );
        container.add(linkFrontLabel);

        JButton selectBacklinkButton = new JButton("Go To Link");
        selectBacklinkButton.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, selectBacklinkButton, padding,
                SpringLayout.EAST, linkFrontLabel
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, selectBacklinkButton, 0,
                SpringLayout.VERTICAL_CENTER, linkFrontLabel
        );
        container.add(selectBacklinkButton);
        selectBacklinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkController.selectSelectedBacklink();
            }
        });
        selectBacklinkButton.setEnabled(false);

        JLabel linkValueLabel = new JLabel("null link");
        linkValueLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, linkValueLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, linkValueLabel, padding,
                SpringLayout.SOUTH, linkFrontLabel
        );
        container.add(linkValueLabel);

        JButton selectLinkButton = new JButton("Select");
        selectLinkButton.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, selectLinkButton, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, selectLinkButton, padding,
                SpringLayout.SOUTH, linkValueLabel
        );
        container.add(selectLinkButton);

        selectLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSentenceControl.setBacklinkId(Integer.parseInt(idValueLabel.getText()), sentenceValueArea.getText());
                setWindow.destroyWindow();
            }
        });

        linkController.setComponents(sentenceTable, idValueLabel, typeValueLabel, nameValueLabel,
                urlValueLabel, sentenceValueArea, selectBacklinkButton, linkValueLabel);
        linkController.initializeTable();
        //sizeTable();
    }

    private static class NoBorderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    /*
    private void sizeTable() {
        int columns = sentenceTable.getColumnCount();
        System.out.println(tableWidth);
        for(int i = 0; i < columns; i++) {
            sentenceTable.getColumnModel().getColumn(i).setMaxWidth(tableWidth);
            sentenceTable.getColumnModel().getColumn(i).setMinWidth(tableWidth / 100);
        }
        sentenceTable.getColumnModel().getColumn(0).setPreferredWidth(tableWidth / 20);
        sentenceTable.getColumnModel().getColumn(1).setPreferredWidth(tableWidth / 10);
        sentenceTable.getColumnModel().getColumn(2).setPreferredWidth(tableWidth / 5);
        sentenceTable.getColumnModel().getColumn(3).setPreferredWidth(tableWidth / 10);
        sentenceTable.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
        sentenceTable.getColumnModel().getColumn(5).setPreferredWidth(tableWidth / 20);

    }
     */
}
