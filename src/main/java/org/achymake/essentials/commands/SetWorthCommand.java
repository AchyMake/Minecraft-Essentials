package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worth;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetWorthCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    public SetWorthCommand() {
        getInstance().getCommand("setworth").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var materialName = args[0].toUpperCase();
                if (getWorth().setWorth(getMaterialHandler().get(materialName), 0)) {
                    player.sendMessage(getMessage().get("commands.setworth.disable", getMessage().toTitleCase(materialName)));
                } else player.sendMessage(getMessage().get("error.file.exception", getWorth().getFile().getName()));
                return true;
            } else if (args.length == 2) {
                var materialName = args[0].toUpperCase();
                var material = getMaterialHandler().get(materialName);
                if (material != null) {
                    try {
                        var worth = Double.parseDouble(args[1]);
                        if (getWorth().setWorth(material, worth)) {
                            if (getWorth().isListed(material)) {
                                player.sendMessage(getMessage().get("commands.setworth.enable", getMessage().toTitleCase(materialName), getEconomyHandler().currency() + getEconomyHandler().format(getWorth().get(material))));
                            } else player.sendMessage(getMessage().get("commands.setworth.disable", getMessage().toTitleCase(materialName)));
                        } else player.sendMessage(getMessage().get("error.file.exception", getWorth().getFile().getName()));
                        return true;
                    } catch (NumberFormatException | NullPointerException e) {
                        throw new RuntimeException(e);
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
                for (var material : Material.values()) {
                    var itemName = material.name().toLowerCase();
                    if (itemName.startsWith(args[0])) {
                        commands.add(itemName);
                    }
                }
            } else if (args.length == 2) {
                commands.add("0.25");
                commands.add("0.50");
                commands.add("0.75");
                commands.add("1.00");
            }
        }
        return commands;
    }
}