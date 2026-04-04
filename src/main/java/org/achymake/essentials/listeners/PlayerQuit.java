package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerQuit implements Listener {
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
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerQuit() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isVanished(player)) {
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.quit.message")));
                playSound();
            } else if (player.hasPermission("essentials.event.quit.message")) {
                event.setQuitMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.quit.message")));
                playSound();
            } else {
                event.setQuitMessage(null);
                getMessage().sendAll(getMessage().get("events.quit.notify", player.getName()), "essentials.event.quit.notify");
            }
        } else {
            getVanishHandler().getVanished().remove(player);
            event.setQuitMessage(null);
        }
        getUserdata().disable(player);
    }
    private void playSound() {
        if (!getConfig().getBoolean("connection.quit.sound.enable"))return;
        var soundType = getConfig().getString("connection.quit.sound.type");
        var volume = (float) getConfig().getDouble("connection.quit.sound.volume");
        var pitch = (float) getConfig().getDouble("connection.quit.sound.pitch");
        getInstance().getOnlinePlayers().forEach(target -> getWorldHandler().playSound(target.getLocation(), soundType, volume, pitch));
    }
}