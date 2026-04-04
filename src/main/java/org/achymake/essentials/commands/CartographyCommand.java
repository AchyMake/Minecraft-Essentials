package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CartographyCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private InventoryHandler getInventoryHandler() {
        return getInstance().getInventoryHandler();
    }
    public CartographyCommand() {
        getInstance().getCommand("cartography").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getInventoryHandler().openCartographyTable(player) == null) {
                    player.sendMessage(getMessage().get("error.invalid"));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.cartography.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getInventoryHandler().openCartographyTable(target) == null) {
                                player.sendMessage(getMessage().get("error.invalid"));
                            } else player.sendMessage(getMessage().get("commands.cartography.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.cartography.exempt")) {
                            if (getInventoryHandler().openCartographyTable(target) == null) {
                                player.sendMessage(getMessage().get("error.invalid"));
                            } else player.sendMessage(getMessage().get("commands.cartography.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.cartography.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getInventoryHandler().openCartographyTable(target) == null) {
                        consoleCommandSender.sendMessage(getMessage().get("error.invalid"));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.cartography.sender", target.getName()));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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
                if (player.hasPermission("essentials.command.cartography.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[0])) {
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