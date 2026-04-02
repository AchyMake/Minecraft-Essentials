package org.achymake.essentials.providers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlaceholderProvider extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return Essentials.getInstance().name().toLowerCase();
    }
    @Override
    public String getAuthor() {
        return "AchyMake";
    }
    @Override
    public String getVersion() {
        return Essentials.getInstance().version();
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    @Override
    public boolean register() {
        return super.register();
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        } else {
            var instance = Essentials.getInstance();
            var userdata = instance.getUserdata();
            var bank = instance.getBank();
            var economy = instance.getEconomyHandler();
            switch (params) {
                case "name" -> {
                    return player.getName();
                }
                case "display_name" -> {
                    return userdata.getDisplayName(player);
                }
                case "account" -> {
                    return economy.currency() + economy.format(economy.getAccount(player));
                }
                case "bank_name" -> {
                    if (economy.hasBank(player)) {
                        return economy.getBank(player);
                    } else return "None";
                }
                case "bank_account" -> {
                    if (economy.hasBank(player)) {
                        return economy.currency() + economy.format(bank.get(economy.getBank(player)));
                    } else return "0";
                }
                case "bank_owner" -> {
                    if (economy.hasBank(player)) {
                        return bank.getOwner(economy.getBank(player)).getName();
                    } else return "None";
                }
                case "pvp" -> {
                    return String.valueOf(userdata.isPVP(player));
                }
                case "homes_max" -> {
                    return String.valueOf(userdata.getMaxHomes(player));
                }
                case "homes_size" -> {
                    return String.valueOf(userdata.getHomes(player).size());
                }
                case "homes_left" -> {
                    return String.valueOf(userdata.getMaxHomes(player) - userdata.getHomes(player).size());
                }
                case "vanished" -> {
                    return String.valueOf(instance.getVanishHandler().isVanish(player));
                }
                case "online_players" -> {
                    return String.valueOf(instance.getOnlinePlayers().size() - instance.getVanishHandler().getVanished().size());
                }
                case "health" -> {
                    return String.valueOf((int) player.getHealth());
                }
                case "walk_speed" -> {
                    return String.valueOf(player.getWalkSpeed());
                }
                case "fly_speed" -> {
                    return String.valueOf(player.getFlySpeed());
                }
                case "is_flying" -> {
                    return String.valueOf(player.isFlying());
                }
                case "is_invulnerable" -> {
                    return String.valueOf(player.isInvulnerable());
                }
                case "has_passenger" -> {
                    return String.valueOf(!player.isEmpty());
                }
                case "is_inside_vehicle" -> {
                    return String.valueOf(player.isInsideVehicle());
                }
                case "is_sleeping" -> {
                    return String.valueOf(player.isSleeping());
                }
                case "is_whitelisted" -> {
                    return String.valueOf(player.isWhitelisted());
                }
                case "is_collidable" -> {
                    return String.valueOf(player.isCollidable());
                }
                case "is_sprinting" -> {
                    return String.valueOf(player.isSprinting());
                }
                case "is_sneaking" -> {
                    return String.valueOf(player.isSneaking());
                }
                case "locale" -> {
                    return player.getLocale();
                }
                case "ping" -> {
                    return String.valueOf(player.getPing());
                }
                case "world_name" -> {
                    return player.getWorld().getName();
                }
                case "world_seed" -> {
                    return String.valueOf(player.getWorld().getSeed());
                }
                case "world_environment" -> {
                    return instance.getMessage().toTitleCase(player.getWorld().getEnvironment().toString());
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}