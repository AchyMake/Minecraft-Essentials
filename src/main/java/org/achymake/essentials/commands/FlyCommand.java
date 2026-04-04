package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public FlyCommand() {
        getInstance().getCommand("fly").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.setAllowFlight(!player.getAllowFlight());
                if (player.getAllowFlight()) {
                    getMessage().sendActionBar(player, getMessage().get("commands.fly.self", getMessage().get("enable")));
                } else getMessage().sendActionBar(player, getMessage().get("commands.fly.self", getMessage().get("disable")));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.fly.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            target.setAllowFlight(!target.getAllowFlight());
                            if (target.getAllowFlight()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("enable")));
                            } else getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("disable")));
                        } else if (!target.hasPermission("essentials.command.fly.exempt")) {
                            target.setAllowFlight(!target.getAllowFlight());
                            if (target.getAllowFlight()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("enable")));
                                player.sendMessage(getMessage().get("commands.fly.sender", target.getName(), getMessage().get("enable")));
                            } else {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("disable")));
                                player.sendMessage(getMessage().get("commands.fly.sender", target.getName(), getMessage().get("disable")));
                            }
                        } else player.sendMessage(getMessage().get("commands.fly.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("enable")));
                    } else getMessage().sendActionBar(target, getMessage().get("commands.fly.self", getMessage().get("disable")));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.fly.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
}