package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.runnable.Board;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "scoreboard.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public ScoreboardManager getScoreboardManager() {
        return getInstance().getScoreboardManager();
    }
    public Scoreboard getMainScoreboard() {
        return getScoreboardManager().getMainScoreboard();
    }
    public Scoreboard getNewScoreboard() {
        return getScoreboardManager().getNewScoreboard();
    }
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public boolean isEnable() {
        return config.getBoolean("enable");
    }
    public long getTick() {
        return config.getLong("tick");
    }
    public boolean hasTitle() {
        return config.isString("title");
    }
    public String getTitle() {
        return config.getString("title");
    }
    public boolean isLine() {
        return config.isList("lines");
    }
    public List<String> getLines() {
        return config.getStringList("lines").reversed();
    }
    public long getTick(String worldName) {
        return config.getLong("worlds." + worldName + ".tick");
    }
    public boolean hasTitle(String worldName) {
        return config.isString("worlds." + worldName + ".title");
    }
    public String getTitle(String worldName) {
        return config.getString("worlds." + worldName + ".title");
    }
    public boolean isLine(String worldName) {
        return config.isList("worlds." + worldName + ".lines");
    }
    public List<String> getLines(String worldName) {
        return config.getStringList("worlds." + worldName + ".lines").reversed();
    }
    public void enable() {
        var onlinePlayers = getInstance().getOnlinePlayers();
        if (onlinePlayers.isEmpty())return;
        onlinePlayers.forEach(this::apply);
    }
    public void apply(Player player) {
        if (!isEnable())return;
        if (hasBoard(player))return;
        var world = player.getWorld().getName();
        if (hasTitle(world) && isLine(world)) {
            getUserdata().addTaskID(player, "board", getScheduleHandler().runTimer(new Board(player), 0, getTick(world)).getTaskId());
        } else if (hasTitle() && isLine()) {
            getUserdata().addTaskID(player, "board", getScheduleHandler().runTimer(new Board(player), 0, getTick()).getTaskId());
        }
    }
    public void disable(Player player) {
        if (!hasBoard(player))return;
        getUserdata().removeTask(player, "board");
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }
    public void disable() {
        var onlinePlayers = getInstance().getOnlinePlayers();
        if (onlinePlayers.isEmpty())return;
        onlinePlayers.forEach(this::disable);
    }
    public boolean hasBoard(Player player) {
        return getUserdata().hasTaskID(player, "board");
    }
    private boolean setup() {
        var lines = new ArrayList<String>();
        lines.add("&ename&f: %essentials_display_name%");
        lines.add("&erank&f: %vault_prefix%%vault_rank%%vault_suffix%");
        lines.add("&ecoins&f:&a €%vault_eco_balance_formatted%");
        config.set("enable", false);
        config.set("tick", 20);
        config.set("title", "&6&lStats");
        config.set("lines", lines);
        config.set("worlds.world.tick", 20);
        config.set("worlds.world.title", "&6&lStats");
        config.set("worlds.world.lines", lines);
        config.set("worlds.test.tick", 20);
        config.set("worlds.test.title", "&6&lStats");
        config.set("worlds.test.lines", lines);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    public boolean reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
}