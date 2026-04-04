package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerMove implements Listener {
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
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerMove() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        var to = event.getTo();
        if (to != null) {
            if (!hasMoved(event.getFrom(), to))return;
            var player = event.getPlayer();
            if (getUserdata().isFrozen(player)) {
                event.setCancelled(true);
            } else if (getUserdata().hasTaskID(player, "teleport")) {
                if (!getConfig().getBoolean("teleport.cancel-on-move"))return;
                getUserdata().removeTask(player, "teleport");
                player.sendMessage(getMessage().get("events.move"));
            }
        } else event.setCancelled(true);
    }
    private boolean hasMoved(Location from, Location to) {
        return from.getX() != to.getX() ||
                from.getY() != to.getY() ||
                from.getZ() != to.getZ();
    }
}