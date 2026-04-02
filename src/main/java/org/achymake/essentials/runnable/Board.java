package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public record Board(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScoreboardHandler getScoreboardHandler() {
        return getInstance().getScoreboardHandler();
    }
    private ScoreboardManager getScoreboardManager() {
        return getInstance().getServer().getScoreboardManager();
    }
    private Scoreboard getMainScoreboard() {
        return getScoreboardManager().getMainScoreboard();
    }
    private Scoreboard getNewScoreboard() {
        return getScoreboardManager().getNewScoreboard();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String getTitle() {
        var world = getPlayer().getWorld();
        var worldName = world.getName();
        if (!getScoreboardHandler().hasTitle(worldName)) {
            return getMessage().addPlaceholder(getPlayer(), getScoreboardHandler().getTitle());
        } else return getMessage().addPlaceholder(getPlayer(), getScoreboardHandler().getTitle(worldName));
    }
    private List<String> getLines() {
        var listed = new ArrayList<String>();
        var world = getPlayer().getWorld();
        var worldName = world.getName();
        if (getScoreboardHandler().isLine(worldName)) {
            for (var line : getScoreboardHandler().getLines(worldName)) {
                listed.add(getMessage().addPlaceholder(getPlayer(), line));
            }
        } else if (getScoreboardHandler().isLine()) {
            for (var line : getScoreboardHandler().getLines()) {
                listed.add(getMessage().addPlaceholder(getPlayer(), line));
            }
        }
        return listed;
    }
    private void update() {
        var scoreboard = getMainScoreboard();
        var sidebar = scoreboard.getObjective(getPlayer().getName() + "-board");
        if (sidebar != null) {
            if (getTitle() != null) {
                if (!getTitle().equals(sidebar.getDisplayName())) {
                    sidebar.setDisplayName(getTitle());
                }
                if (!getLines().isEmpty()) {
                    for (int i = 0; i < getLines().size(); i++) {
                        sidebar.getScore(getLines().get(i)).setScore(i);
                    }
                }
            }
        } else create();
    }
    private void create() {
        if (getTitle() != null) {
            var scoreboard = getNewScoreboard();
            var sidebar = scoreboard.registerNewObjective(getPlayer().getName() + "-board", "yummy", getTitle());
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            if (!getInstance().isBukkit()) {
                sidebar.numberFormat(getInstance().getPaperHandler().getBlank());
            }
            if (!getLines().isEmpty()) {
                for (int i = 0; i < getLines().size(); i++) {
                    sidebar.getScore(getLines().get(i)).setScore(i);
                }
            }
            getPlayer().setScoreboard(scoreboard);
        }
    }
    @Override
    public void run() {
        update();
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}