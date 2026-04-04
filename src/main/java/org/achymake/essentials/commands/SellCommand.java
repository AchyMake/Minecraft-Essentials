package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SellCommand implements CommandExecutor, TabCompleter {
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
    public SellCommand() {
        getInstance().getCommand("sell").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var inventory = getInventoryHandler().create(54, getMessage().get("commands.sell.title"));
                player.openInventory(inventory);
                getInventoryHandler().getInventories().put(player, inventory);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.sell.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            var inventory = getInventoryHandler().create(54, getMessage().get("commands.sell.title"));
                            target.openInventory(inventory);
                            getInventoryHandler().getInventories().put(target, inventory);
                            player.sendMessage(getMessage().get("commands.sell.sender", target.getName()));
                        } else if (!target.hasPermission("essentials.command.sell.exempt")) {
                            var inventory = getInventoryHandler().create(54, getMessage().get("commands.sell.title"));
                            target.openInventory(inventory);
                            getInventoryHandler().getInventories().put(target, inventory);
                            player.sendMessage(getMessage().get("commands.sell.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.sell.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
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
                if (player.hasPermission("essentials.command.sell.other")) {
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