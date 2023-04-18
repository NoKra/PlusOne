package controllers;

import content_objects.SentenceObject;
import database.Database;
import views.AddSentenceView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class AddSentenceControl {
    private final Database database;
    private final JComboBox<String> sourceType;
    private final JTextArea sourceName;
    private final JCheckBox hasUrlCheck;
    private final JTextArea sourceURL;
    private final JTextArea sentence;
    private final JCheckBox nsfwTag;
    private final JTextArea imageLink;
    private final JTextArea backLinkArea;
    private final Color hasLinkColor;
    private final int notLinked = -1;
    private final int linkHead = -2;
    private int backlinkId;

    //Instantiation
    public AddSentenceControl(AddSentenceView addSentenceView, Database database) {
        this.database = database;
        this.sourceType = addSentenceView.getSourceTypeCombo();
        this.sourceName = addSentenceView.getSourceNameArea();
        this.hasUrlCheck = addSentenceView.getHasUrlCheck();
        this.sourceURL = addSentenceView.getSourceUrlArea();
        this.sentence = addSentenceView.getSentenceArea();
        this.nsfwTag = addSentenceView.getNsfwCheck();
        this.imageLink = addSentenceView.getImageArea();
        this.backLinkArea = addSentenceView.getCurrentLinkArea();
        this.hasLinkColor = addSentenceView.getHasLinkColor();
        backlinkId = notLinked;
    }

    public int getBacklinkId() {
        return backlinkId;
    }

    public boolean hasBacklink() {
        return backlinkId != notLinked;
    }


    //Sets a backlink, use the sentence id of the linking sentence
    public void setBackLink(String sourceTypeText, String sourceNameText, String urlText, String backLinkText, int backlinkId) {
        for(int i = 0; i < sourceType.getItemCount(); i++) {
            sourceType.setSelectedIndex(i);
            if(Objects.requireNonNull(sourceType.getSelectedItem()).toString().equals(sourceTypeText)) {
                break;
            }
        }
        this.sourceName.setText(sourceNameText);
        this.sourceURL.setText(urlText);
        if(!urlText.equals("None") && !this.hasUrlCheck.isSelected()) {
            this.hasUrlCheck.doClick();
        }
        this.backlinkId = backlinkId;
        this.backLinkArea.setText(backLinkText);
        this.backLinkArea.setForeground(hasLinkColor);
    }

    //Sets the sentence as the head of a linked sentences
    public void setBackLinkHead() {
        backlinkId = linkHead;
    }

    //Sets the sentence to have no links
    public void removeBacklinkId() {
        backlinkId = notLinked;
    }

    //Adds current sentence in AddSentenceView to the database, sets backlink if applicable
    public void addSentence() throws SQLException {
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
        if(backlinkId != notLinked) {
            if(backlinkId != linkHead) {
                verifyHeadStatus(backlinkId);
            }
            backlinkId = database.getMaxSentenceIndex();
        }
    }

    //Checks if the backlink sentence was previously unlinked, sets as new head if unlinked
    public void verifyHeadStatus(int backlinkId) throws SQLException {
        SentenceObject linkedSentence = database.fetchSentenceByKey(backlinkId);
        if(linkedSentence.getBacklink() == notLinked) {
            linkedSentence.setBacklink(linkHead);
            database.updateSentence(linkedSentence);
        }
    }



    //TODO: How to reference and save images
}
