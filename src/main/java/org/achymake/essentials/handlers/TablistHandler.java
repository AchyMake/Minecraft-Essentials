package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.runnable.Tab;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TablistHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "tablist.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public File getFile() {
        return file;
    }
    /**
     * get tablist.yml
     * @return config
     * @since many moons ago
     */
    public FileConfiguration getConfig() {
        return config;
    }
    /**
     * is enabled
     * @return true if enabled else false
     * @since many moons ago
     */
    public boolean isEnable() {
        return config.getBoolean("enable");
    }
    /**
     * get tick
     * @return long
     * @since many moons ago
     */
    public long getTick() {
        return config.getLong("tick");
    }
    /**
     * has tablist name
     * @return true if name is string else false
     * @since many moons ago
     */
    public boolean hasName() {
        return config.isString("name");
    }
    public String getName() {
        return config.getString("name");
    }
    /**
     * has tablist header
     * @return true if header is list string else false
     * @since many moons ago
     */
    public boolean hasHeaderLines() {
        return config.isList("header.lines");
    }
    public List<String> getHeaderLines() {
        return config.getStringList("header.lines");
    }
    /**
     * has tablist footer
     * @return true if footer is list string else false
     * @since many moons ago
     */
    public boolean hasFooterLines() {
        return config.isList("footer.lines");
    }
    public List<String> getFooterLines() {
        return config.getStringList("footer.lines");
    }
    /**
     * get world tick
     * @return long
     * @since many moons ago
     */
    public long getTick(String worldName) {
        return config.getLong("worlds." + worldName + ".tick");
    }
    /**
     * has world tablist name
     * @return true if name is string else false
     * @since many moons ago
     */
    public boolean hasName(String worldName) {
        return config.isString("worlds." + worldName + ".name");
    }
    public String getName(String worldName) {
        return config.getString("worlds." + worldName + ".name");
    }
    /**
     * has world tablist header
     * @return true if header is list string else false
     * @since many moons ago
     */
    public boolean hasHeaderLines(String worldName) {
        return config.isList("worlds." + worldName + ".header.lines");
    }
    public List<String> getHeaderLines(String worldName) {
        return config.getStringList("worlds." + worldName + ".header.lines");
    }
    /**
     * has world tablist footer
     * @return true if footer is list string else false
     * @since many moons ago
     */
    public boolean hasFooterLines(String worldName) {
        return config.isList("worlds." + worldName + ".footer.lines");
    }
    public List<String> getFooterLines(String worldName) {
        return config.getStringList("worlds." + worldName + ".footer.lines");
    }
    public void enable() {
        var onlinePlayers = getInstance().getOnlinePlayers();
        if (onlinePlayers.isEmpty())return;
        onlinePlayers.forEach(this::apply);
    }
    /**
     * apply tablist
     * @param player target
     * @since many moons ago
     */
    public void apply(Player player) {
        if (!isEnable())return;
        if (hasTab(player))return;
        var world = player.getWorld().getName();
        if (hasName(world) && hasHeaderLines(world) && hasFooterLines(world)) {
            getUserdata().addTaskID(player, "tab", getScheduleHandler().runTimer(new Tab(player), 0, getTick(world)).getTaskId());
        } else if (hasName() && hasHeaderLines() && hasFooterLines()) {
            getUserdata().addTaskID(player, "tab", getScheduleHandler().runTimer(new Tab(player), 0, getTick()).getTaskId());
        }
    }
    /**
     * disable tablist
     * @param player target
     * @since many moons ago
     */
    public void disable(Player player) {
        if (!hasTab(player))return;
        getUserdata().removeTask(player, "tab");
        player.setPlayerListHeader(null);
        player.setPlayerListName(player.getName());
        player.setPlayerListFooter(null);
    }
    public void disable() {
        var onlinePlayers = getInstance().getOnlinePlayers();
        if (onlinePlayers.isEmpty())return;
        onlinePlayers.forEach(this::disable);
    }
    /**
     * setup
     * @since many moons ago
     */
    private boolean setup() {
        config.set("enable", false);
        var header = new ArrayList<String>();
        header.add("&6--------&l[&e&lplay.yourserver.org&6&l]&6--------");
        var footer = new ArrayList<String>();
        footer.add("&6--------&l[&f%essentials_online_players%&e/&f%server_max_players%&6&l]&6--------");
        config.set("tick", 20);
        config.set("name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("header.lines", header);
        config.set("footer.lines", footer);
        config.set("worlds.world.tick", 20);
        config.set("worlds.world.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.world.header.lines", header);
        config.set("worlds.world.footer.lines", footer);
        var testHeader = new ArrayList<String>();
        testHeader.add("&6--------&l[&e&lplay.yourserver.org&6&l]&6--------");
        testHeader.add("&etime&f: %server_time_hh mm ss%&e, date&f: %server_time_dd MM yyyy%");
        testHeader.add("&etps&f: %server_tps%&f");
        var testFooter = new ArrayList<String>();
        testFooter.add("&echunks&f: %server_total_chunks%&e, entities&f: %server_total_living_entities%&e/&f%server_total_entities%");
        testFooter.add("&eram&f: %server_ram_used%&e/&f%server_ram_max%");
        testFooter.add("&euptime&f: %server_uptime%");
        testFooter.add("&6--------&l[&f%essentials_online_players%&e/&f%server_max_players%&6&l]&6--------");
        config.set("worlds.test.tick", 20);
        config.set("worlds.test.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.test.header.lines", testHeader);
        config.set("worlds.test.footer.lines", testFooter);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * reload tablist.yml
     * @since many moons ago
     */
    public boolean reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
    public boolean hasTab(Player player) {
        return getUserdata().hasTaskID(player, "tab");
    }
    public int getWeight(Player player) {
        if (getInstance().getPluginManager().isPluginEnabled("LuckPerms")) {
            return getInstance().getLuckPermsProvider().getWeighted(player);
        } else return 0;
    }
}