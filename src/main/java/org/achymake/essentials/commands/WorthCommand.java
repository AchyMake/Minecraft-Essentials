package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worth;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public WorthCommand() {
        getInstance().getCommand("worth").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var itemName = getMessage().toTitleCase(args[0]);
                var material = getMaterialHandler().get(args[0]);
                if (getWorth().isListed(material)) {
                    player.sendMessage(getMessage().get("commands.worth.listed", itemName, getEconomyHandler().currency() + getEconomyHandler().format(getWorth().get(material))));
                } else player.sendMessage(getMessage().get("commands.worth.unlisted", itemName));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var itemName = getMessage().toTitleCase(args[0]);
                var material = getMaterialHandler().get(args[0]);
                if (getWorth().isListed(material)) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.worth.listed", itemName, getEconomyHandler().currency() + getEconomyHandler().format(getWorth().get(material))));
                } else consoleCommandSender.sendMessage(getMessage().get("commands.worth.unlisted", itemName));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (!getWorth().getListed().isEmpty()) {
                    getWorth().getListed().forEach(listed -> {
                        var lowered = listed.toLowerCase();
                        if (lowered.startsWith(args[0])) {
                            commands.add(lowered);
                        }
                    });
                }
            }
        }
        return commands;
    }
}