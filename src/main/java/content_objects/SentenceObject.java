package content_objects;

import java.awt.image.BufferedImage;
import java.time.Instant;

public class SentenceObject {

    private  final int sentenceKey;
    private  String sourceType;
    private  String sourceName;
    private  String sourceUrl;
    private  String sentence;
    private  String imagePath;
    private  Boolean nsfwTag;
    private int backlink;
    private String createdAt;
    private String updatedAt;
    private BufferedImage sentenceImage;


    public SentenceObject(int sentenceKey, String sourceType, String sourceName, String sourceUrl, String sentence,
                          String imagePath, boolean nsfwTag, int backlink, String createdAt, String updatedAt,
                          BufferedImage sentenceImage) {
        this.sentenceKey = sentenceKey;
        this.sourceType = sourceType;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.sentence = sentence;
        this.imagePath = imagePath;
        this.nsfwTag = nsfwTag;
        this.backlink = backlink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sentenceImage = sentenceImage;
    }

    //Shouldn't be able to change the sentenceKey, as it's unique and created by the database, so no setSentenceKey
    public int getSentenceKey() {
        return sentenceKey;
    }

    public String getSourceType() {
        return sourceType;
    }
    public void setSourceType(String sourceType) {this.sourceType = sourceType;}

    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {this.sourceName = sourceName;}

    public String getSourceUrl() {
        return sourceUrl;
    }
    public void setSourceUrl(String sourceUrl) {this.sourceUrl = sourceUrl;}

    public String getSentence() {
        return sentence;
    }
    public void setSentence(String sentence) {this.sentence = sentence;}

    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}

    public Boolean getNsfwTag() {
        return nsfwTag;
    }
    public void setNsfwTag(Boolean nsfwTag) {this.nsfwTag = nsfwTag;}

    public int getBacklink() {return backlink;}
    public void setBacklink(int backlink) {this.backlink = backlink;}

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public BufferedImage getSentenceImage() {return sentenceImage;}
    public void setSentenceImage(BufferedImage sentenceImage) {this.sentenceImage = sentenceImage;}

    public void consolePrint() {
        System.out.println(String.format(
                "Sentence Key: %d | " +
                "Source Type: %s | " +
                "Source Name: %s | " +
                "Source URL: %s |" +
                "Sentence: %s |" +
                "Image Path: %s |" +
                "NSFW Status: %s |" +
                "Backlink: %s",
                sentenceKey, sourceType, sourceName, sourceUrl, sentence, imagePath, nsfwTag, backlink));
    }

}
