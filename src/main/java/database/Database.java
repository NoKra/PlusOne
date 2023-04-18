package database;
import content_objects.SentenceObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

public class Database {
    //TODO: Create a way to select db save location
    //private final String jdbcURL = "jdbc:h2:file:C:/Users/NoKra/OneDrive/database/podb";
    private final String jdbcURL = "jdbc:h2:file:C:/Users/Kraus/OneDrive/database/podb";
    private final String username = "po";
    private final String password = "";
    private Connection dbConnection;
    private final List<String> tables = List.of("SENTENCES", "WORDS", "OCCURRENCES");
    private int maxSentenceIndex;
    private int maxWordIndex;
    private int maxOccurrenceIndex;

    public Database() throws  SQLException {
        dbConnection = DriverManager.getConnection(jdbcURL, username, password);
        checkForMissingTables();
        maxSentenceIndex = findMaxSentenceIndex();

        System.out.println("Current Sentence Index: " + maxSentenceIndex);
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void backupDatabase()  {
        //Need to close connection to unlock .mv.db file
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FileInputStream inputStream;
        FileOutputStream outputStream;
        //TODO: Release version - Rework this hardcoding of substrings to work within source directory
        //TODO: Also, implement some sort of versioning to have several backups, consider doing interval backups by date?
        String sourceSub = jdbcURL.substring(13);
        String[] sourceFiles = {sourceSub + ".mv.db", sourceSub + ".trace.db"};
        String destinationSub = sourceSub.substring(0, sourceSub.length() - 5);
        String[] destinationFiles = {destinationSub + "/backup/podb.mv.db", destinationSub + "/backup/podb.trace.db"};
        for(int i = 0; i < sourceFiles.length; i++) {
            try {
                inputStream = new FileInputStream(sourceFiles[i]);
                outputStream = new FileOutputStream(destinationFiles[i]);
                int condition;
                while((condition = inputStream.read()) != -1) {
                    outputStream.write(condition);
                }
                System.out.printf("Finished: %s%n", sourceFiles[i]);
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            dbConnection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO: Is there another reason or way to converge the save and load backups?
    public void loadBackupDatabase() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FileInputStream inputStream;
        FileOutputStream outputStream;
        String destinationSub = jdbcURL.substring(13);
        String[] destinationFiles = {destinationSub + ".mv.db", destinationSub + ".trace.db"};
        String sourceSub = destinationSub.substring(0, destinationSub.length() - 5);
        String[] sourceFiles = {sourceSub + "/backup/podb.mv.db", sourceSub + "/backup/podb.trace.db"};
        for(int i = 0; i < destinationFiles.length; i++) {
            try {
                inputStream = new FileInputStream(sourceFiles[i]);
                outputStream = new FileOutputStream(destinationFiles[i]);
                int condition;
                while((condition = inputStream.read()) != -1) {
                    outputStream.write(condition);
                }
                System.out.printf("Finished: %s%n", destinationFiles[i]);
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            dbConnection = DriverManager.getConnection(jdbcURL, username, password);
            maxSentenceIndex = findMaxSentenceIndex();
            //TODO: implement finds max for word and occurrences later
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxSentenceIndex() {
        return maxSentenceIndex;
    }

    public int findMaxSentenceIndex() throws SQLException {
        String emptySql = "SELECT MAX(SENTENCE_KEY) AS MaxKey FROM SENTENCES;";
        ResultSet emptyResult = dbConnection.createStatement().executeQuery(emptySql);
        if(emptyResult.next()) {
            return emptyResult.getInt("MaxKey");
        }
        else {
            System.out.println("SQL Select Max Failed");
            return -1;
        }
    }

    public int findMaxWordIndex() {
        return 0;
    }

    public int findMaxOccurrenceIndex() {
        return 0;
    }

    public void initAllTables() throws SQLException {
        initSentencesTable();
        initWordsTable();
        initOccurrencesTable();
    }

    public void initSentencesTable() throws SQLException {
        String sentence_table = "CREATE TABLE SENTENCES(" +
                "SENTENCE_KEY INT AUTO_INCREMENT, " +
                "SOURCE_TYPE VARCHAR(31), " +
                "SOURCE_NAME VARCHAR(255), " +
                "SOURCE_URL VARCHAR(511), " +
                "SENTENCE VARCHAR(1023) NOT NULL, " +
                "IMAGE_PATH VARCHAR(511), " +
                "NSFW BOOL, " +
                "PRIMARY KEY (SENTENCE_KEY), " +
                "BACK_LINK INT" + //If sentence is from a longer text, links backwards to provide reference
                ");";

        dbConnection.createStatement().execute(sentence_table);
        System.out.println("Sentence table created");
    }

    public void initWordsTable() throws SQLException {
        String word_table = "CREATE TABLE WORDS(" +
                "WORD_KEY INT, " +
                "KANJI VARCHAR(63), " +
                "KANA VARCHAR(127), " +
                "PART_OF_SPEECH VARCHAR(31), " +
                "ENGLISH_DEF VARCHAR(511), " +
                "JAPANESE_DEF VARCHAR(511), " +
                "TAGS VARCHAR(63), " +
                "LOWEST_PLUS_VAL INT, " +
                "FREQUENCY INT, " +
                "PRIMARY KEY (WORD_KEY)" +
                ");";
        dbConnection.createStatement().execute(word_table);
        System.out.println("Words table created");
    }

    public void initOccurrencesTable() throws SQLException {
        String occurrence_table = "CREATE TABLE OCCURRENCES(" +
                "OCCURRENCE_KEY INT, " +
                "SENTENCE_KEY INT, " +
                "WORD_KEY INT, " +
                "APPLICABLE_DEF VARCHAR(511), " +
                "PLUS_VALUE INT" +
                ");";
        dbConnection.createStatement().execute(occurrence_table);
        System.out.println("Occurrences table created");
    }

    //TODO: ADD FOREIGN KEY CONSTRAINTS TO OCCURRENCES TABLE

    //Checks to make sure the database has all required tables created
    public void checkForMissingTables() throws SQLException {
        ArrayList<String> requiredTables = new ArrayList<>(tables);
        int requiredCount = requiredTables.size();
        ArrayList<String> existsTables = new ArrayList<>();

        ResultSet rs = dbConnection.createStatement().executeQuery("SHOW TABLES;");
        while(rs.next()) {
            existsTables.add(rs.getString(1));
        }
        requiredTables.removeAll(existsTables);

        if (requiredTables.isEmpty()) {
            System.out.println("Tables loaded");
            return;
        }

        if(requiredTables.size() == requiredCount) {
            initAllTables();
            return;
        }

        for(String table : requiredTables) {
            System.out.println(table);
            switch (table) {
                case "SENTENCES" -> initSentencesTable();
                case "WORDS" -> initWordsTable();
                case "OCCURRENCES" -> initOccurrencesTable();
                default -> System.out.println("Table init method missing");
            }
        }
    }

    public void purgeTable(String tableName) throws SQLException {

        String sql = String.format("DELETE FROM %s", tableName);
        dbConnection.createStatement().execute(sql);

        switch (tableName) {
            case "SENTENCES" -> maxSentenceIndex = 0;
            case "WORDS" -> maxWordIndex = 0;
            case "OCCURRENCES" -> maxOccurrenceIndex = 0;
        }
    }

    public void deleteAllTables() throws SQLException {
        ResultSet rs = dbConnection.createStatement().executeQuery("SHOW TABLES;");
        while(rs.next()) {
            deleteTable(rs.getString(1));
        }
    }

    public void deleteTable(String tableName) throws SQLException {
        //TODO: Write Sql delete statement
        String sql = "";
        dbConnection.createStatement().execute(sql);
    }

    public void insertSentence(SentenceObject sentence) throws SQLException {
        int sqlKey = sentence.getSentenceKey();
        String sqlType = "'" + sentence.getSourceType() + "'";
        String sqlName = sentence.getSourceName().equals("") ? "NULL" : "'" + sentence.getSourceName() + "'";
        String sqlUrl = sentence.getSourceUrl().equals("") ? "NULL" : "'" + sentence.getSourceUrl() + "'";
        String sqlSentence = "'" + sentence.getSentence() + "'";
        String sqlImagePath = sentence.getImagePath().equals("") ? "NULL" : "'" + sentence.getImagePath() + "'";
        String sqlNsfw = sentence.getNsfwTag() ? "TRUE" : "FALSE";
        int sqlBacklink = sentence.getBacklink();

        String sql = String.format("INSERT INTO SENTENCES VALUES ( " +
                        "%d, %s, %s, %s, %s, %s, %s, %s);",
                sqlKey, sqlType, sqlName, sqlUrl, sqlSentence, sqlImagePath, sqlNsfw, sqlBacklink);
        maxSentenceIndex ++;
        System.out.println(sql);
        dbConnection.createStatement().execute(sql);
    }

    public void updateSentence(SentenceObject sentence) throws SQLException {
        int sqlKey = sentence.getSentenceKey();
        String sqlType = "'" + sentence.getSourceType() + "'";
        String sqlName;
        if(sentence.getSourceName() != null) {
            sqlName = "'" + sentence.getSourceName() + "'";
        } else  {
            sqlName = "NULL";
        }
        String sqlUrl = "'" + sentence.getSourceUrl() + "'";
        String sqlSentence = "'" + sentence.getSentence() + "'";
        String sqlImagePath;
        if(sentence.getImagePath() != null) {
            sqlImagePath = "'" + sentence.getImagePath() + "'";
        } else {
            sqlImagePath = "NULL";
        }
        String sqlNsfw = sentence.getNsfwTag() ? "TRUE" : "FALSE";
        int sqlBacklink = sentence.getBacklink();

        String sql = String.format("UPDATE SENTENCES SET " +
                        "SOURCE_TYPE = %s, " +
                        "SOURCE_NAME = %s, " +
                        "SOURCE_URL = %s, " +
                        "SENTENCE = %s, " +
                        "IMAGE_PATH = %s, " +
                        "NSFW = %s, " +
                        "BACK_LINK = %s "+
                        "WHERE SENTENCE_KEY = %s;",
                sqlType, sqlName, sqlUrl, sqlSentence, sqlImagePath, sqlNsfw, sqlBacklink, sqlKey);
        System.out.println(sql);
        dbConnection.createStatement().execute(sql);
    }

    public SentenceObject[] fetchAllSentences() throws SQLException {
        SentenceObject[] loadedSentences = new SentenceObject[maxSentenceIndex];
        String sql = "SELECT * FROM SENTENCES";
        ResultSet rs = dbConnection.createStatement().executeQuery(sql);
        int index = 0;
        while(rs.next()) {
            loadedSentences[index] = resultToSentenceObject(rs);
            index++;
        }
        return loadedSentences;
    }

    public SentenceObject fetchSentenceByKey(int sentenceKey) throws SQLException {
        String sql = String.format("SELECT * FROM SENTENCES WHERE SENTENCE_KEY = %s;", sentenceKey);
        ResultSet rs = dbConnection.createStatement().executeQuery(sql);
        rs.next();
        return resultToSentenceObject(rs);
    }

    public List<SentenceObject> fetchBySentence(String searchValue) throws SQLException{
        List<SentenceObject> foundSentences= new ArrayList<SentenceObject>();
        String sql = String.format("SELECT * FROM SENTENCES WHERE REGEXP_LIKE(SENTENCE, '%s[a-z]*', 'i');", searchValue);
        ResultSet rs = dbConnection.createStatement().executeQuery(sql);
        while(rs.next()) {
            foundSentences.add(resultToSentenceObject(rs));
        }
        return foundSentences;
    }

    public List<SentenceObject> fetchBySourceName(String searchValue) throws SQLException {
        List<SentenceObject> foundSentences= new ArrayList<SentenceObject>();
        String sql = String.format("SELECT * FROM SENTENCES WHERE REGEXP_LIKE(SOURCE_NAME, '%s[a-z]*', 'i');", searchValue);
        ResultSet rs = dbConnection.createStatement().executeQuery(sql);
        while(rs.next()) {
            foundSentences.add(resultToSentenceObject(rs));
        }
        return foundSentences;
    }

    public SentenceObject resultToSentenceObject(ResultSet set) throws SQLException {
        int sentenceKey = set.getInt("SENTENCE_KEY");
        String sourceType = set.getString("SOURCE_TYPE");
        String sourceName = set.getString("SOURCE_NAME");
        String sourceURL = set.getString("SOURCE_URL");
        String sentence = set.getString("SENTENCE");
        String imgPath = set.getString("IMAGE_PATH");
        boolean nsfw = set.getBoolean("NSFW");
        int backlink = set.getInt("BACK_LINK");

        return new SentenceObject(sentenceKey, sourceType, sourceName, sourceURL, sentence, imgPath, nsfw, backlink);
    }



    public void insertWord() throws SQLException {
        //TODO: Write sql insert word statement
        String sql = "";

    }

    public void insertOccurrence() throws SQLException {
        //TODO: Write sql insert occurrence statement
        String sql = "";

    }
}
