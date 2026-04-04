package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;

public class PlayerInteract implements Listener {
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
    public PlayerInteract() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)return;
        var player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand() != EquipmentSlot.HAND)return;
            if (!getUserdata().isDisabled(player))return;
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
        } else if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType() == getMaterialHandler().get("farmland")) {
                if (!getConfig().getBoolean("crops.disable-tramping-farmland"))return;
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            } else if (event.getClickedBlock().getType() == getMaterialHandler().get("turtle_egg")) {
                if (!getConfig().getBoolean("eggs.disable-tramping-turtle-egg"))return;
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            } else if (event.getClickedBlock().getType() == getMaterialHandler().get("sniffer_egg")) {
                if (!getConfig().getBoolean("eggs.disable-tramping-sniffer-egg"))return;
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            } else if (getUserdata().isDisabled(player) || getUserdata().isVanished(player)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        }
    }
}