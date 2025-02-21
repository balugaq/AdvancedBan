package com.balugaq.advancedban.core.managers;

import com.balugaq.advancedban.api.Since;
import com.balugaq.advancedban.api.events.BuildStation;
import com.balugaq.advancedban.api.events.ConfigVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigManager {
    @Since(ConfigVersion.C_20250221_1)
    private final boolean AUTO_UPDATE;
    @Since(ConfigVersion.C_20250221_1)
    private final boolean DEBUG;
    @Since(ConfigVersion.C_20250221_1)
    private BuildStation BUILD_STATION;
    @Since(ConfigVersion.C_20250221_1)
    private ConfigVersion CONFIG_VERSION;
    @Since(ConfigVersion.C_20250221_1)
    private final @NotNull String LANGUAGE;
    private final @NotNull JavaPlugin plugin;

    public ConfigManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        setupDefaultConfig();
        try {
            this.CONFIG_VERSION = ConfigVersion.valueOf(plugin.getConfig().getString("config-version", "UNKNOWN").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid config-version value: " + plugin.getConfig().getString("config-version", "UNKNOWN") + ", using default value: UNKNOWN");
            this.CONFIG_VERSION = ConfigVersion.C_UNKNOWN;
        }
        this.AUTO_UPDATE = plugin.getConfig().getBoolean("auto-update", false);
        this.DEBUG = plugin.getConfig().getBoolean("debug", false);
        String buildStationStr = plugin.getConfig().getString("build-station", "Guizhan");
        try {
            this.BUILD_STATION = BuildStation.valueOf(buildStationStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid build-station value: " + buildStationStr + ", using default value: Guizhan");
            this.BUILD_STATION = BuildStation.GUIZHAN;
        }
        this.LANGUAGE = plugin.getConfig().getString("language", "zh-CN");
    }

    private void setupDefaultConfig() {
        // config.yml
        final InputStream inputStream = plugin.getResource("config.yml");
        final File existingFile = new File(plugin.getDataFolder(), "config.yml");

        if (inputStream == null) {
            return;
        }

        final Reader reader = new InputStreamReader(inputStream);
        final FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(reader);
        final FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(existingFile);

        for (String key : resourceConfig.getKeys(false)) {
            checkKey(existingConfig, resourceConfig, key);
        }

        try {
            existingConfig.save(existingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParametersAreNonnullByDefault
    private void checkKey(FileConfiguration existingConfig, FileConfiguration resourceConfig, String key) {
        final Object currentValue = existingConfig.get(key);
        final Object newValue = resourceConfig.get(key);
        if (newValue instanceof ConfigurationSection section) {
            for (String sectionKey : section.getKeys(false)) {
                checkKey(existingConfig, resourceConfig, key + "." + sectionKey);
            }
        } else if (currentValue == null) {
            existingConfig.set(key, newValue);
        }
    }

    public boolean isAutoUpdate() {
        return AUTO_UPDATE;
    }

    public boolean isDebug() {
        return DEBUG;
    }

    public String getLanguage() {
        return LANGUAGE;
    }

    public BuildStation getBuildStation() {
        return BUILD_STATION;
    }

    public ConfigVersion getConfigVersion() {
        return CONFIG_VERSION;
    }
}
