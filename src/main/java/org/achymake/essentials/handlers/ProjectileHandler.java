package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.entity.Projectile;

import java.util.HashMap;
import java.util.Map;

public class ProjectileHandler {
    private final Map<Projectile, Integer> projectiles = new HashMap<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    /**
     * add removal task
     * @param projectile projectile
     * @param seconds integer
     * @since many moons ago
     */
    public void addRemovalTask(Projectile projectile, int seconds) {
        int taskID = getScheduleHandler().runLater(() -> {
            if (projectile != null) {
                remove(projectile);
            }
        }, seconds * 20L).getTaskId();
        getProjectiles().put(projectile, taskID);
    }
    /**
     * remove projectile
     * @param projectile projectile
     * @since many moons ago
     */
    public void remove(Projectile projectile) {
        if (projectiles.containsKey(projectile)) {
            var taskID = projectiles.get(projectile);
            if (getScheduleHandler().isQueued(taskID)) {
                getScheduleHandler().cancel(taskID);
            }
            projectiles.remove(projectile);
            projectile.remove();
        } else projectile.remove();
    }
    /**
     * cancel removal task
     * @param projectile projectile
     * @since many moons ago
     */
    public void cancel(Projectile projectile) {
        if (projectiles.containsKey(projectile)) {
            var taskID = projectiles.get(projectile);
            if (getScheduleHandler().isQueued(taskID)) {
                getScheduleHandler().cancel(taskID);
            }
            projectiles.remove(projectile);
        }
    }
    /**
     * disable essentials projectile handler
     * @since many moons ago
     */
    public void disable() {
        if (projectiles.isEmpty())return;
        projectiles.forEach((projectile, taskID) -> {
            if (getScheduleHandler().isQueued(taskID)) {
                getScheduleHandler().cancel(taskID);
            }
            if (projectile != null) {
                projectiles.remove(projectile);
            }
        });
        projectiles.clear();
    }
    /**
     * get projectiles entity and taskID
     * @return map projectile, integer
     * @since many moons ago
     */
    public Map<Projectile, Integer> getProjectiles() {
        return projectiles;
    }
}