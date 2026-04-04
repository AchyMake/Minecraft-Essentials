package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnBanCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public UnBanCommand() {
        getInstance().getCommand("unban").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                if (getUserdata().exists(offlinePlayer)) {
                    if (getUserdata().isBanned(offlinePlayer)) {
                        if (getUserdata().unban(offlinePlayer)) {
                            player.sendMessage(getMessage().get("commands.unban.banned", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else player.sendMessage(getMessage().get("commands.unban.unbanned", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                if (getUserdata().exists(offlinePlayer)) {
                    if (getUserdata().isBanned(offlinePlayer)) {
                        if (getUserdata().unban(offlinePlayer)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.unban.banned", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.unban.unbanned", offlinePlayer.getName()));
                    return true;
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
                getInstance().getOfflinePlayers().forEach(offlinePlayer -> {
                    if (getUserdata().isBanned(offlinePlayer)) {
                        if (offlinePlayer.getName().startsWith(args[0])) {
                            commands.add(offlinePlayer.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}