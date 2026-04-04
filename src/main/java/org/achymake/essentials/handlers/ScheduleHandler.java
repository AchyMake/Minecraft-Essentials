package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private BukkitScheduler getBukkitScheduler() {
        return getInstance().getBukkitScheduler();
    }
    /**
     * run task timer
     * @param runnable runnable
     * @param tick tick
     * @param period long
     * @return bukkitTask
     * @since many moons ago
     */
    public BukkitTask runTimer(Runnable runnable, long tick, long period) {
        return getBukkitScheduler().runTaskTimer(getInstance(), runnable, tick, period);
    }
    /**
     * run task later
     * @param runnable runnable
     * @param tick tick
     * @return bukkitTask
     * @since many moons ago
     */
    public BukkitTask runLater(Runnable runnable, long tick) {
        return getBukkitScheduler().runTaskLater(getInstance(), runnable, tick);
    }
    /**
     * run asynchronously task
     * @param runnable runnable
     * @since many moons ago
     */
    public void runAsynchronously(Runnable runnable) {
        getBukkitScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
    /**
     * is taskID queued
     * @param taskID integer
     * @since many moons ago
     */
    public boolean isQueued(int taskID) {
        return getBukkitScheduler().isQueued(taskID);
    }
    /**
     * cancel taskID if taskID is queued
     * @param taskID integer
     * @since many moons ago
     */
    public void cancel(int taskID) {
        if (isQueued(taskID)) {
            getBukkitScheduler().cancelTask(taskID);
        }
    }
    /**
     * disable essentials schedule handler
     * @since many moons ago
     */
    public void disable() {
        getBukkitScheduler().cancelTasks(getInstance());
    }
}