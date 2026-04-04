package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.PluginManager;

public class BlockIgnite implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockIgnite() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        var cause = event.getCause();
        var ignitingEntity = event.getIgnitingEntity();
        var block = event.getIgnitingBlock();
        if (ignitingEntity instanceof Arrow) {
            if (!cause.equals(BlockIgniteEvent.IgniteCause.ARROW))return;
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (ignitingEntity instanceof EnderCrystal) {
            if (!cause.equals(BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL))return;
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (ignitingEntity instanceof Explosive) {
            if (!cause.equals(BlockIgniteEvent.IgniteCause.EXPLOSION))return;
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (ignitingEntity instanceof Fireball) {
            if (!cause.equals(BlockIgniteEvent.IgniteCause.FIREBALL))return;
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (ignitingEntity instanceof LightningStrike) {
            if (!cause.equals(BlockIgniteEvent.IgniteCause.LIGHTNING))return;
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (ignitingEntity instanceof Player player) {
            if (!getUserdata().isDisabled(player))return;
            event.setCancelled(true);
        } else if (block != null) {
            var material = block.getType();
            if (material.equals(getMaterialHandler().get("lava"))) {
                if (!cause.equals(BlockIgniteEvent.IgniteCause.LAVA))return;
                if (!getConfig().getBoolean("fire.disable-lava-fire-spread"))return;
                event.setCancelled(true);
            } else if (material.equals(getMaterialHandler().get("fire"))) {
                if (!cause.equals(BlockIgniteEvent.IgniteCause.SPREAD))return;
                if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
                event.setCancelled(true);
            }
        }
    }
}