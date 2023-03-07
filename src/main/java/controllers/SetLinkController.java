package controllers;

import database.Database;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class SetLinkController {
    private final Database database;
    private final DefaultTableModel tableModel;

    public SetLinkController(Database database, DefaultTableModel tableModel) {
        this.database = database;
        this.tableModel = tableModel;

    }

    public void populateTableModel() throws SQLException {

        for(int i = 1; i <= database.getSentenceIndex(); i++) {
            database.fetchSentenceByKey(i);
            //TODO: use word objects to populate rows in the the tableModel
        }
    }
}
