package views;

import controllers.AddSentenceController;
import controllers.SetLinkController;
import settings.Settings;
import window_object.WindowObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class SetLinkView {
    private final WindowObject setWindow;
    private final AddSentenceController addSentenceController;
    private final Settings settings;
    private final int padding = 20;
    private final int areaColumns = 45;

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
    private final JLabel searchLabel = new JLabel("Search: ");
    private final JTextField searchField = new JTextField();
    private JPanel tablePanel;
    private final JTable searchTable = new JTable(linkTableModel);
    private final JScrollPane tableScroll = new JScrollPane(searchTable);

    private JPanel selectedInfoPanel;
    private final JLabel idPretextLabel = new JLabel("Selected ID: ");
    private final JLabel idValueLabel = new JLabel("  ");
    private final JLabel typePretextLabel = new JLabel("Source Type: ");
    private final JLabel typeValueLabel = new JLabel("  ");
    private final JLabel namePretextLabel = new JLabel("Source Name: ");
    private final JLabel nameValueLabel = new JLabel("  ");
    private final JLabel urlPretextLabel = new JLabel("Source URL: ");
    private final JLabel urlValueLabel = new JLabel("  ");
    private final JLabel sentencePretextLabel = new JLabel("Sentence: ");
    private final JTextArea sentenceValueArea = new JTextArea("  ");
    private final JLabel linkPretextLabel = new JLabel("Backlink: ");
    private final JButton selectBacklinkButton = new JButton("Go to Link");
    private final JTextArea linkValueArea = new JTextArea("  ");
    private final JButton selectLinkButton = new JButton("Select");

    public SetLinkView(WindowObject mainWindow, AddSentenceController addSentenceController) {
        settings = mainWindow.getSettings();
        this.addSentenceController = addSentenceController;
        setWindow = new WindowObject(mainWindow.getDatabase(), mainWindow.getSettings(), false);
        createView();
        setWindow.setWindowVisible();
        setWindow.getMainFrame().setLocationRelativeTo(null);
        linkController = new SetLinkController(mainWindow.getDatabase(), this);
        if(linkController.getMaxSentences() > 0) {
            searchTable.setRowSelectionInterval(0, 0);
        } else {
            selectLinkButton.setEnabled(false);
        }
        sizeTable();
    }

    public DefaultTableModel getLinkTableModel() {
        return linkTableModel;
    }
    public JTable getSearchTable() {return searchTable;}
    public JLabel getIdValueLabel() {return idValueLabel;}
    public JLabel getTypeValueLabel() {return typeValueLabel;}
    public JLabel getNameValueLabel() {return nameValueLabel;}
    public JLabel getUrlValueLabel() {return urlValueLabel;}
    public JTextArea getSentenceValueArea() {return sentenceValueArea;}
    public JButton getSelectBacklinkButton() {return selectBacklinkButton;}
    public JTextArea getLinkValueArea() {return linkValueArea;}



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

        contentPanelLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, bottomPanel
        );

        setWindow.packFrame();
        Dimension tableScrollDim = new Dimension(
                (int)(searchPanel.getWidth() * 1.5),
                selectedInfoPanel.getHeight());
        tableScroll.setMinimumSize(tableScrollDim);
        tableScroll.setPreferredSize(tableScrollDim);
        setWindow.getMainFrame().setMinimumSize(new Dimension(
                (int)(tableScroll.getPreferredSize().getWidth() * 1.25),
                (int)(contentPanel.getHeight() * 1.1)
        ));


        setWindow.getMainFrame().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.setMinimumSize(setWindow.getMainFrame().getSize());
            }
        });
    }

    //TODO: create option for search queries based on field, i.e. "name: xxxx"
    private JPanel createSearchPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        searchLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        searchField.setFont(settings.pickFont(Settings.Fonts.jpFont));

        searchField.setColumns(30);
        searchField.setMaximumSize(new Dimension());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    linkController.sentenceSearch(searchField.getText());
                }
            }
        });

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

        searchTable.setFont(settings.pickFont(Settings.Fonts.jpFont));



        ListSelectionModel selectionModel = searchTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchTable.getTableHeader().setReorderingAllowed(false);
        searchTable.setDefaultRenderer(Object.class, new NoBorderRenderer());
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    linkController.selectionChanged();

                    //Probably not the best way, but these double invoke later are needed when sentences
                    //are larger than the current window size, otherwise window goes blank until resized
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            sentenceValueArea.repaint();
                            sentenceValueArea.revalidate();

                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    contentPanel.repaint();
                                    contentPanel.revalidate();
                                }
                            });
                        }
                    });
                }
            }
        });

        tableScroll.setMaximumSize(new Dimension());
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelLayout.putConstraint(
                SpringLayout.NORTH, tableScroll, padding,
                SpringLayout.NORTH, returnPanel
        );

        panelLayout.putConstraint(
                SpringLayout.WEST, tableScroll, 0,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(tableScroll);

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
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        idPretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        idValueLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        typePretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        typeValueLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        namePretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        nameValueLabel.setFont(settings.pickFont(Settings.Fonts.jpFont));
        urlPretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        urlValueLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sentencePretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        sentenceValueArea.setFont(settings.pickFont(Settings.Fonts.jpFont));
        linkPretextLabel.setFont(settings.pickFont(Settings.Fonts.uiFont));
        selectBacklinkButton.setFont(settings.pickFont(Settings.Fonts.buttonFont));
        linkValueArea.setFont(settings.pickFont(Settings.Fonts.jpFont));
        selectLinkButton.setFont(settings.pickFont(Settings.Fonts.buttonFont));

        sentenceValueArea.setEditable(false);
        sentenceValueArea.setBackground(null);
        sentenceValueArea.setWrapStyleWord(true);
        sentenceValueArea.setLineWrap(true);
        sentenceValueArea.setMaximumSize(new Dimension());
        sentenceValueArea.setColumns(areaColumns);
        sentenceValueArea.setRows(1);

        selectBacklinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkController.tableSelectCurrentSelectBacklink();
            }
        });

        linkValueArea.setEditable(false);
        linkValueArea.setBackground(null);
        linkValueArea.setWrapStyleWord(true);
        linkValueArea.setLineWrap(true);
        linkValueArea.setMaximumSize(new Dimension());
        linkValueArea.setColumns(areaColumns);
        linkValueArea.setRows(1);

        selectLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSentenceController.setBackLink(linkController.makeSelection());
                setWindow.destroyWindow();
            }
        });
        selectBacklinkButton.setEnabled(false);

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
                SpringLayout.VERTICAL_CENTER, nameValueLabel, 0,
                SpringLayout.VERTICAL_CENTER, namePretextLabel
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
                SpringLayout.VERTICAL_CENTER, urlValueLabel, 0,
                SpringLayout.VERTICAL_CENTER, urlPretextLabel
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
                SpringLayout.WEST, linkValueArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, linkValueArea, padding,
                SpringLayout.SOUTH, selectBacklinkButton
        );
        returnPanel.add(linkValueArea);

        panelLayout.putConstraint(
                SpringLayout.WEST, selectLinkButton, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, selectLinkButton, padding,
                SpringLayout.SOUTH, linkValueArea
        );
        returnPanel.add(selectLinkButton);

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


    //Removes borders from individual cells in table when table row is selected
    private static class NoBorderRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    private void sizeTable() {
        int tableWidth = searchTable.getWidth();
        for(int i = 0; i < searchTable.getColumnCount(); i++) {
            searchTable.getColumnModel().getColumn(i).setMaxWidth(tableWidth);
            searchTable.getColumnModel().getColumn(i).setMinWidth(tableWidth / 100);
        }
        searchTable.getColumnModel().getColumn(0).setPreferredWidth(tableWidth / 15);
        searchTable.getColumnModel().getColumn(1).setPreferredWidth(tableWidth / 10);
        searchTable.getColumnModel().getColumn(2).setPreferredWidth(tableWidth / 2);
        searchTable.getColumnModel().getColumn(3).setPreferredWidth(tableWidth / 10);
        searchTable.getColumnModel().getColumn(4).setPreferredWidth(tableWidth);
        searchTable.getColumnModel().getColumn(5).setPreferredWidth(tableWidth / 15);
    }
}
