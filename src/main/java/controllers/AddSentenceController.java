package controllers;

import content_objects.SentenceObject;
import database.Database;
import org.json.simple.JSONObject;
import views.AddSentenceView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;


public class AddSentenceController {
    private final AddSentenceView addSentenceView;
    private final Database database;
    private final JComboBox<String> sourceType;
    private final JTextArea sourceName;
    private final JCheckBox hasUrlCheck;
    private final JTextArea sourceURL;
    private final JTextArea sentence;
    private final JCheckBox nsfwTag;
    private final JTextArea backLinkArea;
    private final Color hasLinkColor;
    private final int notLinked = -1; //Value used to show sentence has no link, neither forward nor backwards
    private final int linkHead = -2; //Value used to show that this sentence is a head link (start of sentence chain)
    private int backlinkId;
    //To avoid saving multiple of the same images used for multiple sentences, we keep track of if there's a new image
    private String imageId = null;

    //Instantiation
    public AddSentenceController(AddSentenceView addSentenceView, Database database) {
        this.addSentenceView = addSentenceView;
        this.database = database;
        this.sourceType = addSentenceView.getSourceTypeCombo();
        this.sourceName = addSentenceView.getSourceNameArea();
        this.hasUrlCheck = addSentenceView.getHasUrlCheck();
        this.sourceURL = addSentenceView.getSourceUrlArea();
        this.sentence = addSentenceView.getSentenceArea();
        this.nsfwTag = addSentenceView.getNsfwCheck();
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
    public void setBackLink(SentenceObject linkSentence) {
        for(int i = 0; i < sourceType.getItemCount(); i++) {
            sourceType.setSelectedIndex(i);
            if(Objects.requireNonNull(sourceType.getSelectedItem()).toString().equals(linkSentence.getSourceType())) {
                break;
            }
        }
        this.sourceName.setText(linkSentence.getSourceName());
        this.sourceURL.setText(linkSentence.getSourceUrl());
        if(linkSentence.getSourceUrl() != null && !this.hasUrlCheck.isSelected()) {
            this.hasUrlCheck.doClick();
        }
        this.backlinkId = linkSentence.getSentenceKey();
        this.backLinkArea.setText(linkSentence.getSentence());
        this.backLinkArea.setForeground(hasLinkColor);
        addSentenceView.repaintAllFields();
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
        imageId = (addSentenceView.getSentenceImage() == null)
                ? "NULL" : (addSentenceView.isNewImage()) ?
                String.valueOf(database.getMaxSentenceIndex() + 1) : imageId;
        SentenceObject sentenceObject = new SentenceObject(
                database.getMaxSentenceIndex() + 1,
                Objects.requireNonNull(sourceType.getSelectedItem()).toString(),
                sourceName.getText(),
                sourceURL.getText(),
                sentence.getText(),
                imageId,
                nsfwTag.isSelected(),
                backlinkId,
                Database.createTimestamp(),
                null,
                addSentenceView.getSentenceImage()
        );
        database.insertSentence(sentenceObject, addSentenceView.isNewImage());
        if(backlinkId != notLinked) {
            if(backlinkId != linkHead) {
                verifyHeadStatus(backlinkId);
            }
            backlinkId = database.getMaxSentenceIndex();
        }
    }

    //Checks entry fields to ensure they match requirements for database entry (size, required)
    public boolean checkDbRequirements() {
        boolean meetsRequirements = true;
        JSONObject sentenceTableJSON = (JSONObject) database.getTableJSON().get("SENTENCES");
        JSONObject nameJSON = (JSONObject) sentenceTableJSON.get("SOURCE_NAME");
        int maxNameLength = Integer.parseInt(nameJSON.get("SIZE").toString());
        JSONObject urlJSON = (JSONObject) sentenceTableJSON.get("SOURCE_URL");
        int maxUrlLength = Integer.parseInt(urlJSON.get("SIZE").toString());
        JSONObject sentenceJSON = (JSONObject) sentenceTableJSON.get("SENTENCE");
        int maxSentenceLength = Integer.parseInt(sentenceJSON.get("SIZE").toString());

        if (sourceName.getText().length() > maxNameLength) {
            addSentenceView.toggleMaxNameLabelOn(maxNameLength, sourceName.getText().length());
            meetsRequirements = false;
        } else { //Not calling else-if statements here because Off toggles early terminate with visible checks
            addSentenceView.toggleMaxNameLabelOff();
        }
        if (sourceURL.getText().length() > maxUrlLength) {
            meetsRequirements = false;
        }
        if (sentence.getText().length() > maxSentenceLength) {
            meetsRequirements = false;
        }
        if (sentence.getText().length() <= 0) {
            addSentenceView.toggleNoSentenceLabelOn();
            meetsRequirements = false;
        }

        return meetsRequirements;
    }

    //Checks if the backlink sentence was previously unlinked, sets as new head if unlinked
    public void verifyHeadStatus(int backlinkId) throws SQLException {
        SentenceObject linkedSentence = database.fetchSentenceByKey(backlinkId);
        if(linkedSentence.getBacklink() == notLinked) {
            linkedSentence.setBacklink(linkHead);
            database.updateSentence(linkedSentence);
        }
    }

}
