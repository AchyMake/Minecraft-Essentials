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

public class HomesCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public HomesCommand() {
        getInstance().getCommand("homes").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!getUserdata().getHomes(player).isEmpty()) {
                    player.sendMessage(getMessage().get("commands.homes.title"));
                    for (String listedHomes : getUserdata().getHomes(player)) {
                        player.sendMessage(getMessage().get("commands.homes.listed", listedHomes));
                    }
                } else player.sendMessage(getMessage().get("commands.homes.empty"));
                return true;
            } else if (args.length == 3) {
                var targetHome = args[2];
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("essentials.command.homes.delete")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getUserdata().isHome(offlinePlayer, targetHome)) {
                                getUserdata().setString(offlinePlayer, "homes." + targetHome, null);
                                player.sendMessage(getMessage().get("commands.homes.delete", targetHome, offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("commands.homes.invalid", offlinePlayer.getName(), targetHome));
                        } else player.sendMessage(getMessage().get("error.target.invalid", args[1]));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("essentials.command.homes.teleport")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (targetHome.equalsIgnoreCase("bed")) {
                                var location = offlinePlayer.getBedSpawnLocation();
                                if (location != null) {
                                    if (!location.getChunk().isLoaded()) {
                                        location.getChunk().load();
                                    }
                                    player.sendMessage(getMessage().get("commands.homes.teleport", targetHome, offlinePlayer.getName()));
                                    player.teleport(location);
                                } else player.sendMessage(getMessage().get("commands.homes.invalid", offlinePlayer.getName(), "bed"));
                            } else if (getUserdata().isHome(offlinePlayer, targetHome)) {
                                var location = getUserdata().getHome(offlinePlayer, targetHome);
                                if (location != null) {
                                    if (!location.getChunk().isLoaded()) {
                                        location.getChunk().load();
                                    }
                                    player.sendMessage(getMessage().get("commands.homes.teleport", targetHome, offlinePlayer.getName()));
                                    player.teleport(location);
                                }
                            } else player.sendMessage(getMessage().get("commands.homes.invalid", offlinePlayer.getName(), targetHome));
                        } else player.sendMessage(getMessage().get("error.target.invalid", args[1]));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.homes.delete")) {
                    commands.add("delete");
                }
                if (player.hasPermission("essentials.command.homes.teleport")) {
                    commands.add("teleport");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("essentials.command.homes.teleport")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("essentials.command.homes.delete")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!getUserdata().isVanished(target)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("essentials.command.homes.teleport")) {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().exists(offlinePlayer)) {
                            commands.addAll(getUserdata().getHomes(offlinePlayer));
                        }
                    }
                }
            }
        }
        return commands;
    }
}