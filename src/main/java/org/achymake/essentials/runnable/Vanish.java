package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.entity.Player;

public record Vanish(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    @Override
    public void run() {
        if (getVanishHandler().isVanish(getPlayer())) {
            getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", getMessage().get("enable")));
        } else getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", getMessage().get("disable")));
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}