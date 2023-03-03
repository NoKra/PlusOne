package database;
import java.sql.*;
import java.util.*;

public class Database {
    //TODO: try to set database path to onedrive folder
    //TODO: If not just save it to C:/PlusOneDb
    private final String jdbcURL = "jdbc:h2:file:./database/podb";
    private final String username = "po";
    private final String password = "";
    private final Connection dbConnection;
    private final List<String> tables = List.of("SENTENCES", "WORDS", "OCCURRENCES");

    public Database() throws  SQLException {
        dbConnection = DriverManager.getConnection(jdbcURL, username, password);
        checkForMissingTables();
    }

    public Connection getDbConnection() {
        return dbConnection;
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
    public void testInsert() throws SQLException {
        String sql = "INSERT INTO NOTSTUDENTS VALUES (9, 'stop being dumb');";
        dbConnection.createStatement().execute(sql);
        System.out.println("something inserted");
    }

    public void initAllTables() throws SQLException {
        initSentencesTable();
        initWordsTable();
        initOccurrencesTable();
    }

    public void initSentencesTable() throws SQLException {
        String sentence_table = "CREATE TABLE SENTENCES(" +
                "SENTENCE_KEY INT, " +
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
                "APPLICABLE_DEF VARCHAR(511)" +
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

    public void insertSentence() throws SQLException {
        //TODO: Write sql insert sentence statement
        String sql = "";
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
