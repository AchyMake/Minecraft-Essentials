package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public TimeCommand() {
        getInstance().getCommand("time").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    getUserdata().resetTime(player);
                    player.sendMessage(getMessage().get("commands.time.reset"));
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("morning")) {
                        getUserdata().setMorning(player);
                        player.sendMessage(getMessage().get("commands.time.set", getMessage().get("time.morning")));
                    } else if (args[1].equalsIgnoreCase("day")) {
                        getUserdata().setDay(player);
                        player.sendMessage(getMessage().get("commands.time.set", getMessage().get("time.day")));
                    } else if (args[1].equalsIgnoreCase("noon")) {
                        getUserdata().setNoon(player);
                        player.sendMessage(getMessage().get("commands.time.set", getMessage().get("time.noon")));
                    } else if (args[1].equalsIgnoreCase("night")) {
                        getUserdata().setNight(player);
                        player.sendMessage(getMessage().get("commands.time.set", getMessage().get("time.night")));
                    } else if (args[1].equalsIgnoreCase("midnight")) {
                        getUserdata().setMidnight(player);
                        player.sendMessage(getMessage().get("commands.time.set", getMessage().get("time.midnight")));
                    } else {
                        getUserdata().setTime(player, Long.parseLong(args[1]));
                        player.sendMessage(getMessage().get("commands.time.set", args[1]));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("add")) {
                    getUserdata().addTime(player, Long.parseLong(args[1]));
                    player.sendMessage(getMessage().get("commands.time.add",  args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    getUserdata().removeTime(player, Long.parseLong(args[1]));
                    player.sendMessage(getMessage().get("commands.time.remove", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (player.hasPermission("essentials.command.time.other")) {
                        var target = getInstance().getPlayer(args[1]);
                        if (target != null) {
                            getUserdata().resetTime(player);
                            player.sendMessage(getMessage().get("commands.time.other.reset", target.getName()));
                        } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                    }
                    return true;
                }
            } else if (args.length == 3) {
                if (player.hasPermission("essentials.command.time.other")) {
                    var target = getInstance().getPlayer(args[2]);
                    if (target != null) {
                        if (target == player) {
                            if (args[0].equalsIgnoreCase("set")) {
                                if (args[1].equalsIgnoreCase("morning")) {
                                    getUserdata().setMorning(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.morning")));
                                } else if (args[1].equalsIgnoreCase("day")) {
                                    getUserdata().setDay(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.day")));
                                } else if (args[1].equalsIgnoreCase("noon")) {
                                    getUserdata().setNoon(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.noon")));
                                } else if (args[1].equalsIgnoreCase("night")) {
                                    getUserdata().setNight(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.night")));
                                } else if (args[1].equalsIgnoreCase("midnight")) {
                                    getUserdata().setMidnight(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.midnight")));
                                } else {
                                    getUserdata().setTime(target, Long.parseLong(args[1]));
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), args[1]));
                                }
                                return true;
                            } else if (args[0].equalsIgnoreCase("add")) {
                                getUserdata().addTime(target, Long.parseLong(args[1]));
                                player.sendMessage(getMessage().get("commands.time.other.add", target.getName(), args[1]));
                                return true;
                            } else if (args[0].equalsIgnoreCase("remove")) {
                                getUserdata().removeTime(target, Long.parseLong(args[1]));
                                player.sendMessage(getMessage().get("commands.time.other.remove", target.getName(), args[1]));
                                return true;
                            }
                        } else if (!target.hasPermission("essentials.command.time.exempt")) {
                            if (args[0].equalsIgnoreCase("set")) {
                                if (args[1].equalsIgnoreCase("morning")) {
                                    getUserdata().setMorning(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.morning")));
                                } else if (args[1].equalsIgnoreCase("day")) {
                                    getUserdata().setDay(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.day")));
                                } else if (args[1].equalsIgnoreCase("noon")) {
                                    getUserdata().setNoon(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.noon")));
                                } else if (args[1].equalsIgnoreCase("night")) {
                                    getUserdata().setNight(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.night")));
                                } else if (args[1].equalsIgnoreCase("midnight")) {
                                    getUserdata().setMidnight(target);
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), getMessage().get("time.midnight")));
                                } else {
                                    getUserdata().setTime(target, Long.parseLong(args[1]));
                                    player.sendMessage(getMessage().get("commands.time.other.set", target.getName(), args[1]));
                                }
                                return true;
                            } else if (args[0].equalsIgnoreCase("add")) {
                                getUserdata().addTime(target, Long.parseLong(args[1]));
                                player.sendMessage(getMessage().get("commands.time.other.add", target.getName(), args[1]));
                                return true;
                            } else if (args[0].equalsIgnoreCase("remove")) {
                                getUserdata().removeTime(target, Long.parseLong(args[1]));
                                player.sendMessage(getMessage().get("commands.time.other.remove", target.getName(), args[1]));
                                return true;
                            }
                        } else player.sendMessage(getMessage().get("commands.time.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[2]));
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("set");
                commands.add("remove");
                commands.add("reset");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add") ||
                        args[0].equalsIgnoreCase("remove")) {
                    commands.add("10");
                    commands.add("100");
                    commands.add("1000");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[1])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                } else if (args[0].equalsIgnoreCase("set")) {
                    commands.add("day");
                    commands.add("night");
                    commands.add("noon");
                    commands.add("midnight");
                    commands.add("morning");
                }
            } else if (args.length == 3) {
                if (player.hasPermission("essentials.command.time.other")) {
                    if (args[0].equalsIgnoreCase("set") ||
                            args[0].equalsIgnoreCase("add") ||
                            args[0].equalsIgnoreCase("remove")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[2])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                }
            }
        }
        return commands;
    }
}