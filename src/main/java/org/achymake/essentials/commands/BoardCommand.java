package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScoreboardHandler getScoreboardHandler() {
        return getInstance().getScoreboardHandler();
    }
    public BoardCommand() {
        getInstance().getCommand("board").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getScoreboardHandler().hasBoard(player)) {
                    if (getUserdata().setBoolean(player, "settings.board", false)) {
                        getScoreboardHandler().disable(player);
                        player.sendMessage(getMessage().get("commands.board.self", getMessage().get("disable")));
                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                } else if (getUserdata().setBoolean(player, "settings.board", true)) {
                    getScoreboardHandler().apply(player);
                    player.sendMessage(getMessage().get("commands.board.self", getMessage().get("enable")));
                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawn.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getScoreboardHandler().hasBoard(target)) {
                                if (getUserdata().setBoolean(target, "settings.board", false)) {
                                    getScoreboardHandler().disable(target);
                                    player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("disable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else if (getUserdata().setBoolean(target, "settings.board", true)) {
                                getScoreboardHandler().apply(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                        } else if (!target.hasPermission("essentials.command.board.exempt")) {
                            if (getScoreboardHandler().hasBoard(target)) {
                                if (getUserdata().setBoolean(target, "settings.board", false)) {
                                    getScoreboardHandler().disable(target);
                                    player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("disable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else if (getUserdata().setBoolean(target, "settings.board", true)) {
                                getScoreboardHandler().apply(target);
                                player.sendMessage(getMessage().get("commands.board.other", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                        } else player.sendMessage(getMessage().get("commands.board.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getScoreboardHandler().hasBoard(target)) {
                        if (getUserdata().setBoolean(target, "settings.board", false)) {
                            getScoreboardHandler().disable(target);
                            consoleCommandSender.sendMessage(getMessage().get("commands.board.self", getMessage().get("disable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                    } else if (getUserdata().setBoolean(target, "settings.board", true)) {
                        getScoreboardHandler().apply(target);
                        consoleCommandSender.sendMessage(getMessage().get("commands.board.self", getMessage().get("enable")));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
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
                if (player.hasPermission("essentials.command.board.other")) {
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