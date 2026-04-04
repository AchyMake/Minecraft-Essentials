package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Warps;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public WarpCommand() {
        getInstance().getCommand("warp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!getWarps().getListed().isEmpty()) {
                    player.sendMessage(getMessage().get("commands.warp.title"));
                    getWarps().getListed().forEach(warps -> {
                        if (player.hasPermission("essentials.command.warp." + warps)) {
                            player.sendMessage(getMessage().get("commands.warp.listed", warps));
                        }
                    });
                } else player.sendMessage(getMessage().get("commands.warp.empty"));
                return true;
            } else if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (player.hasPermission("essentials.command.warp." + warpName)) {
                    var warp = getWarps().getLocation(warpName);
                    if (warp != null) {
                        getWorldHandler().teleport(player, warp, warpName, getWorldHandler().getTeleportDelay());
                    } else player.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.warp.other")) {
                    var warpName = args[0].toLowerCase();
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        var warp = getWarps().getLocation(warpName);
                        if (warp != null) {
                            if (target == player) {
                                getWorldHandler().teleport(target, warp, warpName, 0);
                            } else if (!target.hasPermission("essentials.command.warp.exempt")) {
                                getWorldHandler().teleport(target, warp, warpName, 0);
                                player.sendMessage(getMessage().get("commands.warp.sender", target.getName(), warpName));
                            } else player.sendMessage(getMessage().get("commands.warp.exempt", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var warpName = args[0].toLowerCase();
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    var warp = getWarps().getLocation(warpName);
                    if (warp != null) {
                        getWorldHandler().teleport(target, warp, warpName, 0);
                        consoleCommandSender.sendMessage(getMessage().get("commands.warp.sender", target.getName(), warpName));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.warp.invalid", warpName));
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
                getWarps().getListed().forEach(warps -> {
                    if (player.hasPermission("essentials.command.warp." + warps)) {
                        if (warps.startsWith(args[0])) {
                            commands.add(warps);
                        }
                    }
                });
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.warp.other")) {
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