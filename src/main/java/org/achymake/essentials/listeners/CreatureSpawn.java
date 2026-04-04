package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.PluginManager;

public class CreatureSpawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public CreatureSpawn() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        var spawnReason = event.getSpawnReason();
        var entityType = event.getEntityType();
        if (!getEntityHandler().isSpawnReasonDisabled(entityType, spawnReason)) {
            if (!getEntityHandler().isCreatureSpawnDisabled(entityType)) {
                var entity = event.getEntity();
                if (!getEntityHandler().isOverChunkLimit(entity)) {
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.CUSTOM))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.BREEDING))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.CURED))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.INFECTION))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.DROWNED))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.FROZEN))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.SHEARED))return;
                    if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.PIGLIN_ZOMBIFIED))return;
                    getEntityHandler().setEquipment(entity);
                } else event.setCancelled(true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}