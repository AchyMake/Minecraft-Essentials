package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerRespawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerRespawn() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH))return;
        var player = event.getPlayer();
        if (getConfig().getBoolean("deaths.send-location") || player.hasPermission("essentials.event.death.location")) {
            var location = getUserdata().getLocation(player, "death");
            if (location == null)return;
            var world = location.getWorld().getName();
            var x = String.valueOf(location.getBlockX());
            var y = String.valueOf(location.getBlockY());
            var z = String.valueOf(location.getBlockZ());
            player.sendMessage(getMessage().get("events.respawn.title"));
            player.sendMessage(getMessage().get("events.respawn.location", world, x, y, z));
        }
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        var home = getUserdata().getLocation(player, "home");
        if (home != null) {
            event.setRespawnLocation(home);
        } else if (getSpawn().getLocation() != null) {
            event.setRespawnLocation(getSpawn().getLocation());
        }
    }
}