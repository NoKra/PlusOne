package controllers;

import content_objects.SentenceObject;
import database.Database;
import views.BrowseView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.SQLException;

public class BrowseController {

    private final BrowseView browseView;
    private final Database database;
    private SentenceObject[] loadedSentences;
    private final DefaultTableModel resultTableModel;
    private final JTable resultTable;


    public BrowseController(BrowseView parentView, Database inDatabase) {
        browseView = parentView;
        database = inDatabase;
        resultTableModel = browseView.getResultTableModel();
        resultTable = browseView.getResultTable();

        initializeResultTable();
    }

    private void initializeResultTable() {
        resultTable.getTableHeader().setReorderingAllowed(false);
        resultTable.setDefaultRenderer(Object.class, new NoBorderRenderer());
        String[] columns = {"ID", "Source Name", "Source Type", "Sentence"};

        for(String column : columns) {
            resultTableModel.addColumn(column);
        }

        try {
            loadedSentences = database.fetchAllSentences();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return;
        }
        for(SentenceObject sentence : loadedSentences) {
            addSentenceToTable(sentence);
        }

    }

    private void addSentenceToTable(SentenceObject targetSentence) {
        resultTableModel.addRow(new Object[] {
                targetSentence.getSentenceKey(),
                targetSentence.getSourceName(),
                targetSentence.getSourceType(),
                targetSentence.getSentence()
        });
    }

    private void tableSelectionChanged() {
        int rowSelected = resultTable.getSelectedRow();
        //TODO: Keep going from here

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

}
