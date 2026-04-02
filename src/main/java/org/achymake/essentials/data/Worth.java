package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Worth {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "worth.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    /**
     * get listed
     * @return set string
     * @since many moons ago
     */
    public List<String> getListed() {
        var listed = new ArrayList<>(config.getKeys(false).stream().toList());
        listed.sort(String::compareTo);
        return listed;
    }
    /**
     * if item is listed
     * @param material material
     * @return true if material contains listed else false
     * @since many moons ago
     */
    public boolean isListed(Material material) {
        return getListed().contains(material.toString());
    }
    /**
     * get item worth
     * @param material material
     * @return double
     * @since many moons ago
     */
    public double get(Material material) {
        return config.getDouble(material.toString());
    }
    /**
     * set item worth
     * @param material material
     * @param value double
     * @since many moons ago
     */
    public boolean setWorth(Material material, double value) {
        if (value > 0) {
            config.set(material.toString(), value);
        } else config.set(material.toString(), null);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * setup worth.yml
     * @since many moons ago
     */
    private boolean setup() {
        config.options().copyDefaults(true);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * reload worth.yml if exists else setup
     * @since many moons ago
     */
    public boolean reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
}