package io.minecast.minecast;

import io.minecast.minecast.gui.MenuHandler;
import io.minecast.minecast.listeners.hooks.MobArenaListener;
import io.minecast.minecast.util.Lang;
import io.minecast.minecast.util.Metrics;
import io.minecast.minecast.util.Updater;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public class Minecast extends JavaPlugin {

    private static Minecast instance;
    private boolean update = false;
    private String name = "";

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadLang();
        getServer().getPluginManager().registerEvents(MenuHandler.getInstance(), this);
        checkUpdate();
        startMetrics();
        checkHooks();
    }

    protected void checkHooks() {
        if (getConfig().getBoolean("hooks.mobarena", false)) {
            Plugin mobarena = getServer().getPluginManager().getPlugin("MobArena");
            if (mobarena != null) {
                getServer().getPluginManager().registerEvents(new MobArenaListener(), this);
            }
        }
    }

    protected void checkUpdate() {
        if (getConfig().getBoolean("check-update", true)) {
            final Minecast plugin = this;
            final File file = this.getFile();
            final Updater.UpdateType updateType = (getConfig().getBoolean("auto-update", true) ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD);
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(plugin, 50123, file, updateType, false);
                    update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    name = updater.getLatestName();
                    if (updater.getResult() == Updater.UpdateResult.SUCCESS) {
                        getLogger().log(Level.INFO, "Successfully updated Minecast to version {0} for next restart!", updater.getLatestName());
                    } else if (updater.getResult() == Updater.UpdateResult.NO_UPDATE) {
                        getLogger().log(Level.INFO, "We didn't find an update!");
                    }
                }
            });
        }
    }

    protected void startMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            getLogger().warning("Failed to load metrics :(");
        }
    }

    public static Minecast getInstance() {
        return instance;
    }

    // EVERYTHING BELOW HERE IS FOR LANG FILE PLZ NO TOUCH

    private static YamlConfiguration LANG;
    private static File LANG_FILE;

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
