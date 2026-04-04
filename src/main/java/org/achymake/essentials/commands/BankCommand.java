package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {
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
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    public BankCommand() {
        getInstance().getCommand("bank").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getEconomyHandler().hasBank(player)) {
                    player.sendMessage(getMessage().get("commands.bank.self", getEconomyHandler().getBank(player), getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(getEconomyHandler().getBank(player)))));
                } else player.sendMessage(getMessage().get("error.bank.empty"));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info")) {
                        if (getEconomyHandler().hasBank(player)) {
                            var bank = getEconomyHandler().getBank(player);
                            var owner = getBank().getOwner(bank);
                            var members = getBank().getMembers(bank);
                            player.sendMessage(getMessage().get("commands.bank.info.title"));
                            player.sendMessage(getMessage().get("commands.bank.info.name", bank));
                            player.sendMessage(getMessage().get("commands.bank.info.account", getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(bank))));
                            player.sendMessage(getMessage().get("commands.bank.info.owner", owner.getName()));
                            if (!members.isEmpty()) {
                                player.sendMessage(getMessage().get("commands.bank.info.member.title"));
                                for (var member : members) {
                                    player.sendMessage(getMessage().get("commands.bank.info.member.listed", member.getName(), getEconomyHandler().getBankRank(member)));
                                }
                            }
                        } else player.sendMessage(getMessage().get("error.bank.empty"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.delete")) {
                            var bank = getEconomyHandler().getBank(player);
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("owner")) {
                                if (!getBank().has(bank, 0.01)) {
                                    getBank().delete(bank);
                                    player.sendMessage(getMessage().get("commands.bank.delete.success"));
                                } else player.sendMessage(getMessage().get("commands.bank.delete.sufficient-funds", getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(bank))));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.leave")) {
                            var bank = getEconomyHandler().getBank(player);
                            if (!getBank().isOwner(bank, player)) {
                                if (getBank().isMember(bank, player)) {
                                    if (getBank().removeMember(bank, player)) {
                                        player.sendMessage(getMessage().get("commands.bank.leave", bank));
                                        getUserdata().setString(player, "bank", "");
                                        getUserdata().setString(player, "bank-rank", "default");
                                    } else player.sendMessage(getMessage().get("error.file.exception", getBank().getFile(getEconomyHandler().getBank(player)).getName()));
                                }
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        if (getUserdata().getBankFrom(player) != null) {
                            var target = getUserdata().getBankFrom(player);
                            if (target != null) {
                                var bankTask = getUserdata().getTaskID(target, "bank-invite");
                                if (getScheduleHandler().isQueued(bankTask)) {
                                    if (getBank().addMember(getEconomyHandler().getBank(target), player)) {
                                        target.sendMessage(getMessage().get("commands.bank.accept.target", player.getName()));
                                        player.sendMessage(getMessage().get("commands.bank.accept.sender", target.getName(), getEconomyHandler().getBank(target)));
                                        getUserdata().setBankSent(target, null);
                                        getUserdata().removeTask(target, "bank-invite");
                                        getUserdata().setBankFrom(player, null);
                                        getUserdata().removeTask(player, "bank-invite");
                                    } else player.sendMessage(getMessage().get("error.file.exception", getBank().getFile(getEconomyHandler().getBank(player)).getName()));
                                }
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        if (getUserdata().getBankFrom(player) != null) {
                            var target = getUserdata().getBankFrom(player);
                            if (target != null) {
                                var bankTask = getUserdata().getTaskID(target, "bank-invite");
                                if (getScheduleHandler().isQueued(bankTask)) {
                                    target.sendMessage(getMessage().get("commands.bank.deny.target", player.getName()));
                                    player.sendMessage(getMessage().get("commands.bank.deny.sender", target.getName(), getEconomyHandler().getBank(target)));
                                    getUserdata().setBankSent(target, null);
                                    getUserdata().removeTask(target, "bank-invite");
                                    getUserdata().setBankFrom(player, null);
                                    getUserdata().removeTask(player, "bank-invite");
                                }
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("cancel")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        var target = getUserdata().getBankSent(player).getPlayer();
                        if (target != null) {
                            var bankTask = getUserdata().getTaskID(player, "bank-invite");
                            if (getScheduleHandler().isQueued(bankTask)) {
                                target.sendMessage(getMessage().get("commands.bank.cancel.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.bank.cancel.sender", target.getName()));
                                getUserdata().setBankFrom(target, null);
                                getUserdata().removeTask(target, "bank-invite");
                                getUserdata().setBankSent(player, null);
                                getUserdata().removeTask(player, "bank-invite");
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.bank.top")) {
                        var top = getEconomyHandler().getTopBanks();
                        if (!top.isEmpty()) {
                            player.sendMessage(getMessage().get("commands.bank.top.title"));
                            var list = new ArrayList<>(top);
                            for (int i = 0; i < list.size(); i++) {
                                player.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey(), getEconomyHandler().currency() + getEconomyHandler().format(list.get(i).getValue())));
                            }
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info.other")) {
                        if (getBank().exists(args[1])) {
                            var owner = getBank().getOwner(args[1]);
                            var members = getBank().getMembers(args[1]);
                            player.sendMessage(getMessage().get("commands.bank.info.title"));
                            player.sendMessage(getMessage().get("commands.bank.info.name", args[1]));
                            player.sendMessage(getMessage().get("commands.bank.info.account", getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(args[1]))));
                            player.sendMessage(getMessage().get("commands.bank.info.owner", owner.getName()));
                            if (!members.isEmpty()) {
                                player.sendMessage(getMessage().get("commands.bank.info.member.title"));
                                for (var member : members) {
                                    player.sendMessage(getMessage().get("commands.bank.info.member.listed", member.getName(), getEconomyHandler().getBankRank(member)));
                                }
                            }
                        } else player.sendMessage(getMessage().get("error.bank.invalid", args[1]));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.remove")) {
                            var bank = getEconomyHandler().getBank(player);
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                var target = getInstance().getOfflinePlayer(args[1]);
                                if (getBank().isMember(bank, target)) {
                                    if (getBank().removeMember(bank, target)) {
                                        player.sendMessage(getMessage().get("commands.bank.remove", target.getName(), bank));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getBank().getFile(getEconomyHandler().getBank(player)).getName()));
                                    return true;
                                }
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.invite")) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                var target = getInstance().getPlayer(args[1]);
                                if (target != null) {
                                    if (target != player) {
                                        if (!getUserdata().hasTaskID(player, "bank-invite")) {
                                            if (!getEconomyHandler().hasBank(target)) {
                                                var taskID = getScheduleHandler().runLater(() -> {
                                                    getUserdata().setBankFrom(target, null);
                                                    getUserdata().removeTask(target, "bank-invite");
                                                    getUserdata().setBankSent(player, null);
                                                    getUserdata().removeTask(player, "bank-invite");
                                                    target.sendMessage(getMessage().get("commands.bank.invite.expired"));
                                                    player.sendMessage(getMessage().get("commands.bank.invite.expired"));
                                                }, 300).getTaskId();
                                                getUserdata().setBankFrom(target, player);
                                                getUserdata().addTaskID(target, "bank-invite", taskID);
                                                getUserdata().setBankSent(player, target);
                                                getUserdata().addTaskID(player, "bank-invite", taskID);
                                                target.sendMessage(getMessage().get("commands.bank.invite.target.notify", player.getName()));
                                                target.sendMessage(getMessage().get("commands.bank.invite.target.decide"));
                                                player.sendMessage(getMessage().get("commands.bank.invite.sender.notify", target.getName()));
                                                player.sendMessage(getMessage().get("commands.bank.invite.sender.decide"));
                                            } else player.sendMessage(getMessage().get("commands.bank.invite.already-has", target.getName()));
                                        } else {
                                            player.sendMessage(getMessage().get("commands.bank.invite.occupied"));
                                            player.sendMessage(getMessage().get("commands.bank.invite.sender.decide"));
                                        }
                                    }
                                }
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rename")) {
                    if (player.hasPermission("essentials.command.bank.rename")) {
                        if (getEconomyHandler().hasBank(player)) {
                            var bank = getEconomyHandler().getBank(player);
                            if (getBank().isOwner(bank, player)) {
                                if (getBank().rename(bank, args[1])) {
                                    if (getUserdata().setString(player, "bank", args[1])) {
                                        var members = getBank().getMembers(args[1]);
                                        if (!members.isEmpty()) {
                                            for (var member : members) {
                                                if (!getUserdata().setString(member, "bank", args[1])) {
                                                    player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(member).getName()));
                                                }
                                            }
                                        }
                                        player.sendMessage(getMessage().get("commands.bank.rename", args[1]));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                                } else player.sendMessage(getMessage().get("error.bank.exists", args[1]));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (!getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.create")) {
                            if (getBank().create(args[1], player)) {
                                player.sendMessage(getMessage().get("commands.bank.create", args[1]));
                            } else player.sendMessage(getMessage().get("error.bank.exists", args[1]));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (player.hasPermission("essentials.command.bank.withdraw")) {
                        if (getEconomyHandler().hasBank(player)) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                var amount = Double.parseDouble(args[1]);
                                if (amount >= getEconomyHandler().getMinimumBankWithdraw()) {
                                    if (getBank().has(getEconomyHandler().getBank(player), amount)) {
                                        if (getBank().remove(getEconomyHandler().getBank(player), amount)) {
                                            if (getEconomyHandler().add(player, amount)) {
                                                player.sendMessage(getMessage().get("commands.bank.withdraw.success", getEconomyHandler().currency() + getEconomyHandler().format(amount)));
                                                player.sendMessage(getMessage().get("commands.bank.withdraw.left", getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(getEconomyHandler().getBank(player)))));
                                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                                        } else player.sendMessage(getMessage().get("error.file.exception", getBank().getFile(getEconomyHandler().getBank(player)).getName()));
                                    } else player.sendMessage(getMessage().get("commands.bank.withdraw.insufficient-funds", getEconomyHandler().currency() + getEconomyHandler().format(amount)));
                                } else player.sendMessage(getMessage().get("commands.bank.withdraw.minimum", getEconomyHandler().currency() + getEconomyHandler().format(getEconomyHandler().getMinimumBankWithdraw())));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.deposit")) {
                        if (getEconomyHandler().hasBank(player)) {
                            var amount = Double.parseDouble(args[1]);
                            if (amount >= getEconomyHandler().getMinimumBankDeposit()) {
                                if (getEconomyHandler().has(player, amount)) {
                                    if (getEconomyHandler().remove(player, amount)) {
                                        if (getBank().add(getEconomyHandler().getBank(player), amount)) {
                                            player.sendMessage(getMessage().get("commands.bank.deposit.success", getEconomyHandler().currency() + getEconomyHandler().format(amount)));
                                            player.sendMessage(getMessage().get("commands.bank.deposit.left", getEconomyHandler().currency() + getEconomyHandler().format(getBank().get(getEconomyHandler().getBank(player)))));
                                        } else player.sendMessage(getMessage().get("error.file.exception", getBank().getFile(getEconomyHandler().getBank(player)).getName()));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                                } else player.sendMessage(getMessage().get("commands.bank.deposit.insufficient-funds", getEconomyHandler().currency() + getEconomyHandler().format(amount)));
                            } else player.sendMessage(getMessage().get("commands.bank.deposit.minimum", getEconomyHandler().currency() + getEconomyHandler().format(getEconomyHandler().getMinimumBankDeposit())));
                            return true;
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rank")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.rank")) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("owner")) {
                                var target = getInstance().getOfflinePlayer(args[1]);
                                if (getBank().isMember(getEconomyHandler().getBank(player), target)) {
                                    if (args[2].equalsIgnoreCase("default") ||
                                            args[2].equalsIgnoreCase("member") ||
                                            args[2].equalsIgnoreCase("co-owner")) {
                                        if (getUserdata().setString(target, "bank-rank", args[2])) {
                                            player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                        return true;
                                    }
                                }
                            } else if (rank.equalsIgnoreCase("co-owner")) {
                                var target = getInstance().getOfflinePlayer(args[1]);
                                if (getBank().isMember(getEconomyHandler().getBank(player), target)) {
                                    if (args[2].equalsIgnoreCase("default") ||
                                            args[2].equalsIgnoreCase("member")) {
                                        if (getUserdata().setString(target, "bank-rank", args[2])) {
                                            player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.title"));
                    var list = new ArrayList<>(getEconomyHandler().getTopBanks());
                    for (int i = 0; i < list.size(); i++) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey(), getEconomyHandler().currency() + getEconomyHandler().format(list.get(i).getValue())));
                    }
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
                if (getUserdata().hasTaskID(player, "bank-invite")) {
                    commands.add("accept");
                    commands.add("deny");
                }
                if (getEconomyHandler().hasBank(player)) {
                    if (!getBank().isOwner(getEconomyHandler().getBank(player), player)) {
                        if (player.hasPermission("essentials.command.bank.leave")) {
                            commands.add("leave");
                        }
                    }
                }
                if (getEconomyHandler().hasBank(player)) {
                    var rank = getEconomyHandler().getBankRank(player);
                    if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                        if (player.hasPermission("essentials.command.bank.rank")) {
                            commands.add("rank");
                        }
                    }
                }
                if (getEconomyHandler().hasBank(player)) {
                    var rank = getEconomyHandler().getBankRank(player);
                    if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                        if (player.hasPermission("essentials.command.bank.remove")) {
                            commands.add("remove");
                        }
                    }
                }
                if (getEconomyHandler().hasBank(player)) {
                    var rank = getEconomyHandler().getBankRank(player);
                    if (rank.equalsIgnoreCase("owner")) {
                        if (player.hasPermission("essentials.command.bank.delete")) {
                            commands.add("delete");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.info")) {
                    commands.add("info");
                }
                if (player.hasPermission("essentials.command.bank.create")) {
                    if (!getEconomyHandler().hasBank(player)) {
                        commands.add("create");
                    } else if (player.hasPermission("essentials.command.bank.invite")) {
                        var rank = getEconomyHandler().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            commands.add("invite");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.rename")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (getBank().isOwner(getEconomyHandler().getBank(player), player)) {
                            commands.add("rename");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.top")) {
                    commands.add("top");
                }
                if (player.hasPermission("essentials.command.bank.withdraw")) {
                    if (getEconomyHandler().hasBank(player)) {
                        var rank = getEconomyHandler().getBankRank(player);
                        if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            commands.add("withdraw");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.deposit")) {
                    if (getEconomyHandler().hasBank(player)) {
                        commands.add("deposit");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info.other")) {
                        for (var bank : getBank().getListed()) {
                            if (bank.startsWith(args[1])) {
                                commands.add(bank);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rank")) {
                    if (getEconomyHandler().hasBank(player)) {
                        var bank = getEconomyHandler().getBank(player);
                        var rank = getEconomyHandler().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            if (!getBank().getMembers(bank).isEmpty()) {
                                getBank().getMembers(bank).forEach(target -> {
                                    if (!getUserdata().isVanished(target)) {
                                        if (target.getName().startsWith(args[1])) {
                                            commands.add(target.getName());
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.invite")) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("owner") || rank.equalsIgnoreCase("co-owner")) {
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
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (getEconomyHandler().hasBank(player)) {
                        var bank = getEconomyHandler().getBank(player);
                        var rank = getEconomyHandler().getBankRank(player);
                        if (rank.equalsIgnoreCase("owner") || rank.equalsIgnoreCase("co-owner")) {
                            if (!getBank().getMembers(bank).isEmpty()) {
                                getBank().getMembers(bank).forEach(target -> {
                                    if (target.getName().startsWith(args[1])) {
                                        commands.add(target.getName());
                                    }
                                });
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rename")) {
                    if (player.hasPermission("essentials.command.bank.rename")) {
                        if (getEconomyHandler().hasBank(player)) {
                            if (getBank().isOwner(getEconomyHandler().getBank(player), player)) {
                                commands.add(getEconomyHandler().getBank(player));
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("essentials.command.bank.create")) {
                        if (!getEconomyHandler().hasBank(player)) {
                            commands.add(player.getName());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.deposit")) {
                            commands.add("8");
                            commands.add("16");
                            commands.add("32");
                            commands.add("64");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (getEconomyHandler().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.withdraw")) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                commands.add("8");
                                commands.add("16");
                                commands.add("32");
                                commands.add("64");
                            }
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rank")) {
                    if (player.hasPermission("essentials.command.bank.rank")) {
                        if (getEconomyHandler().hasBank(player)) {
                            var rank = getEconomyHandler().getBankRank(player);
                            if (rank.equalsIgnoreCase("co-owner")) {
                                commands.add("default");
                                commands.add("member");
                            }
                            if (rank.equalsIgnoreCase("owner")) {
                                commands.add("default");
                                commands.add("member");
                                commands.add("co-owner");
                            }
                        }
                    }
                }
            }
        }
        return commands;
    }
}