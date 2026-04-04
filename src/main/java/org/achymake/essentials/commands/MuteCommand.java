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

public class MuteCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public MuteCommand() {
        getInstance().getCommand("mute").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target == player) {
                        if (getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target))) {
                            if (getUserdata().isMuted(target)) {
                                player.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                    } else if (!target.hasPermission("essentials.command.mute.exempt")) {
                        if (getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target))) {
                            if (getUserdata().isMuted(target)) {
                                player.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                    } else player.sendMessage(getMessage().get("commands.mute.exempt", target.getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getUserdata().setBoolean(offlinePlayer, "settings.muted", !getUserdata().isMuted(offlinePlayer))) {
                            if (getUserdata().isMuted(offlinePlayer)) {
                                player.sendMessage(getMessage().get("commands.mute.enable", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("commands.mute.disable", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getUserdata().setBoolean(target, "settings.muted", !getUserdata().isMuted(target))) {
                        if (getUserdata().isMuted(target)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getUserdata().setBoolean(offlinePlayer, "settings.muted", !getUserdata().isMuted(offlinePlayer))) {
                            if (getUserdata().isMuted(offlinePlayer)) {
                                consoleCommandSender.sendMessage(getMessage().get("commands.mute.enable", offlinePlayer.getName()));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.mute.disable", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}