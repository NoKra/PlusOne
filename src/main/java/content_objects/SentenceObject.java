package content_objects;

public class SentenceObject {

    private final int sentenceKey;
    private final String sourceType;
    private final String sourceName;
    private final String sourceUrl;
    private final String sentence;
    private final String imagePath;
    private final Boolean nsfwTag;
    private SentenceObject backlink;


    public SentenceObject(int sentenceKey, String sourceType, String sourceName, String sourceUrl, String sentence,
                          String imagePath, boolean nsfwTag, SentenceObject backlink) {
        this.sentenceKey = sentenceKey;
        this.sourceType = sourceType;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.sentence = sentence;
        this.imagePath = imagePath;
        this.nsfwTag = nsfwTag;
        this.backlink = backlink;
    }

    public int getSentenceKey() {
        return sentenceKey;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSentence() {
        return sentence;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Boolean getNsfwTag() {
        return nsfwTag;
    }

    public SentenceObject getBacklink() {
        return backlink;
    }

    public void setBacklink(SentenceObject backlink) {
        this.backlink = backlink;
    }
}
