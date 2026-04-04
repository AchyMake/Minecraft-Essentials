package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.handlers.RandomHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerDeath implements Listener {
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
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerDeath() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        var location = player.getLocation();
        getUserdata().setLocation(player, location, "death");
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            if (!getRandomHandler().isTrue(getConfig().getDouble("deaths.drop-player-head.chance")))return;
            event.getDrops().add(getMaterialHandler().getPlayerHead(player, 1));
        }
        if (getConfig().getBoolean("deaths.drop-economy.enable")) {
            var min = getConfig().getDouble("deaths.drop-economy.min");
            var max = getConfig().getDouble("deaths.drop-economy.max");
            var lost = getRandomHandler().nextDouble(min, max);
            if (!getEconomyHandler().has(player, lost))return;
            getEconomyHandler().remove(player, lost);
            player.sendMessage(getMessage().get("events.death", getEconomyHandler().currency() + getEconomyHandler().format(lost), event.getDeathMessage().replace(player.getName(), "you")));
        }
        if (player.hasPermission("essentials.event.death.keep_exp")) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
        if (player.hasPermission("essentials.event.death.keep_inventory")) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }
}