package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NicknameCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public NicknameCommand() {
        getInstance().getCommand("nickname").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().setString(player, "display-name", player.getName())) {
                    player.sendMessage(getMessage().get("commands.nickname.self", player.getName()));
                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                return true;
            } else if (args.length == 1) {
                if (16 > args[0].length()) {
                    if (getUserdata().setString(player, "display-name", args[0])) {
                        player.sendMessage(getMessage().get("commands.nickname.self", args[0]));
                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.nickname.other")) {
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        if (!target.hasPermission("essentials.command.nickname.exempt")) {
                            if (16 > args[0].length()) {
                                if (getUserdata().setString(target, "display-name", args[0])) {
                                    player.sendMessage(getMessage().get("commands.nickname.sender", target.getName(), args[0]));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else player.sendMessage(getMessage().get("commands.nickname.limit", String.valueOf(args[0].length()), args[0]));
                        } else player.sendMessage(getMessage().get("commands.nickname.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
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
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.nickname.other")) {
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