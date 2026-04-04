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

public class FlySpeedCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public FlySpeedCommand() {
        getInstance().getCommand("flyspeed").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var value = Float.parseFloat(args[0]);
                getUserdata().setFlySpeed(player, value);
                player.sendMessage(getMessage().get("commands.flyspeed.self", String.valueOf(value)));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.flyspeed.other")) {
                    var value = Float.parseFloat(args[0]);
                    var target = getInstance().getPlayer(args[1]);
                    if (target != null) {
                        if (target == player) {
                            getUserdata().setFlySpeed(player, value);
                            target.sendMessage(getMessage().get("commands.flyspeed.target", player.getName(), String.valueOf(value)));
                            player.sendMessage(getMessage().get("commands.flyspeed.sender", target.getName(), String.valueOf(value)));
                        } else if (!target.hasPermission("essentials.command.flyspeed.exempt")) {
                            getUserdata().setFlySpeed(target, value);
                            target.sendMessage(getMessage().get("commands.flyspeed.target", player.getName(), String.valueOf(value)));
                            player.sendMessage(getMessage().get("commands.flyspeed.sender", target.getName(), String.valueOf(value)));
                        } else player.sendMessage(getMessage().get("commands.flyspeed.exempt", target.getName()));
                        return true;
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
                commands.add("1");
                commands.add("2");
                commands.add("4");
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.flyspeed.other")) {
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