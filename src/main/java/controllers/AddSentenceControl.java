package controllers;

import database.Database;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class AddSentenceControl {
    private final Database database;
    private final JComboBox<String> sourceType;
    private final JTextArea sourceName;
    private final JTextArea sourceURL;
    private final JTextArea sentence;
    private final JCheckBox nsfwTag;
    private final JTextArea imageLink;
    private final JLabel backlinkLabel;
    private int backlinkId;


    public AddSentenceControl(Database database, JComboBox<String> sourceType,  JTextArea sourceName, JTextArea sourceURL,
                              JTextArea sentence, JCheckBox nsfwTag, JTextArea imageLink, JLabel backLink) {
        this.database = database;
        this.sourceType = sourceType;
        this.sourceName = sourceName;
        this.sourceURL = sourceURL;
        this.sentence = sentence;
        this.nsfwTag = nsfwTag;
        this.imageLink = imageLink;
        this.backlinkLabel = backLink;
    }

    public void addSentence() throws SQLException {
        String type = "'" + Objects.requireNonNull(sourceType.getSelectedItem()).toString() + "'";
        String name = "'" + sourceName.getText() + "'";
        String url = sourceURL.getText().equals("") ? "NULL" : "'" + sourceURL.getText() + "'";
        String sent = "'" + sentence.getText() + "'";
        String image = imageLink.getText().equals("") ? "NULL" : "'" + imageLink.getText() + "'";
        String nsfw = nsfwTag.isSelected() ? "TRUE" : "FALSE";
        String link = backlinkLabel.getText().equals("No Link") || backlinkLabel.getText().equals("Head") ?
                "NULL" : String.valueOf(backlinkId);

        database.insertSentence(type, name, url, sent, image, nsfw, link);
        if(!backlinkLabel.getText().equals("No LInk")) {
            backlinkId = database.getMaxSentenceIndex();
        }
    }

    //TODO: How to reference and save images
}
