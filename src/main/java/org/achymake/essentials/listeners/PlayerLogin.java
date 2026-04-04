package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.DateHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerLogin implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private DateHandler getDateHandler() {
        return getInstance().getDateHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerLogin() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        var server = getInstance().getServer();
        if (server.hasWhitelist()) {
            if (server.getWhitelistedPlayers().contains(player)) {
                if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
                    if (player.hasPermission("essentials.event.login.full_server")) {
                        if (getUserdata().exists(player)) {
                            if (getUserdata().isBanned(player)) {
                                if (!getInstance().getDateHandler().expired(getUserdata().getBanExpire(player))) {
                                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", getUserdata().getBanReason(player), getDateHandler().getFormatted(getUserdata().getBanExpire(player)))));
                                } else allow(event, player);
                            } else allow(event, player);
                        } else allow(event, player);
                    } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
                } else if (getUserdata().exists(player)) {
                    if (getUserdata().isBanned(player)) {
                        if (!getInstance().getDateHandler().expired(getUserdata().getBanExpire(player))) {
                            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", getUserdata().getBanReason(player), getDateHandler().getFormatted(getUserdata().getBanExpire(player)))));
                        } else allow(event, player);
                    } else allow(event, player);
                } else allow(event, player);
            } else event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, getConfig().getString("connection.login.whitelisted"));
        } else if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
            if (player.hasPermission("essentials.event.login.full_server")) {
                if (getUserdata().exists(player)) {
                    if (getUserdata().isBanned(player)) {
                        if (!getInstance().getDateHandler().expired(getUserdata().getBanExpire(player))) {
                            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", getUserdata().getBanReason(player), getDateHandler().getFormatted(getUserdata().getBanExpire(player)))));
                        } else allow(event, player);
                    } else allow(event, player);
                } else allow(event, player);
            } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
        } else if (getUserdata().exists(player)) {
            if (getUserdata().isBanned(player)) {
                if (!getInstance().getDateHandler().expired(getUserdata().getBanExpire(player))) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", getUserdata().getBanReason(player), getDateHandler().getFormatted(getUserdata().getBanExpire(player)))));
                } else allow(event, player);
            } else allow(event, player);
        } else allow(event, player);
    }
    private void allow(PlayerLoginEvent event, Player player) {
        event.allow();
        if (getUserdata().reload(player)) {
            if (getUserdata().isBanned(player)) {
                getUserdata().setBoolean(player, "settings.banned", false);
                getUserdata().setInt(player, "settings.ban-expire", 0);
                getUserdata().setString(player, "settings.ban-reason", null);
            }
        }
    }
}