package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

public class SignChange implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public SignChange() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isDisabled(player)) {
            if (!player.hasPermission("essentials.event.sign.color"))return;
            var line1 = event.getLine(0);
            if (line1 != null) {
                if (line1.contains("&")) {
                    event.setLine(0, getMessage().addColor(line1));
                }
            }
            var line2 = event.getLine(1);
            if (line2 != null) {
                if (line2.contains("&")) {
                    event.setLine(1, getMessage().addColor(line2));
                }
            }
            var line3 = event.getLine(2);
            if (line3 != null) {
                if (line3.contains("&")) {
                    event.setLine(2, getMessage().addColor(line3));
                }
            }
            var line4 = event.getLine(3);
            if (line4 != null) {
                if (line4.contains("&")) {
                    event.setLine(3, getMessage().addColor(line4));
                }
            }
        } else event.setCancelled(true);
    }
}