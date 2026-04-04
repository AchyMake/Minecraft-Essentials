package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

public class AsyncPlayerChat implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public AsyncPlayerChat() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        var username = getMessage().addPlaceholder(player, getUserdata().getChatFormat(player, false));
        var message = getMessage().censor(event.getMessage().replace("%", "%%"));
        var colored = getMessage().addColor(message);
        if (!getUserdata().isMuted(player)) {
            if (!getUserdata().isVanished(player)) {
                if (getMessage().isURL(event.getMessage())) {
                    if (player.hasPermission("essentials.event.chat.url")) {
                        if (player.hasPermission("essentials.event.chat.color")) {
                            event.setFormat(username + colored);
                        } else event.setFormat(username + message);
                    } else event.setCancelled(true);
                } else if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(username + colored);
                } else event.setFormat(username + message);
            } else {
                event.setCancelled(true);
                var vanish = getMessage().addPlaceholder(player, getUserdata().getChatFormat(player, true));
                getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(vanish + colored);
                    } else vanished.sendMessage(vanish + message);
                });
            }
        } else event.setCancelled(true);
    }
}