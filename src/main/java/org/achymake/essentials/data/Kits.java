package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Kits {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "kits.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    /**
     * get listed
     * @return set string
     * @since many moons ago
     */
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    /**
     * is kitName listed
     * @return true if kitName exists else false
     * @since many moons ago
     */
    public boolean isListed(String kitName) {
        return getListed().contains(kitName);
    }
    public boolean hasCooldown(String kitName) {
        return config.isInt(kitName + ".cooldown");
    }
    /**
     * get cooldown
     * @param kitName string
     * @return integer if null this will return 0
     * @since many moons ago
     */
    public int getCooldown(String kitName) {
        return config.getInt(kitName + ".cooldown");
    }
    /**
     * has price
     * @param kitName string
     * @return true if kitName has price
     * @since many moons ago
     */
    public boolean hasPrice(String kitName) {
        return config.isDouble(kitName + ".price");
    }
    /**
     * get price
     * @param kitName string
     * @return double if kitName has price else 0.0
     * @since many moons ago
     */
    public double getPrice(String kitName) {
        return config.getDouble(kitName + ".price");
    }
    /**
     * get kit
     * @param kitName string
     * @return list itemStack if kitName exists else empty list
     * @since many moons ago
     */
    public List<ItemStack> get(String kitName) {
        var itemStacks = new ArrayList<ItemStack>();
        if (config.isConfigurationSection(kitName + ".items")) {
            config.getConfigurationSection(kitName + ".items").getKeys(false).forEach(sections -> {
                var section = kitName + ".items." + sections;
                var materialName = config.getString(section + ".type");
                if (materialName != null) {
                    var itemStack = getMaterialHandler().getItemStack(materialName, 1);
                    if (itemStack != null) {
                        var amount = config.getInt(section + ".amount");
                        if (amount > 0) {
                            itemStack.setAmount(amount);
                        }
                        var meta = itemStack.getItemMeta();
                        var name = config.getString(section + ".name");
                        if (name != null) {
                            meta.setDisplayName(getMessage().addColor(name));
                        }
                        var stringList = config.getStringList(section + ".lore");
                        if (!stringList.isEmpty()) {
                            var lore = new ArrayList<String>();
                            for (var string : stringList) {
                                lore.add(getMessage().addColor(string));
                            }
                            meta.setLore(lore);
                        }
                        if (config.isConfigurationSection(section + ".enchantments")) {
                            config.getConfigurationSection(section + ".enchantments").getKeys(false).forEach(enchantmentString -> {
                                var enchantment = Enchantment.getByName(enchantmentString.toUpperCase());
                                if (enchantment != null) {
                                    var lvl = config.getInt(section + ".enchantments." + enchantmentString);
                                    if (lvl > 0) {
                                        meta.addEnchant(enchantment, lvl, true);
                                    }
                                }
                            });
                        }
                        itemStack.setItemMeta(meta);
                        itemStacks.add(itemStack);
                    }
                }
            });
        }
        return itemStacks;
    }
    /**
     * setup
     * @since many moons ago
     */
    private boolean setup() {
        var lore = new ArrayList<String>();
        lore.add("&9Kit");
        lore.add("&7- &6Starter");
        config.options().copyDefaults(true);
        config.addDefault("starter.price", 75.0);
        config.addDefault("starter.cooldown", 3600);
        config.addDefault("starter.items.sword.type", "stone_sword");
        config.addDefault("starter.items.sword.amount", 1);
        config.addDefault("starter.items.sword.name", "&6Stone Sword");
        config.addDefault("starter.items.sword.lore", lore);
        config.addDefault("starter.items.sword.enchantments.unbreaking", 1);
        config.addDefault("starter.items.pickaxe.type", "stone_pickaxe");
        config.addDefault("starter.items.pickaxe.amount", 1);
        config.addDefault("starter.items.pickaxe.name", "&6Stone Pickaxe");
        config.addDefault("starter.items.pickaxe.lore", lore);
        config.addDefault("starter.items.pickaxe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.axe.type", "stone_axe");
        config.addDefault("starter.items.axe.amount", 1);
        config.addDefault("starter.items.axe.name", "&6Stone Axe");
        config.addDefault("starter.items.axe.lore", lore);
        config.addDefault("starter.items.axe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.shovel.type", "stone_shovel");
        config.addDefault("starter.items.shovel.amount", 1);
        config.addDefault("starter.items.shovel.name", "&6Stone Shovel");
        config.addDefault("starter.items.shovel.lore", lore);
        config.addDefault("starter.items.shovel.enchantments.unbreaking", 1);
        config.addDefault("starter.items.steak.type", "cooked_beef");
        config.addDefault("starter.items.steak.amount", 16);
        config.addDefault("food.price", 25.0);
        config.addDefault("food.cooldown", 1800);
        config.addDefault("food.items.steak.type", "cooked_beef");
        config.addDefault("food.items.steak.amount", 16);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * reload kits.yml
     * @since many moons ago
     */
    public boolean reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
}