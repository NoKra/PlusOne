package controllers;

import database.Database;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class AddSentenceControl {
    private final Database database;
    private final JComboBox<String> sourceType;
    private final JLabel backLink;
    private final JTextArea sourceName;
    private final JTextArea sourceURL;
    private final JTextArea sentence;
    private final JCheckBox nsfwTag;
    private final JTextArea imageLink;


    public AddSentenceControl(Database database, JComboBox<String> sourceType, JLabel backLink, JTextArea sourceName,
                              JTextArea sourceURL, JTextArea sentence, JCheckBox nsfwTag, JTextArea imageLink) {
        this.database = database;
        this.sourceType = sourceType;
        this.backLink = backLink;
        this.sourceName = sourceName;
        this.sourceURL = sourceURL;
        this.sentence = sentence;
        this.nsfwTag = nsfwTag;
        this.imageLink = imageLink;
    }

    public void addSentence() throws SQLException {
        String type = "\"" + Objects.requireNonNull(sourceType.getSelectedItem()).toString() + "\"";
        String name = "\"" + sourceName.getText() + "\"";
        String url = sourceURL.getText().equals("") ? "NULL" : "\"" + sourceURL.getText() + "\"";
        String sent = "\"" + sentence.getText() + "\"";
        String image = imageLink.getText().equals("") ? "NULL" : "\"" + imageLink.getText() + "\"";
        String nsfw = nsfwTag.isSelected() ? "TRUE" : "FALSE";
        String link = backLink.getText().equals("No Link") ? "NULL" : "\"" + backLink.getText() + "\"";
        
        database.insertSentence(type, name, url, sent, image, nsfw, link);
    }

    //TODO: How to reference and save images

    //TODO: How to process linking between sentences
}
