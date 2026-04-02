package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worth;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class InventoryClose implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public InventoryClose() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        var player = (Player) event.getPlayer();
        var inventories = getInstance().getInventoryHandler().getInventories();
        if (!inventories.containsKey(player))return;
        if (event.getInventory() != inventories.get(player))return;
        var listed = new HashMap<Material, Integer>();
        inventories.remove(player);
        event.getInventory().forEach(itemStack -> {
            if (itemStack == null)return;
            var material = itemStack.getType();
            if (getWorth().isListed(material)) {
                var amount = itemStack.getAmount();
                var worth = getWorth().get(material);
                var result = worth * amount;
                if (getEconomyHandler().add(player, result)) {
                    if (listed.containsKey(material)) {
                        listed.replace(material, listed.get(material) + amount);
                    } else listed.put(material, amount);
                    itemStack.setAmount(0);
                } else getMaterialHandler().giveItemStack(player, itemStack);
            } else getMaterialHandler().giveItemStack(player, itemStack);
        });
        listed.keySet().forEach(material -> {
            var amount = listed.get(material);
            if (getWorth().isListed(material)) {
                var result = getWorth().get(material) * amount;
                player.sendMessage(getMessage().get("commands.sell.sellable", String.valueOf(amount), getMessage().toTitleCase(material.name()), getEconomyHandler().currency() + getEconomyHandler().format(result)));
            }
        });
    }
}