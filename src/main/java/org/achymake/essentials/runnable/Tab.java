package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.TablistHandler;
import org.bukkit.entity.Player;

public record Tab(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TablistHandler getTablistHandler() {
        return getInstance().getTablistHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String getHeader() {
        var world = getPlayer().getWorld();
        var worldName = world.getName();
        if (!getTablistHandler().hasHeaderLines(worldName)) {
            if (!getTablistHandler().hasHeaderLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines(worldName)));
    }
    private String getName() {
        var world = getPlayer().getWorld();
        var worldName = world.getName();
        if (!getTablistHandler().hasName(worldName)) {
            if (!getTablistHandler().hasName()) {
                return getPlayer().getName();
            } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName());
        } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName(worldName));
    }
    private String getFooter() {
        var world = getPlayer().getWorld();
        var worldName = world.getName();
        if (!getTablistHandler().hasFooterLines(worldName)) {
            if (!getTablistHandler().hasFooterLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines(worldName)));
    }
    private void setHeader(String value) {
        var header = getPlayer().getPlayerListHeader();
        if (header != null) {
            if (value == null)return;
            if (header.equals(value))return;
            getPlayer().setPlayerListHeader(value);
        } else getPlayer().setPlayerListHeader(value);
    }
    private void setName(String value) {
        if (getPlayer().getPlayerListName().equals(value))return;
        getPlayer().setPlayerListName(value);
    }
    private void setFooter(String value) {
        var footer = getPlayer().getPlayerListFooter();
        if (footer != null) {
            if (value == null)return;
            if (footer.equals(value))return;
            getPlayer().setPlayerListFooter(value);
        } else getPlayer().setPlayerListFooter(value);
    }
    private void setOrder(int order) {
        if (getPlayer().getPlayerListOrder() == order)return;
        getPlayer().setPlayerListOrder(order);
    }
    @Override
    public void run() {
        setHeader(getHeader());
        setName(getName());
        setFooter(getFooter());
        setOrder(getTablistHandler().getWeight(getPlayer()));
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}