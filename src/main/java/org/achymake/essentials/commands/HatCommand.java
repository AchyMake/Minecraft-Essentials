package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HatCommand implements CommandExecutor, TabCompleter {
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
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    public HatCommand() {
        getInstance().getCommand("hat").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterialHandler().isAir(heldItem)) {
                    var helmet = player.getInventory().getHelmet();
                    if (getInventoryHandler().setHelmet(player.getInventory(), heldItem)) {
                        player.sendMessage(getMessage().get("commands.hat.success", getMessage().toTitleCase(heldItem.getType().toString())));
                        heldItem.setAmount(heldItem.getAmount() - 1);
                    } else player.sendMessage(getMessage().get("commands.hat.occupied", getMessage().toTitleCase(helmet.getType().toString())));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.hat.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        var heldItem = player.getInventory().getItemInMainHand();
                        if (!getMaterialHandler().isAir(heldItem)) {
                            var helmet = target.getInventory().getHelmet();
                            if (target == player) {
                                if (getInventoryHandler().setHelmet(target.getInventory(), heldItem)) {
                                    player.sendMessage(getMessage().get("commands.hat.success", getMessage().toTitleCase(heldItem.getType().toString())));
                                    heldItem.setAmount(heldItem.getAmount() - 1);
                                } else player.sendMessage(getMessage().get("commands.hat.occupied", getMessage().toTitleCase(helmet.getType().toString())));
                            } else if (!target.hasPermission("essentials.command.hat.exempt")) {
                                if (getInventoryHandler().setHelmet(target.getInventory(), heldItem)) {
                                    player.sendMessage(getMessage().get("commands.hat.success", getMessage().toTitleCase(heldItem.getType().toString())));
                                    heldItem.setAmount(heldItem.getAmount() - 1);
                                } else player.sendMessage(getMessage().get("commands.hat.occupied", getMessage().toTitleCase(helmet.getType().toString())));
                            } else player.sendMessage(getMessage().get("commands.hat.exempt", target.getName()));
                        } else player.sendMessage(getMessage().get("error.item.invalid"));
                    } else player.sendMessage(getMessage().get("error.target.invalid", args[0]));
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
                if (player.hasPermission("essentials.command.hat.other")) {
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