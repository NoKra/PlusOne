package database;
import java.sql.*;
import java.util.*;

public class Database {
    private final String jdbcURL = "jdbc:h2:file:C:/Users/NoKra/OneDrive/database/podb";
    //private final String jdbcURL = "jdbc:h2:file:C:/Users/Kraus/OneDrive/database/podb";
    private final String username = "po";
    private final String password = "";
    private final Connection dbConnection;
    private final List<String> tables = List.of("SENTENCES", "WORDS", "OCCURRENCES");
    private int sentenceIndex;

    public Database() throws  SQLException {
        dbConnection = DriverManager.getConnection(jdbcURL, username, password);
        checkForMissingTables();
        sentenceIndex = findSentenceIndex();

        System.out.println("Current Sentence Index: " + sentenceIndex);
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    private int findSentenceIndex() throws SQLException {
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

    public void testConnection() throws SQLException {

        System.out.println("Connected to H2 embedded database");

        String sql = "SELECT * FROM STUDENTS";

        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            int ID = resultSet.getInt("ID");
            String name = resultSet.getString("name");
            System.out.println("Student: " + ID + "Name: " + name);
        }
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

    public void insertSentence(String sourceType, String sourceName, String sourceUrl, String sentence,
                               String imagePath, String nsfw, String backLink) throws SQLException {
        sentenceIndex += 1;
        String sql = String.format("INSERT INTO SENTENCES VALUES ( " +
                        "%d, %s, %s, %s, %s, %s, %s, %s);",
                sentenceIndex ,sourceType, sourceName, sourceUrl, sentence, imagePath, nsfw, backLink);
        System.out.println(sql);
        dbConnection.createStatement().execute(sql);
    }

    public void fetchSentenceByKey(int sentenceKey) throws SQLException {
        String sql = String.format("SELECT * FROM SENTENCES WHERE SENTENCE_KEY = %s", sentenceKey);
        ResultSet rs = dbConnection.createStatement().executeQuery(sql);
        while(rs.next()) {
            int key = rs.getInt("SENTENCE_KEY");
            String sourceName = rs.getString("SOURCE_NAME");
            String sentence = rs.getString("SENTENCE");
            System.out.println(String.format("Key: %d | Source: %s | Sentence: %s", key, sourceName, sentence));
            //TODO: transfer the fetched sentences to a word object and return that object
        }
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
