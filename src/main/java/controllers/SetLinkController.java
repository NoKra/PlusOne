package controllers;

import content_objects.SentenceObject;
import database.Database;
import views.SetLinkView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

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
    private int maxSentences = 0;

    public SetLinkController(Database database, SetLinkView linkView) {
        this.database = database;
        this.tableModel = linkView.getLinkTableModel();

    }

    //Fetches required components from SetLinkView,
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
    //Establishes the tables columns and populates the table with entire sentence database table
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
        maxSentences = loadedSentences.length;
        for(SentenceObject sentence : loadedSentences) {
            addSentenceToTable(sentence);
        }
    }

    //Searches for a sentence within the entire database for a partial match (using regex) of the search query
    public void sentenceSearch(String searchQuery) {
        if(searchQuery.equals("")) {
            if(maxSentences <= tableModel.getRowCount()) {
                return;
            }
            SentenceObject[] loadedSentences;
            try {
                loadedSentences = database.fetchAllSentences();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            tableModel.setRowCount(0);
            for(SentenceObject sentence : loadedSentences) {
                addSentenceToTable(sentence);
            }
            sentenceTable.setRowSelectionInterval(0, 0);
            return;
        }
        List<SentenceObject> foundSentences;
        try {
            foundSentences = database.fetchMatchingSentences(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        tableModel.setRowCount(0);
        for(SentenceObject sentence : foundSentences) {
            addSentenceToTable(sentence);
        }
        if(tableModel.getRowCount() > 0) {
            sentenceTable.setRowSelectionInterval(0, 0);
        }
    }

    //Adds sentence to table from a SentenceObject
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

    //Updates the labels below the table with the newly highlighted selection information
    public void selectionChanged() {
        int selectedRow = sentenceTable.getSelectedRow();
        if(selectedRow < 0) {
            clearLabels();
            return;
        }
        idLabel.setText(tableModel.getValueAt(selectedRow, 0).toString());
        typeLabel.setText(tableModel.getValueAt(selectedRow, 1).toString());
        try {
            nameLabel.setText(tableModel.getValueAt(selectedRow, 2).toString());
        } catch (NullPointerException e) {
            nameLabel.setText("None");
        }
        try {
            urlLabel.setText(tableModel.getValueAt(selectedRow, 3).toString());
        } catch (NullPointerException e) {
            urlLabel.setText("None");
        }
        sentenceArea.setText(tableModel.getValueAt(selectedRow, 4).toString());
        String linkValue = tableModel.getValueAt(selectedRow, 5).toString();
        if(!linkValue.equals("-1")) {
            linkLabel.setText(linkValue);
            linkButton.setEnabled(true);
        }
        else {
            linkLabel.setText("None");
            linkButton.setEnabled(false);
        }
    }

    //Moves the selection on the table to the backlink of the currently selected sentence
    public void selectSelectedBacklink() {
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

    public void clearLabels() {
        //TODO: Consider a better default empty value
        String defaultEmpty = "";
        idLabel.setText(defaultEmpty);
        typeLabel.setText(defaultEmpty);
        nameLabel.setText(defaultEmpty);
        urlLabel.setText(defaultEmpty);
        sentenceArea.setText(defaultEmpty);
        linkLabel.setText(defaultEmpty);
    }

    public void selectLink() {

    }
}

