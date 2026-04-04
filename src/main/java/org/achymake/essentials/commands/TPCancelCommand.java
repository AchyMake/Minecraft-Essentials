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

public class TPCancelCommand implements CommandExecutor, TabCompleter {
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
    public TPCancelCommand() {
        getInstance().getCommand("tpcancel").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasTaskID(player, "tpa")) {
                    var target = getUserdata().getTpaSent(player);
                    if (target != null) {
                        var tpaTask = getUserdata().getTaskID(player, "tpa");
                        if (getScheduleHandler().isQueued(tpaTask)) {
                            getUserdata().setTpaFrom(target, null);
                            getUserdata().removeTask(target, "tpa");
                            getUserdata().setTpaSent(player, null);
                            getUserdata().removeTask(player, "tpa");
                            target.sendMessage(getMessage().get("commands.tpcancel.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpcancel.sender"));
                        }
                    }
                } else if (getUserdata().hasTaskID(player, "tpahere")) {
                    var target = getUserdata().getTpaHereSent(player);
                    if (target != null) {
                        var tpaHereTask = getUserdata().getTaskID(player, "tpahere");
                        if (getScheduleHandler().isQueued(tpaHereTask)) {
                            getUserdata().setTpaHereFrom(target, null);
                            getUserdata().removeTask(target, "tpahere");
                            getUserdata().setTpaHereSent(player, null);
                            getUserdata().removeTask(player, "tpahere");
                            target.sendMessage(getMessage().get("commands.tpcancel.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpcancel.sender"));
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.tpcancel.invalid"));
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