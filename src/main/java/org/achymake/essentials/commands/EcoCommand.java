package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public EcoCommand() {
        getInstance().getCommand("eco").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 2) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().set(offlinePlayer, getEconomyHandler().getStartingBalance())) {
                            player.sendMessage(getMessage().get("commands.eco.reset", offlinePlayer.getName(), getEconomyHandler().currency() + getEconomyHandler().format(getEconomyHandler().getStartingBalance())));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().add(offlinePlayer, value)) {
                            player.sendMessage(getMessage().get("commands.eco.add", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().has(offlinePlayer, value)) {
                            if (getEconomyHandler().remove(offlinePlayer, value)) {
                                player.sendMessage(getMessage().get("commands.eco.remove.success", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                        } else player.sendMessage(getMessage().get("commands.eco.remove.insufficient-funds", offlinePlayer.getName(), getEconomyHandler().currency() + getEconomyHandler().format(value)));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().set(offlinePlayer, value)) {
                            player.sendMessage(getMessage().get("commands.eco.set", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().set(offlinePlayer, getEconomyHandler().getStartingBalance())) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.eco.reset", offlinePlayer.getName(), getEconomyHandler().currency() + getEconomyHandler().format(getEconomyHandler().getStartingBalance())));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().add(offlinePlayer, value)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.eco.add", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().has(offlinePlayer, value)) {
                            if (getEconomyHandler().remove(offlinePlayer, value)) {
                                consoleCommandSender.sendMessage(getMessage().get("commands.eco.remove.success", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                            } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.eco.remove.insufficient-funds", offlinePlayer.getName(), getEconomyHandler().currency() + getEconomyHandler().format(value)));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getEconomyHandler().set(offlinePlayer, value)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.eco.set", getEconomyHandler().currency() + getEconomyHandler().format(value), offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("remove");
                commands.add("reset");
                commands.add("set");
            } else if (args.length == 2) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[1])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                    commands.add("100");
                    commands.add("500");
                    commands.add("1000");
                }
            }
        }
        return commands;
    }
}