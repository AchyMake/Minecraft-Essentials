package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
    public PlayerSpawnLocation() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        var player = event.getPlayer();
        var quit = getUserdata().getLocation(player, "quit");
        if (quit != null) {
            event.setSpawnLocation(quit);
            getUserdata().setLocation(player, quit, "join");
        } else if (getSpawn().getLocation() != null) {
            event.setSpawnLocation(getSpawn().getLocation());
            getUserdata().setLocation(player, getSpawn().getLocation(), "join");
        } else getUserdata().setLocation(player, event.getSpawnLocation(), "join");
    }
}