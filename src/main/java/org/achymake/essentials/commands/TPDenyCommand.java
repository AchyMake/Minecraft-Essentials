package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TPDenyCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    public TPDenyCommand() {
        getInstance().getCommand("tpdeny").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasTaskID(player, "tpa")) {
                    if (getUserdata().getTpaFrom(player) != null) {
                        var target = getUserdata().getTpaFrom(player);
                        if (target != null) {
                            var tpaTask = getUserdata().getTaskID(target, "tpa");
                            if (getScheduleHandler().isQueued(tpaTask)) {
                                getScheduleHandler().cancel(tpaTask);
                                getUserdata().setTpaSent(target, null);
                                getUserdata().removeTask(target, "tpa");
                                getUserdata().setTpaFrom(player, null);
                                getUserdata().removeTask(player, "tpa");
                                target.sendMessage(getMessage().get("commands.tpdeny.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.tpdeny.sender", target.getName()));
                            }
                        }
                    }
                } else if (getUserdata().hasTaskID(player, "tpahere")) {
                    if (getUserdata().getTpaHereFrom(player) != null) {
                        var target = getUserdata().getTpaHereFrom(player);
                        if (target != null) {
                            var tpaHereTask = getUserdata().getTaskID(target, "tpahere");
                            if (getScheduleHandler().isQueued(tpaHereTask)) {
                                getScheduleHandler().cancel(tpaHereTask);
                                getUserdata().setTpaHereSent(target, null);
                                getUserdata().removeTask(target, "tpahere");
                                getUserdata().setTpaHereFrom(player, null);
                                getUserdata().removeTask(player, "tpahere");
                                target.sendMessage(getMessage().get("commands.tpdeny.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.tpdeny.sender", target.getName()));
                            }
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.tpdeny.invalid"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}