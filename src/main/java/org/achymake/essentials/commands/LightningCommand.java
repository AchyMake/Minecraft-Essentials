package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LightningCommand implements CommandExecutor, TabCompleter {
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
    public LightningCommand() {
        getInstance().getCommand("lightning").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var block = player.getTargetBlockExact(150);
                if (block != null) {
                    getWorldHandler().summonLightning(block.getLocation());
                    getMessage().sendActionBar(player, getMessage().get("commands.lightning.block", String.valueOf(block.getX()), String.valueOf(block.getY()), String.valueOf(block.getZ())));
                } else player.sendMessage(getMessage().get("commands.lightning.invalid"));
                return true;
            } else if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target == player) {
                        getWorldHandler().summonLightning(target.getLocation());
                        getMessage().sendActionBar(player, getMessage().get("commands.lightning.target", target.getName()));
                    } else if (!target.hasPermission("essentials.command.lightning.exempt")) {
                        getWorldHandler().summonLightning(target.getLocation());
                        getMessage().sendActionBar(player, getMessage().get("commands.lightning.target", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.lightning.exempt", target.getName()));
                } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            getInstance().getOnlinePlayers().forEach(target -> {
                if (!getUserdata().isVanished(target)) {
                    if (target.getName().startsWith(args[0])) {
                        commands.add(target.getName());
                    }
                }
            });
        }
        return commands;
    }
}