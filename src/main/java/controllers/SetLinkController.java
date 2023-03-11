package controllers;

import content_objects.SentenceObject;
import database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class SetLinkController {
    private final Database database;
    private final DefaultTableModel tableModel;
    private JTable sentenceTable;
    private JLabel idLabel;
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel urlLabel;
    private JTextArea sentenceArea;
    private JButton linkButton;
    private JLabel linkLabel;

    public SetLinkController(Database database, DefaultTableModel tableModel) {
        this.database = database;
        this.tableModel = tableModel;

    }

    public void setComponents(JTable sentenceTable, JLabel idLabel, JLabel typeLabel, JLabel nameLabel,
                              JLabel urlLabel, JTextArea sentenceArea, JButton linkButton, JLabel linkLabel) {
        this.sentenceTable = sentenceTable;
        this.idLabel = idLabel;
        this.typeLabel = typeLabel;
        this.nameLabel = nameLabel;
        this.urlLabel = urlLabel;
        this.sentenceArea = sentenceArea;
        this.linkButton = linkButton;
        this.linkLabel = linkLabel;
    }

    //TODO: Remove sentence ID on release, no need for it to be there
    public void initializeTable() {
        String[] columns = {"Sentence ID", "Source Type", "Source Name", "Source URL", "Sentence", "Backlink"};
        SentenceObject[] loadedSentences;
        for(String column : columns) {
            tableModel.addColumn(column);
        }

        try {
            loadedSentences = database.fetchAllSentences();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for(SentenceObject sentence : loadedSentences) {
            addSentenceToTable(sentence);
        }
    }

    public void sentenceSearch(String searchQuery) {
        SentenceObject[] foundSentences;
        try {
            foundSentences = database.fetchMatchingSentences(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        for(SentenceObject sentenceObject : foundSentences) {
            //TODO: Clear current table
            System.out.println(sentenceObject.getSentence());
        }
    }

    public void addSentenceToTable(SentenceObject targetSentence) {
        tableModel.addRow(new Object[]{
                targetSentence.getSentenceKey(),
                targetSentence.getSourceType(),
                targetSentence.getSourceName(),
                targetSentence.getSourceUrl(),
                targetSentence.getSentence(),
                targetSentence.getBacklink()
                //TODO: replace backlink number with backlink sentence, later
        });
    }

    public void selectionChanged() {
        int selectedRow = sentenceTable.getSelectedRow();
        idLabel.setText(tableModel.getValueAt(selectedRow, 0).toString());
        typeLabel.setText(tableModel.getValueAt(selectedRow, 1).toString());
        nameLabel.setText(tableModel.getValueAt(selectedRow, 2).toString());
        try {
            urlLabel.setText(tableModel.getValueAt(selectedRow, 3).toString());
        } catch (NullPointerException e) {
            urlLabel.setText("None");
        }
        sentenceArea.setText(tableModel.getValueAt(selectedRow, 4).toString());
        String linkValue = tableModel.getValueAt(selectedRow, 5).toString();
        if(!linkValue.equals("0")) {
            linkLabel.setText(linkValue);
            linkButton.setEnabled(true);
        }
        else {
            linkLabel.setText("None");
            linkButton.setEnabled(false);
        }
    }

    public void selectBacklink() {
        int selectedRow = sentenceTable.getSelectedRow();
        String targetRow = tableModel.getValueAt(selectedRow, 5).toString();
        for(int i =0; i < sentenceTable.getRowCount(); i++) {
            if(sentenceTable.getValueAt(i, 0).toString().equals(targetRow)) {
                sentenceTable.setRowSelectionInterval(0, i);
                return;
            }
        }
        SentenceObject missingSentence;
        try {
            missingSentence  = database.fetchSentenceByKey(Integer.parseInt(targetRow));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        addSentenceToTable(missingSentence);
        sentenceTable.setRowSelectionInterval(0, sentenceTable.getRowCount() - 1);
    }
}

