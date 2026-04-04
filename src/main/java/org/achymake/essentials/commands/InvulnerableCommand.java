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

public class InvulnerableCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public InvulnerableCommand() {
        getInstance().getCommand("invulnerable").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.setInvulnerable(!player.isInvulnerable());
                if (player.isInvulnerable()) {
                    getMessage().sendActionBar(player, getMessage().get("commands.invulnerable.self", getMessage().get("enable")));
                } else getMessage().sendActionBar(player, getMessage().get("commands.invulnerable.self", getMessage().get("disable")));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.invulnerable.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            target.setInvulnerable(!target.isInvulnerable());
                            if (target.isInvulnerable()) {
                                player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("disable")));
                        } else if (!target.hasPermission("essentials.command.invulnerable.exempt")) {
                            target.setInvulnerable(!target.isInvulnerable());
                            if (target.isInvulnerable()) {
                                player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("disable")));
                        } else player.sendMessage(getMessage().get("commands.invulnerable.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.invulnerable.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            var value = Boolean.parseBoolean(args[1]);
                            target.setInvulnerable(value);
                            if (target.isInvulnerable()) {
                                player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("disable")));
                        } else if (!target.hasPermission("essentials.command.invulnerable.exempt")) {
                            var value = Boolean.parseBoolean(args[1]);
                            target.setInvulnerable(value);
                            if (target.isInvulnerable()) {
                                player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.invulnerable.other", target.getName(), getMessage().get("disable")));
                        } else player.sendMessage(getMessage().get("commands.invulnerable.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.invulnerable.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (player.hasPermission("essentials.command.invulnerable.other")) {
                        commands.add(String.valueOf(target.isInvulnerable()));
                    }
                }
            }
        }
        return commands;
    }
}