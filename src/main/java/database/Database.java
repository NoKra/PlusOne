package database;
import content_objects.SentenceObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import settings.Settings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.List;


public class Database {
    private final Settings settings;
    private Connection dbConnection;
    private final List<String> tables = List.of("SENTENCES", "WORDS", "OCCURRENCES");
    private int maxSentenceIndex;
    private int maxWordIndex;
    private int maxOccurrenceIndex;
    private final String resourceAddress = "json/database.json";
    private JSONObject tableJSON;


    public Database(Settings settings) throws  SQLException {

        this.settings = settings;
        try {
            tableJSON = (JSONObject) new JSONParser().parse( new InputStreamReader(getDatabaseJsonStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            verifySubDirectories();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        dbConnection = DriverManager.getConnection(
                this.settings.getDatabaseURL(),
                this.settings.getDatabaseUsername(),
                this.settings.getDatabasePassword());
        checkForMissingTables();
        maxSentenceIndex = findMaxSentenceIndex();

        System.out.println("Current Sentence Index: " + maxSentenceIndex);

        //Ensures lock for H2 database is removed upon exiting
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    dbConnection.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public JSONObject getTableJSON() {
        return tableJSON;
    }
    public int getMaxSentenceIndex() { return maxSentenceIndex; }

    //Loads database.json from resources as an InputStream
    private InputStream getDatabaseJsonStream() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceAddress);

        if(inputStream == null) {
            throw new IllegalArgumentException("file not found" + resourceAddress);
        } else {
            return inputStream;
        }
    }

    //Verifies the database/backup/images directories are present, creates if not
    private void verifySubDirectories() throws IOException {
        Path backupDirectory = Paths.get(settings.getDatabaseBackupPath());
        if(!Files.exists(backupDirectory)) {
            Files.createDirectories(backupDirectory);
            System.out.println("Backup directory created");
        }

        Path generalImageDirectory = Paths.get(settings.getGeneralImagePath());
        if(!Files.exists(generalImageDirectory)) {
            Files.createDirectories(generalImageDirectory);
            System.out.println("General image directory created");
        }

        Path nsfwImageDirectory = Paths.get(settings.getNsfwImagePath());
        if(!Files.exists(nsfwImageDirectory)) {
            Files.createDirectories(nsfwImageDirectory);
            System.out.println("NSFW image directory created");
        }

    }

    //Creates an extra copy of the current live database in the
    public void backupDatabase()  {
        //Need to close connection to unlock .mv.db file
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FileInputStream inputStream;
        FileOutputStream outputStream;
        //TODO: Implement some sort of versioning to have several backups, consider doing interval backups by date?
        String databaseFileConcat = settings.getDatabasePath() + settings.getDatabaseName();
        String[] sourceFiles = {
                databaseFileConcat + ".mv.db",
                databaseFileConcat + ".trace.db"};
        String databaseBackupFileConcat = settings.getDatabaseBackupPath() + settings.getDatabaseName();
        String[] destinationFiles = {
                databaseBackupFileConcat + ".mv.db",
                databaseBackupFileConcat + ".trace.db"};
        for(int i = 0; i < sourceFiles.length; i++) {
            Path filePath = Paths.get(sourceFiles[i]);
            if(!Files.exists(filePath)) {
                continue;
            }
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
            dbConnection = DriverManager.getConnection(
                    settings.getDatabaseURL(),
                    settings.getDatabaseUsername(),
                    settings.getDatabasePassword());
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
        String databaseFileConcat = settings.getDatabasePath() + settings.getDatabaseName();
        String[] destinationFiles = {
                databaseFileConcat + ".mv.db",
                databaseFileConcat + ".trace.db"};
        String databaseBackupFileConcat = settings.getDatabaseBackupPath() + settings.getDatabaseName();
        String[] sourceFiles = {
                databaseBackupFileConcat + ".mv.db",
                databaseBackupFileConcat + ".trace.db"};
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
            dbConnection = DriverManager.getConnection(
                    settings.getDatabaseURL(),
                    settings.getDatabaseUsername(),
                    settings.getDatabasePassword());
            maxSentenceIndex = findMaxSentenceIndex();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Creates table from database.json using the target table name
    public void createTable(String tableName) throws SQLException {
        JSONObject tableObject = (JSONObject) tableJSON.get(tableName);
        Iterator<?> columns = tableObject.keySet().iterator();
        StringBuilder sqlStatementBuilder = new StringBuilder("CREATE TABLE " + tableName + "(");
        while (columns.hasNext()) {
            String columnName = String.valueOf(columns.next());
            JSONObject column = (JSONObject) tableObject.get(columnName);
            StringBuilder columnStatement = new StringBuilder(String.valueOf(column.get("TYPE")));
            if (columnStatement.toString().equals("VARCHAR")) {
                String sizeValue = "(" + column.get("SIZE") + ")";
                columnStatement.append(sizeValue);
            }
            if (column.containsKey("OPTION")) {
                columnStatement.append(" ");
                JSONArray columnOptions = (JSONArray) column.get("OPTION");
                for (int i = 0; i < columnOptions.size() - 1; i++) {
                    String option = columnOptions.get(i) + " ";
                    columnStatement.append(option);
                }
                columnStatement.append(columnOptions.get(columnOptions.size() - 1));
            }
            String columnEntry = columnName + " " + columnStatement + ", ";
            sqlStatementBuilder.append(columnEntry);
        }
        String sqlStatement = sqlStatementBuilder.substring(0, sqlStatementBuilder.length() - 2) + ");";
        dbConnection.createStatement().execute(sqlStatement);
        System.out.printf("%s table created\n", tableName);
    }

    private String buildSqlInsertStatement(String[][] columnEntries) {
        StringBuilder sqlFrontHalf = new StringBuilder("INSERT INTO SENTENCES (");
        StringBuilder sqlBackHalf = new StringBuilder(") VALUES (");
        for(int i = 0; i < columnEntries.length - 1; i++) {
            String front = columnEntries[i][0] + ", ";
            sqlFrontHalf.append(front);

            String back = columnEntries[i][1] + ", ";
            sqlBackHalf.append(back);
        }
        return sqlFrontHalf + columnEntries[columnEntries.length - 1][0] +
                sqlBackHalf + columnEntries[columnEntries.length - 1][1] + ");";
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

    //TODO: ADD FOREIGN KEY CONSTRAINTS TO OCCURRENCES TABLE

    //Checks to make sure the database has all required tables created
    public void checkForMissingTables() throws SQLException {
        ArrayList<String> requiredTables = new ArrayList<>(tables);
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

        for(String table : requiredTables) {
            createTable(table);
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

    private String[][] prepareSentenceColumns(SentenceObject sentence) {
        int sourceKey = sentence.getSentenceKey();
        String sourceType = "'" + sentence.getSourceType() + "'";
        String sourceName = sentence.getSourceName().equals("") ? "NULL" : "'" + sentence.getSourceName() + "'";
        String sourceURL = sentence.getSourceUrl().equals("") ? "NULL" : "'" + sentence.getSourceUrl() + "'";
        String sourceSentence = "'" + sentence.getSentence() + "'";
        String sourceImagePath = sentence.getImagePath();
        String sourceNSFW = sentence.getNsfwTag() ? "TRUE" : "FALSE";
        int sourceBacklink = sentence.getBacklink();
        String sourceCreatedAt = "'" + sentence.getCreatedAt() + "'";
        String sourceUpdatedAt = "NULL";


        return new String[][] {
                {"SENTENCE_KEY", Integer.toString(sourceKey)},
                {"SOURCE_TYPE", sourceType},
                {"SOURCE_NAME", sourceName},
                {"SOURCE_URL", sourceURL},
                {"SENTENCE", sourceSentence},
                {"IMAGE_PATH", sourceImagePath},
                {"NSFW", sourceNSFW},
                {"BACK_LINK", Integer.toString(sourceBacklink)},
                {"CREATED_AT", sourceCreatedAt},
                {"UPDATED_AT", sourceUpdatedAt}
        };
    }

    public void insertSentence(SentenceObject sentence, boolean newImage) throws SQLException {
        String[][] columnEntries = prepareSentenceColumns(sentence);
       String sqlStatement = buildSqlInsertStatement(columnEntries);

        maxSentenceIndex ++;
        System.out.println(sqlStatement);
        dbConnection.createStatement().execute(sqlStatement);
        if(newImage) {
            saveImageToLocal(sentence.getSentenceImage(), sentence.getImagePath(), sentence.getNsfwTag());
        }
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
        String sqlCreatedAt = "'" + sentence.getCreatedAt() + "'";
        String sqlUpdatedAt = "'" + createNowTimestampString() + "'";

        String sql = String.format("UPDATE SENTENCES SET " +
                        "SOURCE_TYPE = %s, " +
                        "SOURCE_NAME = %s, " +
                        "SOURCE_URL = %s, " +
                        "SENTENCE = %s, " +
                        "IMAGE_PATH = %s, " +
                        "NSFW = %s, " +
                        "BACK_LINK = %s, "+
                        "CREATED_AT = %s, " +
                        "UPDATED_AT = %s " +
                        "WHERE SENTENCE_KEY = %s;",
                sqlType, sqlName, sqlUrl, sqlSentence, sqlImagePath, sqlNsfw, sqlBacklink,
                sqlCreatedAt, sqlUpdatedAt, sqlKey);
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
        String createdAt = set.getTimestamp("CREATED_AT").toString();
        Timestamp updatedAtStamp = set.getTimestamp("UPDATED_AT");
        String updatedAt = (updatedAtStamp != null) ? updatedAtStamp.toString() : null;
        BufferedImage sentenceImage = fetchLocalImage(sentenceKey, nsfw);

        return new SentenceObject(
                sentenceKey, sourceType, sourceName, sourceURL, sentence, imgPath, nsfw, backlink,
                createdAt, updatedAt, sentenceImage);
    }

    public static String createNowTimestampString() {
        String[] now = Instant.now().toString().split("[.T]");
        return now[0] + " " + now[1];
    }

    public void saveImageToLocal(BufferedImage sentenceImage, String filename, boolean nsfw) {
        String path;
        if(nsfw) {
            path = settings.getNsfwImagePath();
        } else {
            path = settings.getGeneralImagePath();
        }
        String fullPath = path + filename + ".png";
        File outputFile = new File(fullPath);
        try{
            ImageIO.write(sentenceImage, "png", outputFile);
            System.out.println("Image successfully saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage fetchLocalImage(int sentenceKey, boolean nsfw) {
        String path;
        if(nsfw) {
            path = settings.getNsfwImagePath();
        } else {
            path = settings.getGeneralImagePath();
        }

        return null;
    }

    public int findTotalImageCount() {
        int imageCount = 0;
        File generalImages = new File(settings.getGeneralImagePath());
        File nsfwImages = new File(settings.getNsfwImagePath());

        imageCount += Objects.requireNonNull(generalImages.list()).length;
        imageCount += Objects.requireNonNull(nsfwImages.list()).length;

        return imageCount;
    }

    public int findTotalSourceCount() {
        String sql = "SELECT COUNT(DISTINCT SOURCE_NAME) FROM SENTENCES";
        int sourceCount = -1;
        try {
            ResultSet rs = dbConnection.createStatement().executeQuery(sql);
            while(rs.next())
            {
                sourceCount = rs.getInt("COUNT(DISTINCT SOURCE_NAME)");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return sourceCount;
    }

    //Finds the total number of entries in the last 24 hours
    public int findTodayEntryCount() {
        String[] now = Instant.now().minusSeconds(86400).toString().split("[.T]");
        String formattedNow = String.format("%s %s", now[0], now[1]);

        String sql = String.format(
                "SELECT COUNT(*) FROM SENTENCES WHERE CREATED_AT >= PARSEDATETIME('%s', 'yyyy-MM-dd HH:mm:ss')",
                formattedNow);
        int todayCount = -1;
        try {
            ResultSet rs = dbConnection.createStatement().executeQuery(sql);
            while(rs.next()) {
                todayCount = rs.getInt("COUNT(*)");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return todayCount;
    }

    //Finds the average number of entries per day, since the very first entry
    public double findAverageEntriesPerDay() {

        double entriesPerDay = -1.0;
        String sql = "SELECT * FROM SENTENCES LIMIT 1";

        try {
            ResultSet rs = dbConnection.createStatement().executeQuery(sql);
            Timestamp created;
            Timestamp now = Timestamp.valueOf(Database.createNowTimestampString());
            while(rs.next()) {
                created = rs.getTimestamp("CREATED_AT");
                long timeDifference = now.getTime() - created.getTime();
                int days = (int)(timeDifference / (1000 * 60 * 60 * 24));
                days = days > 0 ? days : 1;
                entriesPerDay = (double)maxSentenceIndex / days;

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return entriesPerDay == -1.0 ? 0.0 : entriesPerDay;
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
