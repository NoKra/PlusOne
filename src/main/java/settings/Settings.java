package settings;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import views.InitializationDialog;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Settings {

    //Setting file info
    private final String relativeAddress = "./user_settings.json";
    private final Path settingsJsonPath = Paths.get(relativeAddress);
    private JSONObject settingsJSON; //Anything potentially user defined should go here

    //Database defaults
    private final String defaultDatabaseLocation = "./database/";
    private final String defaultDatabaseName = "plusOneDatabase"; //default file name upon creation, use settingJSON for actual name
    private final String defaultDatabaseUser = "po"; //default username upon creation, use settingJSON for actual user

    private final String defaultDatabasePass = ""; //default password upon creation, use settingJSON for actual pass

    //Stored Settings
    private final String[] defaultSourceTypes = {"Visual Novel", "Manga", "Anime", "Online", "Newspaper", "Magazine"};

    //Defined colors
    private final Color backgroundGray = new Color(47, 47, 49);
    private final Color successGreen = new Color(  156, 204, 101);
    private final Color problemRed = new Color( 244, 81, 30);
    private final Color selectedBlue = new Color(   33, 150, 243);

    public enum Colors {
        backgroundGray,
        successGreen,
        problemRed,
        selectedBlue
    }

    //Defined fonts
    private final Font uiFont = new Font("Meiryo UI", Font.BOLD, 14);
    private final Font buttonFont = new Font("Verdana", Font.BOLD, 16);
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);

    public enum Fonts {
        uiFont,
        buttonFont,
        jpFont
    }

    public Settings() {
        if(!verifySettingsJsonExists()) {
            new InitializationDialog(this);
        }

        if(verifySettingsJsonExists()) {
            try {
                settingsJSON = (JSONObject) new JSONParser().parse(new FileReader(relativeAddress));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            System.exit(0);
        }

    }

    //Getters
    public String getDatabaseURL() {
        return "jdbc:h2:file:" + settingsJSON.get("DATABASE_PATH") + settingsJSON.get("DATABASE_NAME");
    }

    public String getDatabaseUsername() {
        return String.valueOf(settingsJSON.get("DATABASE_USER"));
    }

    public String getDatabasePassword() {
        return String.valueOf(settingsJSON.get("DATABASE_PASS"));
    }

    public String getDatabaseName() {
        return String.valueOf(settingsJSON.get("DATABASE_NAME"));
    }

    public String getDefaultDatabaseLocation() {return defaultDatabaseLocation;}

    public String getDatabasePath() {
        return String.valueOf(settingsJSON.get("DATABASE_PATH"));
    }

    public String getGeneralImagePath() {
        return String.valueOf(settingsJSON.get("GENERAL_IMAGES"));
    }

    public String getNsfwImagePath() {
        return String.valueOf(settingsJSON.get("NSFW_IMAGES"));
    }

    public String getDatabaseBackupPath() {
        return settingsJSON.get("DATABASE_PATH") + "backup/";
    }

    public String[] getSourceTypes() {
        JSONArray jsonArray =  (JSONArray) settingsJSON.get("SOURCE_TYPES");
        String[] returnArray = new String[jsonArray.size()];
        for(int i = 0; i < jsonArray.size(); i++) {
            returnArray[i] = jsonArray.get(i).toString();
        }
        return returnArray;
    }

    //Allows selection of defined colors, default case is just for breakages
    public Color pickColor(Colors selectedColor) {
        switch(selectedColor) {
            case backgroundGray -> {return backgroundGray;}
            case successGreen -> {return successGreen;}
            case problemRed -> {return problemRed;}
            case selectedBlue -> {return selectedBlue;}
            default -> {return Color.BLUE;}
        }
    }

    //Allows selection of defined fonts, default case is just for breakages
    public Font pickFont(Fonts selectedFont) {
        switch (selectedFont) {
            case uiFont -> {return  uiFont;}
            case buttonFont -> {return buttonFont;}
            case jpFont -> {return jpFont;}
            default -> {return new Font("Symbol", Font.BOLD, 16);}
        }
    }

    //Verifies that the settings json is present
    public boolean verifySettingsJsonExists() {
        return Files.exists(settingsJsonPath);
    }

    //Writes JSON file with provided paths for database and images
    public void initializeSettingsJSON(String databasePath, String imagePath) {
        JSONObject newSettingsJSON = createSettingsJSON(databasePath, imagePath);
        try{
            writeSettingsToFile(newSettingsJSON);
            System.out.println("Settings JSON created");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //Creates settings JSON object with database path and image paths
    @SuppressWarnings("unchecked")
    private JSONObject createSettingsJSON(String databasePath, String imagePath) {
        JSONArray sourceTypeArray = new JSONArray();
        sourceTypeArray.addAll(Arrays.asList(defaultSourceTypes));

        JSONObject settingJSON = new JSONObject();
        settingJSON.put("DATABASE_NAME", defaultDatabaseName);
        settingJSON.put("DATABASE_USER", defaultDatabaseUser);
        settingJSON.put("DATABASE_PASS", defaultDatabasePass);
        settingJSON.put("DATABASE_PATH", databasePath);
        settingJSON.put("IMAGE_PATH", imagePath);
        settingJSON.put("GENERAL_IMAGES", imagePath + "general/");
        settingJSON.put("NSFW_IMAGES", imagePath + "nsfw/");
        settingJSON.put("SOURCE_TYPES", sourceTypeArray);
        return settingJSON;
    }

    //Writes the provided user settings to user_settings.json file
    private void writeSettingsToFile(JSONObject settingsJSON) throws IOException {
        FileWriter file = new FileWriter(relativeAddress);
        file.write(settingsJSON.toJSONString());
        System.out.println("User Settings JSON successfully created");
        file.flush();
        file.close();
    }

}
