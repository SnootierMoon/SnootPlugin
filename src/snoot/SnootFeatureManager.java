package snoot;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SnootFeatureManager {

    protected YamlConfiguration data;
    protected final String fileName;
    private boolean file_loaded = false;

    protected SnootFeatureManager() {
        fileName = "";
    }
    protected SnootFeatureManager(String fileName) {
        this.fileName = fileName;
        readFile();
    }

    protected File findFile() {
        File file = new File(Main.getInstance().getDataFolder(), fileName);
        try {
            if (!((!Main.getInstance().getDataFolder().exists() && !Main.getInstance().getDataFolder().mkdirs()) || (!file.exists() && !file.createNewFile()))) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void readFile() {
        File file = findFile();
        file_loaded = true;
        if (file != null) {
            data = YamlConfiguration.loadConfiguration(file);
        }
    }

    protected void writeFile() {
        if (!file_loaded) {
            return;
        }
        File file = findFile();
        if (file == null) {
            return;
        }
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
