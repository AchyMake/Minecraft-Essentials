package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Jail;
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

public class JailCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Jail getJail() {
        return getInstance().getJail();
    }
    public JailCommand() {
        getInstance().getCommand("jail").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var jail = getJail().getLocation();
                    if (jail != null) {
                        if (target == player) {
                            if (getUserdata().isJailed(target)) {
                                var recent = getUserdata().getLocation(target, "recent");
                                if (recent != null) {
                                    if (getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                        recent.getChunk().load();
                                        target.teleport(recent);
                                        player.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("disable")));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                }
                            } else {
                                jail.getChunk().load();
                                target.teleport(jail);
                                if (!getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                    target.teleport(getUserdata().getLocation(target, "recent"));
                                    player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                } else player.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("enable")));
                            }
                        } else if (!target.hasPermission("essentials.command.jail.exempt")) {
                            if (getUserdata().isJailed(target)) {
                                var recent = getUserdata().getLocation(target, "recent");
                                if (recent != null) {
                                    if (getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                        recent.getChunk().load();
                                        target.teleport(recent);
                                        player.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("disable")));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                }
                            } else {
                                jail.getChunk().load();
                                target.teleport(jail);
                                if (!getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                    target.teleport(getUserdata().getLocation(target, "recent"));
                                    player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                } else player.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("enable")));
                            }
                        } else player.sendMessage(getMessage().get("commands.jail.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.jail.invalid"));
                } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var jail = getJail().getLocation();
                    if (jail != null) {
                        if (getUserdata().isJailed(target)) {
                            var recent = getUserdata().getLocation(target, "recent");
                            if (recent != null) {
                                if (getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                    recent.getChunk().load();
                                    target.teleport(recent);
                                    consoleCommandSender.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("disable")));
                                } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            }
                        } else {
                            jail.getChunk().load();
                            target.teleport(jail);
                            if (!getUserdata().setBoolean(target, "settings.jailed", !getUserdata().isJailed(target))) {
                                target.teleport(getUserdata().getLocation(target, "recent"));
                                consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.jail.toggle", target.getName(), getMessage().get("enable")));
                        }
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.jail.invalid"));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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