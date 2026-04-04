package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    public DelWarpCommand() {
        getInstance().getCommand("delwarp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (getWarps().isListed(warpName)) {
                    if (getWarps().setLocation(warpName, null)) {
                        player.sendMessage(getMessage().get("commands.delwarp.success", warpName));
                    } else player.sendMessage(getMessage().get("error.file.exception", getWarps().getFile().getName()));
                } else player.sendMessage(getMessage().get("commands.delwarp.invalid", warpName));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (getWarps().isListed(warpName)) {
                    if (getWarps().setLocation(warpName, null)) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.delwarp.success", warpName));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getWarps().getFile().getName()));
                } else consoleCommandSender.sendMessage(getMessage().get("commands.delwarp.invalid", warpName));
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
                commands.addAll(getWarps().getListed());
            }
        }
        return commands;
    }
}