package views;

import window_object.WindowObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SetLinkView {
    private final WindowObject setWindow;
    private final Container container;
    private final SpringLayout layout;
    private final int padding = 20;

    public SetLinkView(WindowObject mainWindow) {

        setWindow = new WindowObject(mainWindow.getDatabase());
        container = setWindow.getContentContainer();
        layout = setWindow.getLayout();

        setWindow.changeWindowSize();
        setWindow.setWindowVisible();
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

        DefaultTableModel sentTableModel = new DefaultTableModel();
        JTable sentenceTable = new JTable(sentTableModel);
        sentTableModel.addColumn("SENTENCE_KEY");
        sentTableModel.addColumn("KANJI");
        sentTableModel.insertRow(0, new Object[] {"a", "1"});
        layout.putConstraint(
                SpringLayout.WEST, sentenceTable, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sentenceTable, padding,
                SpringLayout.SOUTH, header
        );
        container.add(sentenceTable);


    }
}
