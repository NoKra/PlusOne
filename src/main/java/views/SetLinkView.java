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
import java.awt.event.*;

public class SetLinkView {
    private final AddSentenceControl addSentenceControl;
    private final WindowObject setWindow;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    private final int padding = 20;
    //Disables editing for all cells

    DefaultTableModel linkTableModel = new DefaultTableModel(){
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final SetLinkController linkController;

    //New Stuff
    private JPanel contentPanel;
    private SpringLayout contentPanelLayout;
    private JPanel searchPanel;
    private JLabel searchLabel = new JLabel("Search: ");
    private JTextField searchField = new JTextField();
    private JPanel tablePanel;
    private JTable sentenceTable = new JTable(linkTableModel);
    private JScrollPane tableScroll = new JScrollPane(sentenceTable);
    private JPanel selectedInfoPanel;
    private JLabel idPretextLabel = new JLabel("Selected ID: ");
    private JLabel idValueLabel = new JLabel("null id");
    private JLabel typePretextLabel = new JLabel("Source Type: ");
    private JLabel typeValueLabel = new JLabel("null type");
    private JLabel namePretextLabel = new JLabel("Source Name: ");
    private JLabel nameValueLabel = new JLabel("null name");
    private JLabel urlPretextLabel = new JLabel("Source URL: ");
    private JLabel urlValueLabel = new JLabel("null URL");
    private JLabel sentencePretextLabel = new JLabel("Sentence: ");
    private JTextArea sentenceValueArea = new JTextArea("null sentence");
    private JLabel linkPretextLabel = new JLabel("Backlink: ");
    private JButton selectBacklinkButton = new JButton("Go to Link");
    private JLabel linkValueLabel = new JLabel("null link");
    private JButton selectLinkButton = new JButton("Select");

    public SetLinkView(WindowObject mainWindow, AddSentenceControl addSentenceControl) {
        this.addSentenceControl = addSentenceControl;
        setWindow = new WindowObject(mainWindow.getDatabase(), false);
        createView();
        setWindow.setWindowVisible();
        setWindow.getMainFrame().setLocationRelativeTo(null);
        linkController = new SetLinkController(mainWindow.getDatabase(), this);
    }

    public DefaultTableModel getLinkTableModel() {
        return linkTableModel;
    }
    public JTable getSentenceTable() {return sentenceTable;}
    public JLabel getIdValueLabel() {return idValueLabel;}
    public JLabel getTypeValueLabel() {return typeValueLabel;}
    public JLabel getNameValueLabel() {return nameValueLabel;}
    public JLabel getUrlValueLabel() {return urlValueLabel;}
    public JTextArea getSentenceValueArea() {return sentenceValueArea;}
    public JButton getSelectBacklinkButton() {return selectBacklinkButton;}
    public JLabel getLinkValueLabel() {return linkValueLabel;}



    private void createView() {
        contentPanel = setWindow.createContentPanel();
        contentPanelLayout = (SpringLayout)contentPanel.getLayout();
        //empty topPanel prevents stretching of fields in other panels
        JPanel topPanel = new JPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, topPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, topPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(topPanel);
        topPanel.setBackground(Color.BLUE);

        searchPanel = createSearchPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, searchPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, searchPanel, 0,
                SpringLayout.SOUTH, topPanel
        );
        contentPanel.add(searchPanel);

        tablePanel = createTablePanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, tablePanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, tablePanel, 0,
                SpringLayout.SOUTH, searchPanel
        );
        contentPanel.add(tablePanel);

