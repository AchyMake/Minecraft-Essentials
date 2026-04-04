package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InformationCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public InformationCommand() {
        getInstance().getCommand("information").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                if (getUserdata().exists(offlinePlayer)) {
                    var simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    player.sendMessage(getMessage().get("commands.information.title"));
                    player.sendMessage(getMessage().get("commands.information.name", offlinePlayer.getName()));
                    player.sendMessage(getMessage().get("commands.information.account", getEconomyHandler().currency()) + getEconomyHandler().format(getEconomyHandler().getAccount(offlinePlayer)));
                    if (getEconomyHandler().hasBank(offlinePlayer)) {
                        var bank = getEconomyHandler().getBank(offlinePlayer);
                        player.sendMessage(getMessage().get("commands.information.bank.name", bank));
                        player.sendMessage(getMessage().get("commands.information.bank.account", getEconomyHandler().currency()) + getEconomyHandler().format(getBank().get(bank)));
                        player.sendMessage(getMessage().get("commands.information.bank.owner", getBank().getOwner(bank).getName()));
                        var members = getBank().getMembers(bank);
                        if (!members.isEmpty()) {
                            player.sendMessage(getMessage().get("commands.information.bank.member.title"));
                            for (var member : members) {
                                player.sendMessage(getMessage().get("commands.information.bank.member.listed", member.getName()));
                            }
                        }
                    }
                    player.sendMessage(getMessage().get("commands.information.homes", String.valueOf(getUserdata().getHomes(offlinePlayer).size())));
                    if (!getUserdata().getHomes(offlinePlayer).isEmpty()) {
                        getUserdata().getHomes(offlinePlayer).forEach(home -> player.sendMessage(getMessage().get("commands.information.listed", home)));
                    }
                    player.sendMessage(getMessage().get("commands.information.pvp", String.valueOf(getUserdata().isPVP(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.muted", String.valueOf(getUserdata().isMuted(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.frozen", String.valueOf(getUserdata().isFrozen(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.jailed", String.valueOf(getUserdata().isJailed(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.banned", String.valueOf(getUserdata().isBanned(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.ban-reason", getUserdata().getBanReason(offlinePlayer)));
                    player.sendMessage(getMessage().get("commands.information.ban-expire", simpleDateFormat.format(getUserdata().getBanExpire(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.vanished", String.valueOf(getUserdata().isVanished(offlinePlayer))));
                    player.sendMessage(getMessage().get("commands.information.last-online", getInstance().getDateHandler().getFormatted(offlinePlayer.getLastSeen())));
                    var join = getUserdata().getLocation(offlinePlayer, "join");
                    if (join != null) {
                        player.sendMessage(getMessage().get("commands.information.join-location", join.getWorld().getName(), String.valueOf(join.getBlockX()), String.valueOf(join.getBlockY()), String.valueOf(join.getBlockZ())));
                    }
                    var quit = getUserdata().getLocation(offlinePlayer, "quit");
                    if (quit != null) {
                        player.sendMessage(getMessage().get("commands.information.quit-location", quit.getWorld().getName(), String.valueOf(quit.getBlockX()), String.valueOf(quit.getBlockY()), String.valueOf(quit.getBlockZ())));
                    }
                    player.sendMessage(getMessage().get("commands.information.uuid", String.valueOf(offlinePlayer.getUniqueId())));
                } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
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
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata().isVanished(target)) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}