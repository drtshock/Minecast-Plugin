package io.minecast.minecast;

import io.minecast.minecast.util.Lang;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public class Minecast extends JavaPlugin {

    public static YamlConfiguration LANG;
    public static File LANG_FILE;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadLang();
    }

    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        OutputStream out = null;
        InputStream defLangStream = this.getResource("lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                if (defLangStream != null) {
                    out = new FileOutputStream(lang);
                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = defLangStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defLangStream);
                    Lang.setFile(defConfig);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace(); // So they notice
                getLogger().severe("[Minecast] Couldn't create language file.");
                getLogger().severe("[Minecast] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            } finally {
                if (defLangStream != null) {
                    try {
                        defLangStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        LANG = conf;
        LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Minecast: Failed to save lang.yml.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the lang.yml config.
     *
     * @return The lang.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Get the lang.yml file.
     *
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }
}
