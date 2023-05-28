package settings;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import views.InitializationDialog;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {

    private final String relativeAddress = "./src/main/java/settings/user_settings.json";
    private final String defaultDatabaseName = "plusOneDatabase"; //default name upon creation, use settingJSON for actual name
    private final String defaultDatabaseUser = "po"; //default user upon creation, use settingJSON for actual user
    private final String defaultDatabasePass = ""; //default password upon creation, use settingJSON for actual pass
    private final Path settingsJsonPath = Paths.get(relativeAddress);
    private JSONObject settingsJSON;

    public Settings() {
        if(!verifySettingsExists()) {
            new InitializationDialog(this);
        }

        if(verifySettingsExists()) {
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

    //Verifies that the settings json is present
    public boolean verifySettingsExists() {
        return Files.exists(settingsJsonPath);
    }

    //Writes JSON file with provided paths for database and images
    public void initializeSettingsJSON(String databasePath, String imagePath) {
        JSONObject newSettingsJSON = createSettingsJSON(databasePath, imagePath);
        try{
            writeSettingsToFile(newSettingsJSON);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //Creates settings JSON object with database path and image paths
    @SuppressWarnings("unchecked")
    private JSONObject createSettingsJSON(String databasePath, String imagePath) {
        JSONObject settingJSON = new JSONObject();
        settingJSON.put("DATABASE_NAME", defaultDatabaseName);
        settingJSON.put("DATABASE_USER", defaultDatabaseUser);
        settingJSON.put("DATABASE_PASS", defaultDatabasePass);
        settingJSON.put("DATABASE_PATH", databasePath);
        settingJSON.put("IMAGE_PATH", imagePath);
        settingJSON.put("GENERAL_IMAGES", imagePath + "general/");
        settingJSON.put("NSFW_IMAGES", imagePath + "nsfw/");
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
