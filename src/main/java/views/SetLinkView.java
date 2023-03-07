package views;

import controllers.SetLinkController;
import window_object.WindowObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class SetLinkView {
    private final WindowObject setWindow;
    private final Container container;
    private final SpringLayout layout;
    private final int padding = 20;
    DefaultTableModel linkTableModel = new DefaultTableModel();
    private final SetLinkController linkController;

    public SetLinkView(WindowObject mainWindow) {

        setWindow = new WindowObject(mainWindow.getDatabase(), WindowObject.WindowSize.SetLinkView);
        container = setWindow.getContentContainer();
        layout = setWindow.getLayout();

        setWindow.setWindowVisible();

        linkController = new SetLinkController(mainWindow.getDatabase(), linkTableModel);
        try {
            linkController.populateTableModel();
        } catch (SQLException e) {
            System.out.println(e);
        }

        createView();
    }

    private void createView() {
        JLabel header = new JLabel("Set Link:");
        layout.putConstraint(
                SpringLayout.WEST, header, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, header, padding,
                SpringLayout.NORTH, container
        );
        container.add(header);

        String[] columns = {"Sentence ID", "Source Type", "Source Name", "Source URL", "Sentence", "Backlink"};
        JTable sentenceTable = new JTable(linkTableModel);
        sentenceTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScroll = new JScrollPane(sentenceTable);
        sentenceTable.setCellSelectionEnabled(false);
        for(String column : columns) {
            linkTableModel.addColumn(column);
        }
        layout.putConstraint(
                SpringLayout.WEST, tableScroll, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, tableScroll, padding,
                SpringLayout.SOUTH, header
        );
        container.add(tableScroll);

    }
}
