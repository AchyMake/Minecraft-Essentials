package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.DateHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BanCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private DateHandler getDateHandler() {
        return getInstance().getDateHandler();
    }
    public BanCommand() {
        getInstance().getCommand("ban").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                var value = Integer.parseInt(args[1]);
                if (value > 0) {
                    if (isDates(args[2])) {
                        if (target != null) {
                            if (target == player) {
                                if (args[2].equalsIgnoreCase("d")) {
                                    getUserdata().setLong(target, "settings.ban-expire",  getDateHandler().addDays(value));
                                } else if (args[2].equalsIgnoreCase("m")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (args[2].equalsIgnoreCase("y")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                }
                                getUserdata().setBoolean(target, "settings.banned", true);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], "none"));
                                target.kickPlayer(getUserdata().getBanReason(target));
                            } else if (target.hasPermission("essentials.command.ban.exempt")) {
                                player.sendMessage(getMessage().get("commands.ban.exempt", target.getName()));
                            } else if (!getUserdata().isBanned(target)) {
                                if (args[2].equalsIgnoreCase("d")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addDays(value));
                                } else if (args[2].equalsIgnoreCase("m")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (args[2].equalsIgnoreCase("y")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                }
                                getUserdata().setBoolean(target, "settings.banned", true);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], "none"));
                                target.kickPlayer(getUserdata().getBanReason(target));
                            } else player.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (!getUserdata().isBanned(offlinePlayer)) {
                                    if (args[2].equalsIgnoreCase("d")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (args[2].equalsIgnoreCase("m")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (args[2].equalsIgnoreCase("y")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    getUserdata().setBoolean(offlinePlayer, "settings.banned", true);
                                    player.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), args[2], "none"));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
                }
            } else if (args.length > 3) {
                var target = getInstance().getPlayer(args[0]);
                var value = Integer.parseInt(args[1]);
                if (value > 0) {
                    if (isDates(args[2])) {
                        var reason = getMessage().toString(args, 3);
                        if (target != null) {
                            if (target == player) {
                                if (args[2].equalsIgnoreCase("d")) {
                                    getUserdata().setLong(target, "settings.ban-expire",  getDateHandler().addDays(value));
                                } else if (args[2].equalsIgnoreCase("m")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (args[2].equalsIgnoreCase("y")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                }
                                getUserdata().setBoolean(target, "settings.banned", true);
                                getUserdata().setString(target, "settings.ban-reason", reason);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], reason));
                                target.kickPlayer(getUserdata().getBanReason(target));
                            } else if (!target.hasPermission("essentials.command.ban.exempt")) {
                                if (!getUserdata().isBanned(target)) {
                                    if (args[2].equalsIgnoreCase("d")) {
                                        getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (args[2].equalsIgnoreCase("m")) {
                                        getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (args[2].equalsIgnoreCase("y")) {
                                        getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    getUserdata().setBoolean(target, "settings.banned", true);
                                    getUserdata().setString(target, "settings.ban-reason", reason);
                                    player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], reason));
                                    target.kickPlayer(getUserdata().getBanReason(target));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.ban.exempt", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (!getUserdata().isBanned(offlinePlayer)) {
                                    if (args[2].equalsIgnoreCase("d")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (args[2].equalsIgnoreCase("m")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (args[2].equalsIgnoreCase("y")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    getUserdata().setBoolean(offlinePlayer, "settings.banned", true);
                                    getUserdata().setString(offlinePlayer, "settings.ban-reason", reason);
                                    player.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), args[2], reason));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                var value = Integer.parseInt(args[1]);
                if (value > 0) {
                    if (isDates(args[2])) {
                        if (target != null) {
                            if (!getUserdata().isBanned(target)) {
                                if (args[2].equalsIgnoreCase("d")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addDays(value));
                                } else if (args[2].equalsIgnoreCase("m")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (args[2].equalsIgnoreCase("y")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                }
                                getUserdata().setBoolean(target, "settings.banned", true);
                                consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], "none"));
                                target.kickPlayer(getUserdata().getBanReason(target));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (!getUserdata().isBanned(offlinePlayer)) {
                                    if (args[2].equalsIgnoreCase("d")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (args[2].equalsIgnoreCase("m")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (args[2].equalsIgnoreCase("y")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    getUserdata().setBoolean(offlinePlayer, "settings.banned", true);
                                    consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), args[2], "none"));
                                } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                            }
                        }
                        return true;
                    }
                }
            } else if (args.length > 3) {
                var target = getInstance().getPlayer(args[0]);
                var value = Integer.parseInt(args[1]);
                if (value > 0) {
                    if (isDates(args[2])) {
                        var reason = getMessage().toString(args, 3);
                        if (target != null) {
                            if (!getUserdata().isBanned(target)) {
                                if (args[2].equalsIgnoreCase("d")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addDays(value));
                                } else if (args[2].equalsIgnoreCase("m")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (args[2].equalsIgnoreCase("y")) {
                                    getUserdata().setLong(target, "settings.ban-expire", getDateHandler().addYears(value));
                                }
                                getUserdata().setBoolean(target, "settings.banned", true);
                                getUserdata().setString(target, "settings.ban-reason", reason);
                                consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), args[2], reason));
                                target.kickPlayer(getUserdata().getBanReason(target));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (!getUserdata().isBanned(offlinePlayer)) {
                                    if (args[2].equalsIgnoreCase("d")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (args[2].equalsIgnoreCase("m")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (args[2].equalsIgnoreCase("y")) {
                                        getUserdata().setLong(offlinePlayer, "settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    getUserdata().setBoolean(offlinePlayer, "settings.banned", true);
                                    getUserdata().setString(offlinePlayer, "settings.ban-reason", reason);
                                    consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), args[2], reason));
                                } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
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
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                commands.add("1");
                commands.add("2");
                commands.add("3");
                commands.add("4");
                commands.add("5");
                commands.add("6");
                commands.add("7");
            } else if (args.length == 3) {
                commands.add("d");
                commands.add("m");
                commands.add("y");
            }
        }
        return commands;
    }
    private boolean isDates(String date) {
        if (date.equalsIgnoreCase("d")) {
            return true;
        } else if (date.equalsIgnoreCase("m")) {
            return true;
        } else return date.equalsIgnoreCase("y");
    }
}