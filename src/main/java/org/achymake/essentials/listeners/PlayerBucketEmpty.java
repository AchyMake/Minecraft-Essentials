package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerBucketEmpty implements Listener {
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
    public PlayerBucketEmpty() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        var player = event.getPlayer();
        var material = event.getBucket();
        if (!getUserdata().isDisabled(player)) {
            if (!getConfig().getBoolean("notification.enable"))return;
            if (!getConfig().getStringList("notification.bucket-empty").contains(material.toString()))return;
            var name = player.getName();
            var block = event.getBlock();
            var worldName = block.getWorld().getName();
            var x = String.valueOf(block.getX());
            var y = String.valueOf(block.getY());
            var z = String.valueOf(block.getZ());
            getConfig().getStringList("notification.message").forEach(messages -> getMessage().sendAll(messages.replaceAll("%player%", name)
                        .replaceAll("%material%", getMessage().toTitleCase(material.toString()))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%x%", x)
                        .replaceAll("%y%", y)
                        .replaceAll("%z%", z), "essentials.event.bucket_empty.notify"));
        } else event.setCancelled(true);
    }
}