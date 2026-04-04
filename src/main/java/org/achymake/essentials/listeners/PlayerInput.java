package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerInput implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerInput() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInput(PlayerInputEvent event) {
        var player = event.getPlayer();
        if (getConfig().getBoolean("double-jump.enable") || player.hasPermission("essentials.event.double_jump")) {
            if (!event.getInput().isJump())return;
            if (player.isOnGround()) {
                player.addScoreboardTag("double_jump");
            } else if (player.getScoreboardTags().contains("double_jump")) {
                if (player.isSprinting()) {
                    player.setVelocity(player.getLocation().getDirection().multiply(getConfig().getDouble("double-jump.sprinting.multiply")).setY(getConfig().getDouble("double-jump.sprinting.y")));
                } else if (player.getCurrentInput().isForward()) {
                    player.setVelocity(player.getLocation().getDirection().multiply(getConfig().getDouble("double-jump.forward.multiply")).setY(getConfig().getDouble("double-jump.forward.y")));
                } else if (player.getCurrentInput().isBackward()) {
                    player.setVelocity(player.getLocation().getDirection().multiply(getConfig().getDouble("double-jump.backward.multiply")).setY(getConfig().getDouble("double-jump.backward.y")));
                } else player.setVelocity(player.getLocation().getDirection().multiply(getConfig().getDouble("double-jump.standing.multiply")).setY(getConfig().getDouble("double-jump.standing.y")));
                player.removeScoreboardTag("double_jump");
                if (getConfig().getBoolean("double-jump.particle.enable")) {
                    var particleType = getConfig().getString("double-jump.particle.type");
                    if (particleType == null)return;
                    var count =  getConfig().getInt("double-jump.particle.count");
                    var offsetX = getConfig().getDouble("double-jump.particle.offsetX");
                    var offsetY = getConfig().getDouble("double-jump.particle.offsetY");
                    var offsetZ = getConfig().getDouble("double-jump.particle.offsetZ");
                    var location = player.getLocation();
                    getWorldHandler().spawnParticle(location, particleType, count, offsetX, offsetY, offsetZ);
                }
                if (getConfig().getBoolean("double-jump.sound.enable")) {
                    var soundType = getConfig().getString("double-jump.sound.type");
                    if (soundType == null)return;
                    var volume = getConfig().getDouble("double-jump.sound.volume");
                    var pitch = getConfig().getDouble("double-jump.sound.pitch");
                    getWorldHandler().playSound(player.getLocation(), soundType, volume, pitch);
                }
            }
        }
    }
}