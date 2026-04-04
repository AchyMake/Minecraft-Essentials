package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RTPCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public RTPCommand() {
        getInstance().getCommand("rtp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.rtp");
                if (!getUserdata().hasCooldown(player, "rtp", timer)) {
                    getUserdata().addCooldown(player, "rtp", timer);
                    getWorldHandler().randomTeleport(player);
                } else player.sendMessage(getMessage().get("commands.rtp.cooldown", getUserdata().getCooldown(player, "rtp", timer)));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.rtp.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getWorldHandler().randomTeleport(target);
                            player.sendMessage(getMessage().get("commands.rtp.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.rtp.exempt")) {
                            getWorldHandler().randomTeleport(target);
                            player.sendMessage(getMessage().get("commands.rtp.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.rtp.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    getWorldHandler().randomTeleport(target);
                    consoleCommandSender.sendMessage(getMessage().get("commands.rtp.sender", target.getName()));
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
                if (player.hasPermission("essentials.command.rtp.other")) {
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