package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.PluginManager;

public class EntityTarget implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
    public EntityTarget() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        var target = event.getTarget();
        if (target == null)return;
        var entity = event.getEntity();
        if (entity instanceof Player)return;
        if (getEntityHandler().isEntityTargetDisabled(event.getEntityType(), target.getType())) {
            event.setCancelled(true);
            event.setTarget(null);
        } else if (target instanceof Player player) {
            if (!getUserdata().isVanished(player))return;
            event.setCancelled(true);
            event.setTarget(null);
        }
    }
}