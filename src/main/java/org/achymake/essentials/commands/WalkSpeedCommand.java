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

public class WalkSpeedCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public WalkSpeedCommand() {
        getInstance().getCommand("walkspeed").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getUserdata().setWalkSpeed(player, 0);
                player.sendMessage(getMessage().get("commands.walkspeed.changed", "Default"));
            } else if (args.length == 1) {
                var value = Float.parseFloat(args[0]);
                getUserdata().setWalkSpeed(player, value);
                player.sendMessage(getMessage().get("commands.walkspeed.changed", String.valueOf(value)));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.walkspeed.other")) {
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        var value = Float.parseFloat(args[0]);
                        if (target == player) {
                            getUserdata().setWalkSpeed(target, value);
                            player.sendMessage(getMessage().get("commands.walkspeed.sender", target.getName(), String.valueOf(value)));
                        } else if (!target.hasPermission("essentials.command.walkspeed.exempt")) {
                            getUserdata().setWalkSpeed(target, value);
                            player.sendMessage(getMessage().get("commands.walkspeed.sender", target.getName(), String.valueOf(value)));
                        } else player.sendMessage(getMessage().get("commands.walkspeed.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    var value = Float.parseFloat(args[0]);
                    getUserdata().setWalkSpeed(target, value);
                    consoleCommandSender.sendMessage(getMessage().get("commands.walkspeed.sender", target.getName(), String.valueOf(value)));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[1]));
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
                commands.add("1");
                commands.add("2");
                commands.add("4");
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.walkspeed.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[1])) {
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