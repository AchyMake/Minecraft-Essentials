package org.achymake.essentials.listeners;

import io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class AsyncPlayerSpawnLocation implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public AsyncPlayerSpawnLocation() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerSpawnLocation(AsyncPlayerSpawnLocationEvent event) {
        var uuid = event.getConnection().getProfile().getId();
        var offlinePlayer = getInstance().getOfflinePlayer(uuid);
        var quit = getUserdata().getLocation(offlinePlayer, "quit");
        if (quit != null) {
            event.setSpawnLocation(quit);
            getUserdata().setLocation(offlinePlayer, quit, "join");
        } else if (getSpawn().getLocation() != null) {
            event.setSpawnLocation(getSpawn().getLocation());
            getUserdata().setLocation(offlinePlayer, event.getSpawnLocation(), "join");
        } else getUserdata().setLocation(offlinePlayer, event.getSpawnLocation(), "join");
    }
}
