package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EconomyHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    public double get(OfflinePlayer offlinePlayer) {
        return getAccount(offlinePlayer);
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return getAccount(offlinePlayer) >= amount;
    }
    public boolean set(OfflinePlayer offlinePlayer, double amount) {
        return getUserdata().setDouble(offlinePlayer, "account", amount);
    }
    public boolean add(OfflinePlayer offlinePlayer, double amount) {
        return set(offlinePlayer, amount + get(offlinePlayer));
    }
    public boolean remove(OfflinePlayer offlinePlayer, double amount) {
        return set(offlinePlayer, get(offlinePlayer) - amount);
    }
    /**
     * get account
     * @param offlinePlayer or player
     * @return double
     * @since many moons ago
     */
    public double getAccount(OfflinePlayer offlinePlayer) {
        return getUserdata().getConfig(offlinePlayer).getDouble("account");
    }
    /**
     * has bank
     * @param offlinePlayer or player
     * @return true if offlinePlayer has bank else false
     * @since many moons ago
     */
    public boolean hasBank(OfflinePlayer offlinePlayer) {
        return !getBank(offlinePlayer).isEmpty();
    }
    /**
     * get bank name
     * @param offlinePlayer or player
     * @return string
     * @since many moons ago
     */
    public String getBank(OfflinePlayer offlinePlayer) {
        var config = getUserdata().getConfig(offlinePlayer);
        if (config.isString("bank")) {
            return config.getString("bank");
        } else return "";
    }
    /**
     * get bank rank
     * @param offlinePlayer
     * or player
     * @return string
     * @since many moons ago
     */
    public String getBankRank(OfflinePlayer offlinePlayer) {
        var config = getUserdata().getConfig(offlinePlayer);
        if (config.isString("bank-rank")) {
            return config.getString("bank-rank");
        } else return "default";
    }
    /**
     * get set map offlinePlayer, double
     * @return set map offlinePlayer, double
     * @since many moons ago
     */
    public List<Map.Entry<OfflinePlayer, Double>> getTopAccounts() {
        var accounts = new HashMap<OfflinePlayer, Double>();
        for (var offlinePlayer : getInstance().getOfflinePlayers()) {
            if (!getUserdata().isBanned(offlinePlayer) || !getUserdata().isDisabled(offlinePlayer)) {
                if (has(offlinePlayer, 0.01)) {
                    accounts.put(offlinePlayer, get(offlinePlayer));
                }
            }
        }
        var listed = new ArrayList<>(accounts.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed.stream().limit(10).toList();
    }
    /**
     * get set map string, double
     * @return set map string, double
     * @since many moons ago
     */
    public List<Map.Entry<String, Double>> getTopBanks() {
        var bankAccounts = new HashMap<String, Double>();
        for (var bankName : getBank().getListed()) {
            if (getBank().has(bankName, 0.01)) {
                bankAccounts.put(bankName, getBank().get(bankName));
            }
        }
        var listed = new ArrayList<>(bankAccounts.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed.stream().limit(10).toList();
    }
    public String format(double amount) {
        return new DecimalFormat(getConfig().getString("economy.format")).format(amount);
    }
    public String currency() {
        return getConfig().getString("economy.currency");
    }
    public boolean autoCreateBank() {
        return getConfig().getBoolean("economy.bank.auto-create");
    }
    public double getStartingBalance() {
        return getConfig().getDouble("economy.starting-balance");
    }
    public double getStartingBank() {
        return getConfig().getDouble("economy.bank.starting-balance");
    }
    public double getMinimumPayment() {
        return getConfig().getDouble("economy.minimum-payment");
    }
    public double getMinimumBankWithdraw() {
        return getConfig().getDouble("economy.bank.minimum-withdraw");
    }
    public double getMinimumBankDeposit() {
        return getConfig().getDouble("economy.bank.minimum-withdraw");
    }
}