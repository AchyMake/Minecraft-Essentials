package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnerCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SpawnerCommand() {
        getInstance().getCommand("spawner").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        var target = getInstance().getPlayer(args[1]);
                        if (target != null) {
                            var entityType = args[2].toUpperCase();
                            int amount = Integer.parseInt(args[3]);
                            if (amount > 0) {
                                getMaterialHandler().giveItemStack(target, getMaterialHandler().getSpawner(entityType, amount));
                                player.sendMessage(getMessage().get("commands.spawner.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(entityType)));
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        var entityType = args[2].toUpperCase();
                        int amount = Integer.parseInt(args[3]);
                        if (amount > 0) {
                            getMaterialHandler().giveItemStack(target, getMaterialHandler().getSpawner(entityType, amount));
                            consoleCommandSender.sendMessage(getMessage().get("commands.spawner.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(entityType)));
                            return true;
                        }
                    }
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
                if (player.hasPermission("essentials.command.spawner.give")) {
                    commands.add("give");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (target.getName().startsWith(args[1])) {
                                commands.add(target.getName());
                            }
                        });
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        for(var entityType : EntityType.values()) {
                            var type = entityType.toString().toLowerCase().replace("player", "null");
                            if (type.startsWith(args[2])) {
                                commands.add(type);
                            }
                        }
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        commands.add("1");
                        commands.add("2");
                        commands.add("3");
                    }
                }
            }
        }
        return commands;
    }
}
