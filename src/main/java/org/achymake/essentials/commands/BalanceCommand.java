package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public BalanceCommand() {
        getInstance().getCommand("balance").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().get("commands.balance.self", getEconomyHandler().currency() + getEconomyHandler().format(getEconomyHandler().getAccount(player))));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.balance.top")) {
                        player.sendMessage(getMessage().get("commands.balance.top.title"));
                        var list = new ArrayList<>(getEconomyHandler().getTopAccounts());
                        for (int i = 0; i < list.size(); i++) {
                            player.sendMessage(getMessage().get("commands.balance.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomyHandler().currency() + getEconomyHandler().format(list.get(i).getValue())));
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.balance.top.title"));
                    var list = new ArrayList<>(getEconomyHandler().getTopAccounts());
                    for (int i = 0; i < list.size(); i++) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.balance.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomyHandler().currency() + getEconomyHandler().format(list.get(i).getValue())));
                    }
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
                if (player.hasPermission("essentials.command.balance.top")) {
                    commands.add("top");
                }
            }
        }
        return commands;
    }
}