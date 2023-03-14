package controllers;

import content_objects.SentenceObject;
import database.Database;
import views.AddSentenceView;

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



    public AddSentenceControl(AddSentenceView addSentenceView, Database database) {
        this.database = database;
        this.sourceType = addSentenceView.getSourceTypeCombo();
        this.sourceName = addSentenceView.getSourceNameArea();
        this.sourceURL = addSentenceView.getSourceUrlArea();
        this.sentence = addSentenceView.getSentenceArea();
        this.nsfwTag = addSentenceView.getNsfwCheck();
        this.imageLink = addSentenceView.getImageArea();
        this.backlinkLabel = addSentenceView.getCurrentLinkStatusLabel();
        backlinkId = -1;
    }

    public void addSentence() throws SQLException {
        backlinkId = !backlinkLabel.getText().equals("No Link") && !backlinkLabel.getText().equals("Head") ?
                database.getMaxSentenceIndex() : -1;
        SentenceObject insertObject = new SentenceObject(
                database.getMaxSentenceIndex() + 1,
                Objects.requireNonNull(sourceType.getSelectedItem()).toString(),
                sourceName.getText(),
                sourceURL.getText(),
                sentence.getText(),
                imageLink.getText(),
                nsfwTag.isSelected(),
                backlinkId
        );
        database.insertSentence(insertObject);
    }

    public void setBacklinkId(int backlinkId, String backLinkText) {
        this.backlinkId = backlinkId;
        this.backlinkLabel.setText(backLinkText);
    }

    //TODO: How to reference and save images
}
