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

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public DelHomeCommand() {
        getInstance().getCommand("delhome").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var homeName = args[0].toLowerCase();
                if (getUserdata().isHome(player, homeName)) {
                    if (getUserdata().setHome(player, null, homeName)) {
                        player.sendMessage(getMessage().get("commands.delhome.success", homeName));
                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                } else player.sendMessage(getMessage().get("commands.delhome.invalid", homeName));
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
                commands.addAll(getUserdata().getHomes(player));
            }
        }
        return commands;
    }
}