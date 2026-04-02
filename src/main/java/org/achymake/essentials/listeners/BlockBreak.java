package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class BlockBreak implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockBreak() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())return;
        var player = event.getPlayer();
        var block = event.getBlock();
        var material = block.getType();
        if (!getUserdata().isDisabled(player)) {
            if (block.getState() instanceof CreatureSpawner creatureSpawner) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterialHandler().hasEnchantment(heldItem, "silk_touch"))return;
                if (!player.hasPermission("essentials.event.silk_touch.spawner"))return;
                var location = creatureSpawner.getLocation().add(0.5,0.3,0.5);
                var entityType = creatureSpawner.getSpawnedType();
                if (entityType != null) {
                    getWorldHandler().dropItemStack(location, getMaterialHandler().getSpawner(entityType.toString(), 1));
                } else getWorldHandler().dropItemStack(location, getMaterialHandler().getSpawner("null", 1));
                event.setExpToDrop(0);
            }
            if (getConfig().getBoolean("notification.enable")) {
                if (!getConfig().getStringList("notification.block-break").contains(material.toString()))return;
                var name = player.getName();
                var worldName = block.getWorld().getName();
                var x = String.valueOf(block.getX());
                var y = String.valueOf(block.getY());
                var z = String.valueOf(block.getZ());
                getConfig().getStringList("notification.message").forEach(messages -> getMessage().sendAll(messages.replaceAll("%player%", name)
                            .replaceAll("%material%", getMessage().toTitleCase(material.toString()))
                            .replaceAll("%world%", worldName)
                            .replaceAll("%x%", x)
                            .replaceAll("%y%", y)
                            .replaceAll("%z%", z), "essentials.event.block_break.notify"));
            }
        } else event.setCancelled(true);
    }
}