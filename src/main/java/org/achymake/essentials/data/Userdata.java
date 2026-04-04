package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.*;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class Userdata {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private UUIDHandler getUUIDHandler() {
        return getInstance().getUUIDHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    /**
     * gets userdata/uuid.yml
     * @param offlinePlayer or player
     * @return file
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getInstance().getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    /**
     * if file exists
     * @param offlinePlayer or player
     * @return true if file exists else false
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean exists(OfflinePlayer offlinePlayer) {
        return getFile(offlinePlayer).exists();
    }
    /**
     * config of userdata/uuid.yml
     * @param offlinePlayer or player
     * @return config
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    public boolean setObject(OfflinePlayer offlinePlayer, String path, Object object) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, object);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets string
     * @param offlinePlayer or player
     * @param path path
     * @param value string
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setString(OfflinePlayer offlinePlayer, String path, String value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets list string
     * @param offlinePlayer or player
     * @param path path
     * @param value list string
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets double
     * @param offlinePlayer or player
     * @param path path
     * @param value double
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets int
     * @param offlinePlayer or player
     * @param path path
     * @param value int
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setInt(OfflinePlayer offlinePlayer, String path, int value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets float
     * @param offlinePlayer or player
     * @param path path
     * @param value float
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets long
     * @param offlinePlayer or player
     * @param path path
     * @param value long
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setLong(OfflinePlayer offlinePlayer, String path, long value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * sets boolean
     * @param offlinePlayer or player
     * @param path path
     * @param value boolean
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        return setObject(offlinePlayer, path, value);
    }
    /**
     * has joined
     * @param offlinePlayer or player
     * @return true if player has joined else false
     * @since many moons ago
     */
    public boolean hasJoined(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            return isLocation(offlinePlayer, "quit");
        } else return false;
    }
    /**
     * get display name
     * @param offlinePlayer or player
     * @return display name if null else name
     * @since many moons ago
     */
    public String getDisplayName(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("display-name")) {
            return config.getString("display-name");
        } else return offlinePlayer.getName();
    }
    /**
     * gets boolean for 'settings.jailed' and 'settings.frozen'
     * @param offlinePlayer or player
     * @return true if player is frozen or jailed else false
     * @since many moons ago
     */
    public boolean isDisabled(OfflinePlayer offlinePlayer) {
        return isFrozen(offlinePlayer) || isJailed(offlinePlayer);
    }
    /**
     * gets boolean for 'settings.pvp'
     * @param offlinePlayer or player
     * @return true if player has pvp enabled else false
     * @since many moons ago
     */
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.pvp");
    }
    /**
     * gets boolean for 'settings.frozen'
     * @param offlinePlayer or player
     * @return true if player is frozen else false
     * @since many moons ago
     */
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.frozen");
    }
    /**
     * gets boolean for 'settings.jailed'
     * @param offlinePlayer or player
     * @return true if player is jailed else false
     * @since many moons ago
     */
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.jailed");
    }
    /**
     * gets boolean for 'settings.muted'
     * @param offlinePlayer or player
     * @return true if player is muted else false
     * @since many moons ago
     */
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.muted");
    }
    /**
     * gets boolean for 'settings.banned'
     * @param offlinePlayer or player
     * @return true if player is banned else false
     * @since many moons ago
     */
    public boolean isBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.banned");
    }
    /**
     * gets string for 'settings.ban-reason'
     * @param offlinePlayer or player
     * @return string
     * @since many moons ago
     */
    public String getBanReason(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("settings.ban-reason")) {
            return config.getString("settings.ban-reason");
        } else return "None";
    }
    /**
     * gets long for 'settings.ban-expire'
     * @param offlinePlayer or player
     * @return long
     * @since many moons ago
     */
    public long getBanExpire(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getLong("settings.ban-expire");
    }
    /**
     * gets boolean for 'settings.board'
     * @param offlinePlayer or player
     * @return true if offlinePlayer has board enabled else false
     * @since many moons ago
     */
    public boolean hasBoard(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.board");
    }
    /**
     * gets boolean for 'settings.vanished'
     * @param offlinePlayer or player
     * @return true if target is vanished else false
     * @since many moons ago
     */
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.vanished");
    }
    public void setLastWhisper(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("last-whisper"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("last-whisper"));
    }
    /**
     * gets player for 'last-whisper'
     * @param player player
     * @return player else null
     * @since many moons ago
     */
    public Player getLastWhisper(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("last-whisper"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setBankSent(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("bank.sent"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("bank.sent"));
    }
    /**
     * gets player for 'bank.sent'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getBankSent(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("bank.sent"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setBankFrom(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("bank.from"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("bank.from"));
    }
    /**
     * gets player for 'bank.from'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getBankFrom(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("bank.from"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setTpaSent(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("tpa.sent"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("tpa.sent"));
    }
    /**
     * gets player for 'tpa.sent'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getTpaSent(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("tpa.sent"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setTpaFrom(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("tpa.from"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("tpa.from"));
    }
    /**
     * gets player for 'tpa.from'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getTpaFrom(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("tpa.from"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setTpaHereSent(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("tpahere.sent"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("tpahere.sent"));
    }
    /**
     * gets player for 'tpahere.sent'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getTpaHereSent(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("tpahere.sent"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    public void setTpaHereFrom(Player player, Player target) {
        var data = getEntityHandler().getData(player);
        if (target != null) {
            data.set(getInstance().getKey("tpahere.from"), PersistentDataType.STRING, target.getUniqueId().toString());
        } else data.remove(getInstance().getKey("tpahere.from"));
    }
    /**
     * gets player for 'tpahere.from'
     * @param player player
     * @return player else null if none
     * @since many moons ago
     */
    public Player getTpaHereFrom(Player player) {
        var data = getEntityHandler().getData(player);
        var tpaFrom = data.get(getInstance().getKey("tpahere.from"), PersistentDataType.STRING);
        if (tpaFrom != null) {
            return getInstance().getPlayer(getUUIDHandler().get(tpaFrom));
        } else return null;
    }
    /**
     * @param offlinePlayer or player
     * @param homeName string
     * @return true if homeName exists else false
     * @since many moons ago
     */
    public boolean isHome(OfflinePlayer offlinePlayer, String homeName) {
        return getHomes(offlinePlayer).contains(homeName);
    }
    /**
     * gets set string of homes
     * @param offlinePlayer or player
     * @return set string
     * @since many moons ago
     */
    public Set<String> getHomes(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false);
    }
    /**
     * get player max home
     * @return integer
     * @since many moons ago
     */
    public int getMaxHomes(Player player) {
        if (!player.isOp()) {
            var listed = new ArrayList<Integer>();
            for (var value : getConfig().getConfigurationSection("homes").getKeys(false)) {
                if (player.hasPermission("essentials.command.sethome.multiple." + value)) {
                    listed.add(getConfig().getInt("homes." + value));
                }
            }
            listed.sort(Integer::compareTo);
            if (!listed.isEmpty()) {
                return listed.getLast();
            } else return getConfig().getInt("homes.default");
        } else return getConfig().getInt("homes.op");
    }
    /**
     * get home
     * @param offlinePlayer or player
     * @param homeName string
     * @return location if homeName exists else null
     * @since many moons ago
     */
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        if (isHome(offlinePlayer, homeName)) {
            var config = getConfig(offlinePlayer);
            var world = getWorldHandler().get(config.getString("homes." + homeName + ".world"));
            if (world != null) {
                var x = config.getDouble("homes." + homeName + ".x");
                var y = config.getDouble("homes." + homeName + ".y");
                var z = config.getDouble("homes." + homeName + ".z");
                var yaw = config.getLong("homes." + homeName + ".yaw");
                var pitch = config.getLong("homes." + homeName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    /**
     * set home
     * @param offlinePlayer or player
     * @param location location or null
     * @param homeName string
     * @return true if file saved else false and nothing changed
     * @since many moons ago
     */
    public boolean setHome(OfflinePlayer offlinePlayer, Location location, String homeName) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        if (location != null) {
            config.set("homes." + homeName + ".world", location.getWorld().getName());
            config.set("homes." + homeName + ".x", location.getX());
            config.set("homes." + homeName + ".y", location.getY());
            config.set("homes." + homeName + ".z", location.getZ());
            config.set("homes." + homeName + ".yaw", location.getYaw());
            config.set("homes." + homeName + ".pitch", location.getPitch());
        } else config.set("homes." + homeName, null);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * is location set
     * @param offlinePlayer or player
     * @param locationName string
     * @return true if locationName exists else false
     * @since many moons ago
     */
    public boolean isLocation(OfflinePlayer offlinePlayer, String locationName) {
        return getLocations(offlinePlayer).contains(locationName);
    }
    /**
     * get locations name
     * @param offlinePlayer or player
     * @return set string
     * @since many moons ago
     */
    public Set<String> getLocations(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("locations").getKeys(false);
    }
    /**
     * get location
     * @param offlinePlayer or player
     * @param locationName string
     * @return location if locationName exists else null
     * @since many moons ago
     */
    public Location getLocation(OfflinePlayer offlinePlayer, String locationName) {
        if (isLocation(offlinePlayer, locationName)) {
            var config = getConfig(offlinePlayer);
            var world = getWorldHandler().get(config.getString("locations." + locationName + ".world"));
            if (world != null) {
                var x = config.getDouble("locations." + locationName + ".x");
                var y = config.getDouble("locations." + locationName + ".y");
                var z = config.getDouble("locations." + locationName + ".z");
                var yaw = config.getLong("locations." + locationName + ".yaw");
                var pitch = config.getLong("locations." + locationName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    /**
     * set location
     * @param offlinePlayer or player
     * @param location location
     * @param locationName string
     * @since many moons ago
     */
    public boolean setLocation(OfflinePlayer offlinePlayer, Location location, String locationName) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("locations." + locationName + ".world", location.getWorld().getName());
        config.set("locations." + locationName + ".x", location.getX());
        config.set("locations." + locationName + ".y", location.getY());
        config.set("locations." + locationName + ".z", location.getZ());
        config.set("locations." + locationName + ".yaw", location.getYaw());
        config.set("locations." + locationName + ".pitch", location.getPitch());
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * add task id
     * @param player player
     * @param taskName string
     * @param value integer
     * @since many moons ago
     */
    public void addTaskID(Player player, String taskName, int value) {
        setInt(player, "tasks." + taskName, value);
    }
    /**
     * has task id
     * @param player player
     * @param taskName string
     * @return true if target has task else false
     * @since many moons ago
     */
    public boolean hasTaskID(Player player, String taskName) {
        return getConfig(player).isInt("tasks." + taskName);
    }
    /**
     * get task id
     * @param player player
     * @param taskName string
     * @return integer
     * @since many moons ago
     */
    public int getTaskID(Player player, String taskName) {
        return getConfig(player).getInt("tasks." + taskName);
    }
    /**
     * remove task which will likely cancel first if its scheduled
     * @param player player
     * @param taskName string
     * @since many moons ago
     */
    public void removeTask(Player player, String taskName) {
        if (getScheduleHandler().isQueued(getTaskID(player, taskName))) {
            getScheduleHandler().cancel(getTaskID(player, taskName));
        }
        setString(player, "tasks." + taskName, null);
    }
    /**
     * this will cancel then remove all target tasks
     * @param player player
     * @since many moons ago
     */
    public void disableTasks(Player player) {
        var section = getConfig(player).getConfigurationSection("tasks").getKeys(false);
        if (section.isEmpty())return;
        section.forEach(s -> removeTask(player, s));
    }
    /**
     * gets main config chat format
     * @param player target
     * @since many moons ago
     */
    public String getChatFormat(Player player, boolean vanished) {
        if (!vanished) {
            if (!player.isOp()) {
                for (var value : getConfig().getConfigurationSection("chat.format").getKeys(false)) {
                    if (player.hasPermission("essentials.event.chat.group" + value)) {
                        return getConfig().getString("chat.format." + value);
                    }
                }
                return getConfig().getString("chat.format.default");
            } else return getConfig().getString("chat.format.op");
        } else return getConfig().getString("chat.format.vanished");
    }
    /**
     * get default fly speed
     * @since many moons ago
     */
    public float getDefaultFlySpeed() {
        return 0.1F;
    }
    /**
     * get default walk speed
     * @since many moons ago
     */
    public float getDefaultWalkSpeed() {
        return 0.2F;
    }
    /**
     * sets fly speed
     * @param player target
     * @param amount float
     * @since many moons ago
     */
    public void setFlySpeed(Player player, float amount) {
        if (amount > 0) {
            player.setFlySpeed(getDefaultFlySpeed() * amount);
        } else player.setFlySpeed(getDefaultFlySpeed());
    }
    /**
     * sets walk speed
     * @param player target
     * @param amount float
     * @since many moons ago
     */
    public void setWalkSpeed(Player player, float amount) {
        if (amount > 0) {
            player.setWalkSpeed(getDefaultWalkSpeed() * amount);
        } else player.setWalkSpeed(getDefaultWalkSpeed());
    }
    /**
     * set morning for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setMorning(Player player) {
        setTime(player, 0);
    }
    /**
     * set day for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setDay(Player player) {
        setTime(player, 1000);
    }
    /**
     * set noon for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setNoon(Player player) {
        setTime(player, 6000);
    }
    /**
     * set night for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setNight(Player player) {
        setTime(player, 13000);
    }
    /**
     * set midnight for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setMidnight(Player player) {
        setTime(player, 18000);
    }
    /**
     * set target time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void setTime(Player player, long value) {
        player.setPlayerTime(value, false);
    }
    /**
     * add time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void addTime(Player player, long value) {
        setTime(player, player.getPlayerTime() + value);
    }
    /**
     * remove target time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void removeTime(Player player, long value) {
        setTime(player, player.getPlayerTime() - value);
    }
    /**
     * reset target time, this will reset to server time relative
     * @param player target
     * @since many moons ago
     */
    public void resetTime(Player player) {
        player.resetPlayerTime();
    }
    /**
     * set weather for target
     * @param player target
     * @param weatherType string
     * @since many moons ago
     */
    public void setWeather(Player player, String weatherType) {
        if (weatherType.equalsIgnoreCase("clear")) {
            player.setPlayerWeather(WeatherType.CLEAR);
        } else if (weatherType.equalsIgnoreCase("rain")) {
            player.setPlayerWeather(WeatherType.DOWNFALL);
        } else if (weatherType.equalsIgnoreCase("reset")) {
            player.resetPlayerWeather();
        }
    }
    /**
     * sets file up for target
     * @param offlinePlayer or player
     * @since many moons ago
     */
    private boolean setup(OfflinePlayer offlinePlayer) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", offlinePlayer.getName());
        config.set("display-name", offlinePlayer.getName());
        config.set("account", getEconomyHandler().getStartingBalance());
        if (getEconomyHandler().autoCreateBank()) {
            getInstance().getBank().create(offlinePlayer.getName(), offlinePlayer);
            config.set("bank", offlinePlayer.getName());
            config.set("bank-rank", "owner");
        } else {
            config.set("bank", "");
            config.set("bank-rank", "default");
        }
        config.set("settings.board", !hasBoard(offlinePlayer));
        config.set("settings.pvp", !isPVP(offlinePlayer));
        config.set("settings.muted", isMuted(offlinePlayer));
        config.set("settings.frozen", isFrozen(offlinePlayer));
        config.set("settings.jailed", isJailed(offlinePlayer));
        config.set("settings.banned", isBanned(offlinePlayer));
        config.set("settings.ban-expire", 0);
        config.set("settings.vanished", isVanished(offlinePlayer));
        config.createSection("homes");
        config.createSection("locations");
        config.createSection("tasks");
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    private void update(OfflinePlayer offlinePlayer) {
        if (!exists(offlinePlayer))return;
        var name = offlinePlayer.getName();
        var fileName = getConfig(offlinePlayer).getString("name");
        if (fileName == null)return;
        if (fileName.equals(name))return;
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        config.set("display-name", name);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    /**
     * reloads target file else setup if file does not exist
     * @param offlinePlayer or player
     * @since many moons ago
     */
    public boolean reload(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            update(offlinePlayer);
            var file = getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
                return true;
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return setup(offlinePlayer);
    }
    /**
     * reload userdata folder
     * @since many moons ago
     */
    public void reload() {
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (!folder.exists())return;
        if (!folder.isDirectory())return;
        for (var file : folder.listFiles()) {
            if (!file.exists())return;
            if (!file.isFile())return;
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
        }
    }
    /**
     * get offlinePlayers from userdata folder
     * @return list offlinePlayer
     * @since many moons ago
     */
    public Collection<OfflinePlayer> getOfflinePlayers() {
        var listed = new HashSet<OfflinePlayer>();
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var uuidString = file.getName().replace(".yml", "");
                    listed.add(getInstance().getOfflinePlayer(getUUIDHandler().get(uuidString)));
                }
            }
        }
        return listed;
    }
    public boolean unban(OfflinePlayer offlinePlayer) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.banned", false);
        config.set("settings.ban-reason", null);
        config.set("settings.ban-expire", 0);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * has cooldown
     * @param player player
     * @param name string
     * @param seconds integer
     * @return true if target has cooldown else false
     * @since many moons ago
     */
    public boolean hasCooldown(Player player, String name, int seconds) {
        var cooldownString = getEntityHandler().getData(player).get(getInstance().getKey("cooldown"), PersistentDataType.STRING);
        if (cooldownString != null) {
            var cooldown = getMessage().getMapStringLong(cooldownString);
            getMessage().getMapStringLong(cooldownString).containsKey(name);
            if (cooldown.containsKey(name)) {
                var timeElapsed = System.currentTimeMillis() - cooldown.get(name);
                return timeElapsed < Integer.parseInt(seconds + "000");
            } else return false;
        } else return false;
    }
    /**
     * add cooldown
     * @param player player
     * @param name string
     * @param seconds integer
     * @since many moons ago
     */
    public void addCooldown(Player player, String name, int seconds) {
        var data = getEntityHandler().getData(player);
        var cooldownString = data.get(getInstance().getKey("cooldown"), PersistentDataType.STRING);
        if (cooldownString != null) {
            var cooldown = getMessage().getMapStringLong(cooldownString);
            if (cooldown.containsKey(name)) {
                var timeElapsed = System.currentTimeMillis() - cooldown.get(name);
                if (timeElapsed > Integer.parseInt(seconds + "000")) {
                    cooldown.put(name, System.currentTimeMillis());
                }
            } else cooldown.put(name, System.currentTimeMillis());
            data.set(getInstance().getKey("cooldown"), PersistentDataType.STRING, cooldown.toString());
        } else {
            var cooldown = new HashMap<String, Long>();
            cooldown.put(name, System.currentTimeMillis());
            data.set(getInstance().getKey("cooldown"), PersistentDataType.STRING, cooldown.toString());
        }
    }
    /**
     * get cooldown
     * @param player player
     * @param name string
     * @param seconds integer
     * @return string
     * @since many moons ago
     */
    public String getCooldown(Player player, String name, int seconds) {
        var data = getEntityHandler().getData(player);
        var cooldownString = data.get(getInstance().getKey("cooldown"), PersistentDataType.STRING);
        if (cooldownString != null) {
            var cooldown = getMessage().getMapStringLong(cooldownString);
            if (cooldown.containsKey(name)) {
                var timeElapsed = System.currentTimeMillis() - cooldown.get(name);
                var timerResult = Integer.parseInt(seconds + "000");
                if (timeElapsed < timerResult) {
                    var result = (timerResult - timeElapsed);
                    return String.valueOf(result).substring(0, String.valueOf(result).length() - 3);
                } else return "0";
            } else return "0";
        } else return "0";
    }
    public void disable(Player player) {
        setLocation(player, player.getLocation(), "quit");
        disableTasks(player);
        if (player.getWalkSpeed() != getDefaultWalkSpeed()) {
            setWalkSpeed(player, 0);
        }
        if (player.getFlySpeed() != getDefaultWalkSpeed()) {
            setFlySpeed(player, 0);
        }
        if (getTpaSent(player) != null) {
            setTpaSent(player, null);
        }
        if (getTpaFrom(player) != null) {
            setTpaFrom(player, null);
        }
        if (getTpaHereSent(player) != null) {
            setTpaHereSent(player, null);
        }
        if (getTpaHereFrom(player) != null) {
            setTpaHereFrom(player, null);
        }
        if (getBankSent(player) != null) {
            setBankSent(player, null);
        }
        if (getBankFrom(player) != null) {
            setBankFrom(player, null);
        }
        if (player.isInvulnerable()) {
            player.setInvulnerable(false);
        }
        getInstance().getTablistHandler().disable(player);
        if (hasBoard(player)) {
            getInstance().getScoreboardHandler().disable(player);
        }
        getInstance().getInventoryHandler().getInventories().remove(player);
    }
    public void disable() {
        getInstance().getOnlinePlayers().forEach(this::disable);
    }
}