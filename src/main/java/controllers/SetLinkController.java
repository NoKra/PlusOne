package controllers;

import content_objects.SentenceObject;
import database.Database;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetLinkController {
    private final Database database;
    private final DefaultTableModel tableModel;

    public SetLinkController(Database database, DefaultTableModel tableModel) {
        this.database = database;
        this.tableModel = tableModel;

    }

    public void setupTable() {
        //TODO: move all the table/tablemodel initialization here
    }

    public void loadAllSentences() throws SQLException {
        SentenceObject[] loadedSentences = database.fetchAllSentences();
    }
}
