package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Bank {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    /**
     * get file bank/bankName.yml
     * @param bankName string
     * @return file
     * @since many moons ago
     */
    public File getFile(String bankName) {
        return new File(getInstance().getDataFolder(), "bank/" + bankName + ".yml");
    }
    /**
     * if file exists
     * @param bankName string
     * @return true if file exists else false
     * @since many moons ago
     */
    public boolean exists(String bankName) {
        return getFile(bankName).exists();
    }
    /**
     * get listed
     * @return list string
     * @since many moons ago
     */
    public List<String> getListed() {
        var listed = new ArrayList<String>();
        var folder = new File(getInstance().getDataFolder(), "bank");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    listed.add(file.getName().replace(".yml", ""));
                }
            }
        }
        return listed;
    }
    /**
     * get bankName config
     * @param bankName string
     * @return config
     * @since many moons ago
     */
    public FileConfiguration getConfig(String bankName) {
        return YamlConfiguration.loadConfiguration(getFile(bankName));
    }
    /**
     * create bank
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if file was created else false if file exists
     * @since many moons ago
     */
    public boolean create(String bankName, OfflinePlayer offlinePlayer) {
        if (!exists(bankName)) {
            var file = getFile(bankName);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("owner", offlinePlayer.getUniqueId().toString());
            config.set("account", getEconomyHandler().getStartingBank());
            config.set("members", new ArrayList<String>());
            try {
                config.save(file);
                getUserdata().setString(offlinePlayer, "bank", bankName);
                getUserdata().setString(offlinePlayer, "bank-rank", "owner");
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    /**
     * rename bank
     * @param bankName string
     * @param name string
     * @return true if file was renamed else false if file exists
     * @since many moons ago
     */
    public boolean rename(String bankName, String name) {
        if (exists(bankName)) {
            if (!exists(name)) {
                return getFile(bankName).renameTo(getFile(name));
            } else return false;
        } else return false;
    }
    /**
     * delete bank
     * @param bankName string
     * @return true if file was deleted else false if file does not exist
     * @since many moons ago
     */
    public boolean delete(String bankName) {
        if (exists(bankName)) {
            if (!getMembers(bankName).isEmpty()) {
                for (var member : getMembers(bankName)) {
                    getUserdata().setString(member, "bank", "");
                    getUserdata().setString(member, "bank-rank", "default");
                }
            }
            var owner = getOwner(bankName);
            getUserdata().setString(owner, "bank", "");
            getUserdata().setString(owner, "bank-rank", "default");
            getFile(bankName).delete();
            return true;
        } else return false;
    }
    /**
     * get owner offlinePlayer
     * @param bankName string
     * @return offlinePlayer if bank exists else null
     * @since many moons ago
     */
    public OfflinePlayer getOwner(String bankName) {
        if (exists(bankName)) {
            var uuidString = getConfig(bankName).getString("owner");
            if (uuidString != null) {
                return getInstance().getOfflinePlayer(UUID.fromString(uuidString));
            } else return null;
        } else return null;
    }
    /**
     * is owner
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is bank owner else false
     * @since many moons ago
     */
    public boolean isOwner(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getOwner(bankName).equals(offlinePlayer);
        } else return false;
    }
    /**
     * get list offlinePlayer
     * @param bankName string
     * @return list offlinePlayer if exists else empty list
     * @since many moons ago
     */
    public List<OfflinePlayer> getMembers(String bankName) {
        var listed = new ArrayList<OfflinePlayer>();
        if (exists(bankName)) {
            var stringList = getConfig(bankName).getStringList("members");
            if (!stringList.isEmpty()) {
                for (var string : getConfig(bankName).getStringList("members")) {
                    listed.add(getInstance().getOfflinePlayer(UUID.fromString(string)));
                }
            }
        }
        return listed;
    }
    /**
     * is member
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is member else false
     * @since many moons ago
     */
    public boolean isMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getMembers(bankName).contains(offlinePlayer);
        } else return false;
    }
    /**
     * add member, if target is not a member and bankName exists
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is added else false
     * @since many moons ago
     */
    public boolean addMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            if (!isMember(bankName, offlinePlayer)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var listed = getConfig(bankName).getStringList("members");
                listed.add(offlinePlayer.getUniqueId().toString());
                config.set("members", listed);
                try {
                    config.save(file);
                    getUserdata().setString(offlinePlayer, "bank", bankName);
                    getUserdata().setString(offlinePlayer, "bank-rank", "default");
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    /**
     * remove member, if target is a member and bankName exists
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is removed else false
     * @since many moons ago
     */
    public boolean removeMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            if (isMember(bankName, offlinePlayer)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var listed = getConfig(bankName).getStringList("members");
                listed.remove(offlinePlayer.getUniqueId().toString());
                config.set("members", listed);
                try {
                    config.save(file);
                    getUserdata().setString(offlinePlayer, "bank", "");
                    getUserdata().setString(offlinePlayer, "bank-rank", "default");
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    /**
     * get bank account
     * @param bankName string
     * @return double if bank exists else 0
     * @since many moons ago
     */
    public double get(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getDouble("account");
        } else return 0;
    }
    /**
     * if bankName has amount
     * @param bankName string
     * @return true if bankName has or over the amount else false
     * @since many moons ago
     */
    public boolean has(String bankName, double amount) {
        return get(bankName) >= amount;
    }
    public boolean remove(String bankName, double amount) {
        if (exists(bankName)) {
            if (has(bankName, amount)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var result = get(bankName) - amount;
                config.set("account", result);
                try {
                    config.save(file);
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    public boolean add(String bankName, double amount) {
        if (exists(bankName)) {
            var file = getFile(bankName);
            var config = YamlConfiguration.loadConfiguration(file);
            var result = amount + get(bankName);
            config.set("account", result);
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public void reload() {
        var folder = new File(getInstance().getDataFolder(), "bank");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    try {
                        config.load(file);
                    } catch (IOException | InvalidConfigurationException e) {
                        getInstance().sendWarning(e.getMessage());
                    }
                }
            }
        }
    }
}