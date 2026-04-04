package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.PluginManager;

public class EntityPlace implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityPlace() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPlace(EntityPlaceEvent event) {
        var entityType = event.getEntityType();
        if (!getEntityHandler().isCreatureSpawnDisabled(entityType)) {
            var player = event.getPlayer();
            if (getEntityHandler().isOverChunkLimit(event.getEntity())) {
                event.setCancelled(true);
                if (player != null) {
                    getMessage().sendActionBar(player, getMessage().get("events.entity-place", getMessage().toTitleCase(entityType.toString()), String.valueOf(getEntityHandler().getChunkLimit(entityType))));
                }
            } else if (player != null) {
                if (!getUserdata().isDisabled(player))return;
                event.setCancelled(true);
            }
        } else event.setCancelled(true);
    }
}