package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.runnable.Vanish;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishHandler {
    private final List<Player> vanished = new ArrayList<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    /**
     * is vanished
     * @param offlinePlayer offlinePlayer
     * @return true if offlinePlayer is vanished else false
     * @since many moons ago
     */
    public boolean isVanish(OfflinePlayer offlinePlayer) {
        return getUserdata().isVanished(offlinePlayer);
    }
    /**
     * hide vanished for target
     * @param player target
     * @since many moons ago
     */
    public void hideVanished(Player player) {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
        }
    }
    /**
     * toggle vanish
     * @param player target
     * @return true if file saved else false and nothing changed
     * @since many moons ago
     */
    public boolean toggleVanish(Player player) {
        return setVanish(player, !isVanish(player));
    }
    /**
     * toggle vanish
     * @param offlinePlayer offlinePlayer
     * @return true if file saved else false and nothing changed
     * @since many moons ago
     */
    public boolean toggleVanish(OfflinePlayer offlinePlayer) {
        return setVanish(offlinePlayer, !isVanish(offlinePlayer));
    }
    /**
     * set vanish
     * @param offlinePlayer offlinePlayer
     * @param value boolean
     * @return true if file saved else false and nothing changed
     * @since many moons ago
     */
    public boolean setVanish(OfflinePlayer offlinePlayer, boolean value) {
        return getUserdata().setBoolean(offlinePlayer, "settings.vanished", value);
    }
    /**
     * set vanish
     * @param player target
     * @param value boolean
     * @return true if file saved else false and nothing changed
     * @since many moons ago
     */
    public boolean setVanish(Player player, boolean value) {
        if (value) {
            if (getUserdata().setBoolean(player, "settings.vanished", true)) {
                player.setAllowFlight(true);
                player.setInvulnerable(true);
                player.setSleepingIgnored(true);
                player.setCollidable(false);
                player.setSilent(true);
                player.setCanPickupItems(false);
                getInstance().getOnlinePlayers().forEach(players -> players.hidePlayer(getInstance(), player));
                getVanished().add(player);
                getVanished().forEach(vanished -> {
                    player.showPlayer(getInstance(), vanished);
                    vanished.showPlayer(getInstance(), player);
                });
                getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("enable")));
                var taskID = getScheduleHandler().runTimer(new Vanish(player), 0, 50).getTaskId();
                getUserdata().addTaskID(player, "vanish", taskID);
                return true;
            } else return false;
        } else {
            if (getUserdata().setBoolean(player, "settings.vanished", false)) {
                if (!player.hasPermission("essentials.command.fly")) {
                    player.setAllowFlight(false);
                }
                player.setInvulnerable(false);
                player.setSleepingIgnored(false);
                player.setCollidable(true);
                player.setSilent(false);
                player.setCanPickupItems(true);
                getUserdata().removeTask(player, "vanish");
                getVanished().remove(player);
                getInstance().getOnlinePlayers().forEach(players -> players.showPlayer(getInstance(), player));
                if (!getVanished().isEmpty()) {
                    getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
                }
                getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("disable")));
                return true;
            } else return false;
        }
    }
    /**
     * disable this is for onDisable
     * @since many moons ago
     */
    public void disable() {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(player -> getUserdata().removeTask(player, "vanish"));
            getVanished().clear();
        }
    }
    /**
     * get vanished
     * @return list player
     * @since many moons ago
     */
    public List<Player> getVanished() {
        return vanished;
    }
}