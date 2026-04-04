package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public BackCommand() {
        getInstance().getCommand("back").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var recent = getUserdata().getLocation(player, "recent");
                if (recent != null) {
                    var worldName = recent.getWorld().getName().toLowerCase();
                    if (player.hasPermission("essentials.command.back.world." + worldName)) {
                        getWorldHandler().teleport(player, recent, "recent", getWorldHandler().getTeleportDelay());
                        return true;
                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("death")) {
                    if (player.hasPermission("essentials.command.back.death")) {
                        var death = getUserdata().getLocation(player, "death");
                        if (death != null) {
                            getWorldHandler().teleport(player, death, "death", getWorldHandler().getTeleportDelay());
                        }
                        return true;
                    }
                } else if (player.hasPermission("essentials.command.back.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            var recent = getUserdata().getLocation(target, "recent");
                            if (recent != null) {
                                getWorldHandler().teleport(target, recent, "recent", 0);
                            }
                        } else if (!target.hasPermission("essentials.command.back.exempt")) {
                            var recent = getUserdata().getLocation(target, "recent");
                            if (recent != null) {
                                getWorldHandler().teleport(target, recent, "recent", 0);
                            }
                        } else player.sendMessage(getMessage().get("commands.back.exempt"), target.getName());
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("death")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            var death = getUserdata().getLocation(target, "death");
                            if (death != null) {
                                getWorldHandler().teleport(target, death, "death", 0);
                            }
                        } else if (!target.hasPermission("essentials.command.back.exempt")) {
                            var death = getUserdata().getLocation(target, "death");
                            if (death != null) {
                                getWorldHandler().teleport(target, death, "death", 0);
                            }
                        } else player.sendMessage(getMessage().get("commands.back.exempt"), target.getName());
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var recent = getUserdata().getLocation(target, "recent");
                    if (recent != null) {
                        getWorldHandler().teleport(target, recent, "recent", 0);
                    }
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("death")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        var death = getUserdata().getLocation(target, "death");
                        if (death != null) {
                            getWorldHandler().teleport(target, death, "death", 0);
                        }
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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
                if (player.hasPermission("essentials.command.back.death")) {
                    commands.add("death");
                }
                if (player.hasPermission("essentials.command.back.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            } else if (args.length == 2) {
                if (!args[0].equalsIgnoreCase("death")) {
                    commands.add("death");
                }
            }
        }
        return commands;
    }
}