        selectedInfoPanel = createSelectedInfoPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, selectedInfoPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, selectedInfoPanel, 0,
                SpringLayout.SOUTH, tablePanel
        );
        contentPanel.add(selectedInfoPanel);

        //Constraints for EAST and SOUTH of contentPanel, EAST must be widest panel and SOUTH must be bottom panel
        //empty bottomPanel prevents stretching of fields in other panels
        JPanel bottomPanel = new JPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, bottomPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, bottomPanel, 0,
                SpringLayout.SOUTH, selectedInfoPanel
        );
        contentPanel.add(bottomPanel);
        bottomPanel.setBackground(Color.BLUE);

        contentPanelLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, bottomPanel
        );

        setWindow.packFrame();
        tableScroll.setPreferredSize(new Dimension((int)(searchPanel.getWidth() * 1.5), selectedInfoPanel.getHeight()));
        setWindow.getMainFrame().setMinimumSize(new Dimension(
                (int)(tableScroll.getPreferredSize().getWidth() * 1.5),
                (int)(contentPanel.getHeight() * 1.1)
        ));


        setWindow.getMainFrame().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.setMinimumSize(setWindow.getMainFrame().getSize());
            }
        });
    }

    private JPanel createSearchPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.WEST, searchLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, searchLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(searchLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, searchField, padding,
                SpringLayout.EAST, searchLabel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, searchField, 0,
                SpringLayout.VERTICAL_CENTER, searchLabel
        );
        returnPanel.add(searchField);

        searchField.setColumns(30);
        searchField.setMaximumSize(new Dimension());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    linkController.sentenceSearch(searchField.getText());
                    setWindow.packFrame();
                }
            }
        });

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, searchField
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, searchField
        );

        return returnPanel;
    }

    private JPanel createTablePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);


        ListSelectionModel selectionModel = sentenceTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sentenceTable.getTableHeader().setReorderingAllowed(false);
        sentenceTable.setCellSelectionEnabled(false);
        sentenceTable.setRowSelectionAllowed(false);
        sentenceTable.setDefaultRenderer(Object.class, new NoBorderRenderer());
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    linkController.selectionChanged();
                }
            }
        });

        panelLayout.putConstraint(
                SpringLayout.NORTH, tableScroll, padding,
                SpringLayout.NORTH, returnPanel
        );

        panelLayout.putConstraint(
                SpringLayout.WEST, tableScroll, 0,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(tableScroll);
        tableScroll.setMaximumSize(new Dimension());
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, tableScroll
        );

        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, 0,
                SpringLayout.EAST, tableScroll
        );


        return returnPanel;
    }

    private JPanel createSelectedInfoPanel() {
        //TODO: may need to separate this into individual panels because of horizontal size
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, idPretextLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, idPretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(idPretextLabel);

        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, idValueLabel, 0,
                SpringLayout.VERTICAL_CENTER, idPretextLabel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, idValueLabel, padding,
                SpringLayout.EAST, idPretextLabel
        );
        returnPanel.add(idValueLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, typePretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, typePretextLabel, padding,
                SpringLayout.SOUTH, idPretextLabel
        );
        returnPanel.add(typePretextLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, typeValueLabel, padding,
                SpringLayout.EAST, typePretextLabel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, typeValueLabel, 0,
                SpringLayout.VERTICAL_CENTER, typePretextLabel
        );
        returnPanel.add(typeValueLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, namePretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, namePretextLabel, padding,
                SpringLayout.SOUTH, typePretextLabel
        );
        returnPanel.add(namePretextLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, nameValueLabel, padding,
                SpringLayout.EAST, namePretextLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, nameValueLabel, padding,
                SpringLayout.SOUTH, typeValueLabel
        );
        returnPanel.add(nameValueLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, urlPretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, urlPretextLabel, padding,
                SpringLayout.SOUTH, namePretextLabel
        );
        returnPanel.add(urlPretextLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, urlValueLabel, padding,
                SpringLayout.EAST, urlPretextLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, urlValueLabel, padding,
                SpringLayout.SOUTH, nameValueLabel
        );
        returnPanel.add(urlValueLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sentencePretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentencePretextLabel, padding,
                SpringLayout.SOUTH, urlPretextLabel
        );
        returnPanel.add(sentencePretextLabel);

        sentenceValueArea.setEditable(false);
        sentenceValueArea.setBackground(null);
        sentenceValueArea.setWrapStyleWord(true);
        sentenceValueArea.setLineWrap(true);
        sentenceValueArea.setMaximumSize(new Dimension());
        sentenceValueArea.setColumns(45);
        sentenceValueArea.setRows(1);

        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceValueArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceValueArea, padding,
                SpringLayout.SOUTH, sentencePretextLabel
        );
        returnPanel.add(sentenceValueArea);

        panelLayout.putConstraint(
                SpringLayout.WEST, linkPretextLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, linkPretextLabel, padding,
                SpringLayout.SOUTH, sentenceValueArea
        );
        returnPanel.add(linkPretextLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, selectBacklinkButton, padding,
                SpringLayout.EAST, linkPretextLabel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, selectBacklinkButton, 0,
                SpringLayout.VERTICAL_CENTER, linkPretextLabel
        );
        returnPanel.add(selectBacklinkButton);

        panelLayout.putConstraint(
                SpringLayout.WEST, linkValueLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, linkValueLabel, padding,
                SpringLayout.SOUTH, selectBacklinkButton
        );
        returnPanel.add(linkValueLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, selectLinkButton, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, selectLinkButton, padding,
                SpringLayout.SOUTH, linkValueLabel
        );
        returnPanel.add(selectLinkButton);

        selectLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSentenceControl.setBacklinkId(Integer.parseInt(idValueLabel.getText()), sentenceValueArea.getText());
                setWindow.destroyWindow();
            }
        });
        selectBacklinkButton.setEnabled(false);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, 0,
                SpringLayout.EAST, sentenceValueArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, selectLinkButton
        );

        return returnPanel;
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
