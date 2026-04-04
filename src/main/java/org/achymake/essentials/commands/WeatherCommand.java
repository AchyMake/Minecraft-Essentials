package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WeatherCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public WeatherCommand() {
        getInstance().getCommand("weather").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    getUserdata().setWeather(player, "clear");
                    player.sendMessage(getMessage().get("commands.weather.self", getMessage().get("weather.clear")));
                    return true;
                } else if (args[0].equalsIgnoreCase("rain")) {
                    getUserdata().setWeather(player, "rain");
                    player.sendMessage(getMessage().get("commands.weather.self", getMessage().get("weather.rain")));
                    return true;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    getUserdata().setWeather(player, "reset");
                    player.sendMessage(getMessage().get("commands.weather.self", getMessage().get("weather.reset")));
                    return true;
                }
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    if (target == player) {
                        if (args[0].equalsIgnoreCase("clear")) {
                            getUserdata().setWeather(target, "clear");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.clear")));
                        } else if (args[0].equalsIgnoreCase("rain")) {
                            getUserdata().setWeather(target, "rain");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.rain")));
                        } else if (args[0].equalsIgnoreCase("reset")) {
                            getUserdata().setWeather(target, "reset");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.reset")));
                        }
                    } else if (!target.hasPermission("essentials.command.weather.exempt")) {
                        if (args[0].equalsIgnoreCase("clear")) {
                            getUserdata().setWeather(target, "clear");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.clear")));
                        } else if (args[0].equalsIgnoreCase("rain")) {
                            getUserdata().setWeather(target, "rain");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.rain")));
                        } else if (args[0].equalsIgnoreCase("reset")) {
                            getUserdata().setWeather(target, "reset");
                            player.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.reset")));
                        }
                    } else player.sendMessage(getMessage().get("commands.weather.exempt", target.getName()));
                } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            var target = getInstance().getPlayer(args[1]);
            if (target != null) {
                if (args[0].equalsIgnoreCase("clear")) {
                    getUserdata().setWeather(target, "clear");
                    consoleCommandSender.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.clear")));
                } else if (args[0].equalsIgnoreCase("rain")) {
                    getUserdata().setWeather(target, "rain");
                    consoleCommandSender.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.rain")));
                } else if (args[0].equalsIgnoreCase("reset")) {
                    getUserdata().setWeather(target, "reset");
                    consoleCommandSender.sendMessage(getMessage().get("commands.weather.other", target.getName(), getMessage().get("weather.reset")));
                }
            } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[1]));
            return true;
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("clear");
                commands.add("rain");
                commands.add("reset");
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.weather.other")) {
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
