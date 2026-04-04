package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MOTDCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public MOTDCommand() {
        getInstance().getCommand("motd").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                if (getConfig().isList("message-of-the-day." + args[0])) {
                    getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day." + args[0]));
                } else player.sendMessage(getMessage().get("commands.motd.invalid", args[0]));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.motd.other")) {
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        if (target == player) {
                            if (getConfig().isList("message-of-the-day." + args[0])) {
                                getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + args[0]));
                            } else player.sendMessage(getMessage().get("commands.motd.invalid", args[0]));
                        } else if (!target.hasPermission("essentials.command.motd.exempt")) {
                            if (getConfig().isList("message-of-the-day." + args[0])) {
                                getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + args[0]));
                            } else player.sendMessage(getMessage().get("commands.motd.invalid", args[0]));
                        } else player.sendMessage(getMessage().get("commands.motd.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().sendStringList(consoleCommandSender, getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                if (getConfig().isList("message-of-the-day." + args[0])) {
                    getMessage().sendStringList(consoleCommandSender, getConfig().getStringList("message-of-the-day." + args[0]));
                } else consoleCommandSender.sendMessage(getMessage().get("commands.motd.invalid", args[0]));
                return true;
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    if (getConfig().isList("message-of-the-day." + args[0])) {
                        getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + args[0]));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.motd.invalid", args[0]));
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
                commands.addAll(getConfig().getConfigurationSection("message-of-the-day").getKeys(false));
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.motd.other")) {
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