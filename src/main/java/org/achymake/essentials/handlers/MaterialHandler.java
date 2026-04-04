package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MaterialHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    public PersistentDataContainer getData(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        if (meta != null) {
            return meta.getPersistentDataContainer();
        } else return null;
    }
    /**
     * get material
     * @param materialName string
     * @return material
     * @since many moons ago
     */
    public Material get(String materialName) {
        if (materialName.contains("minecraft:")) {
            return Material.getMaterial(materialName.replace("minecraft:", "").toUpperCase());
        } else return Material.getMaterial(materialName.toUpperCase());
    }
    /**
     * get enchantment
     * @param enchantmentName string
     * @return enchantment
     * @since many moons ago
     */
    public Enchantment getEnchantment(String enchantmentName) {
        return Enchantment.getByName(enchantmentName.toUpperCase());
    }
    public boolean isEnchantment(String enchantmentName) {
        return getEnchantment(enchantmentName) != null;
    }
    /**
     * set enchantment
     * @param itemStack itemStack
     * @param enchantmentName string
     * @param level integer
     * @since many moons ago
     */
    public void setEnchantment(ItemStack itemStack, String enchantmentName, int level) {
        if (!isEnchantment(enchantmentName))return;
        if (level > 0) {
            itemStack.addUnsafeEnchantment(getEnchantment(enchantmentName), level);
        } else itemStack.removeEnchantment(getEnchantment(enchantmentName));
    }
    /**
     * has enchantment
     * @param itemStack itemStack
     * @param enchantmentName string
     * @return true if itemStack has enchantmentName else false
     * @since many moons ago
     */
    public boolean hasEnchantment(ItemStack itemStack, String enchantmentName) {
        if (isEnchantment(enchantmentName) && itemStack != null) {
            return itemStack.getItemMeta().hasEnchant(getEnchantment(enchantmentName));
        } else return false;
    }
    /**
     * get enchantments
     * @return list enchantments
     * @since many moons ago
     */
    public List<Enchantment> getEnchantments() {
        return new ArrayList<>(Arrays.asList(Enchantment.values()));
    }
    /**
     * get new itemStack
     * @param materialName string
     * @param amount integer
     * @return itemStack if materialName is null returns null
     * @since many moons ago
     */
    public ItemStack getItemStack(String materialName, int amount) {
        var material = get(materialName);
        if (material != null) {
            return new ItemStack(material, amount);
        } else return null;
    }
    /**
     * get copy of itemStack
     * @param itemStack itemStack
     * @return itemStack copy of itemStack if itemStack is null returns null
     * @since many moons ago
     */
    public ItemStack getItemStack(ItemStack itemStack, int amount) {
        if (itemStack != null) {
            var copy = new ItemStack(itemStack);
            copy.setAmount(amount);
            return copy;
        } else return null;
    }
    /**
     * give itemStack
     * @param player target
     * @param itemStack itemStack
     * @since many moons ago
     */
    public void giveItemStack(Player player, ItemStack itemStack) {
        if (itemStack == null)return;
        var rest = player.getInventory().addItem(itemStack).values();
        if (rest.isEmpty())return;
        var location = player.getLocation();
        rest.forEach(itemStacks -> getWorldHandler().dropItemStack(location, itemStack));
    }
    /**
     * give itemStack
     * @param player target
     * @param itemStacks collection itemStacks
     * @since many moons ago
     */
    public void giveItemStacks(Player player, Collection<ItemStack> itemStacks) {
        itemStacks.forEach(itemStack -> giveItemStack(player, itemStack));
    }
    /**
     * get player head
     * @param offlinePlayer or player
     * @param amount integer
     * @since many moons ago
     */
    public ItemStack getPlayerHead(OfflinePlayer offlinePlayer, int amount) {
        var itemStack = getItemStack("player_head", amount);
        var skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    /**
     * repair itemStack
     * @param itemStack itemStack
     * @return true if itemStack gets repaired else false
     * @since many moons ago
     */
    public boolean repair(ItemStack itemStack) {
        var damageable = (Damageable) itemStack.getItemMeta();
        if (damageable.hasDamage()) {
            damageable.setDamage(0);
            itemStack.setItemMeta(damageable);
            return true;
        } else return false;
    }
    /**
     * is air
     * @param itemStack itemStack
     * @return true if itemStack is null or air else false
     * @since many moons ago
     */
    public boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(get("air"));
    }
    public ItemStack getSpawner(String entityType, int amount) {
        var itemStack = getItemStack("spawner", amount);
        var itemMeta = itemStack.getItemMeta();
        if (getConfig().isList("spawner.lore")) {
            var listed = new ArrayList<String>();
            for(var string : getConfig().getStringList("spawner.lore")) {
                listed.add(getMessage().addColor(string));
            }
            itemMeta.setLore(listed);
        }
        var itemDisplayName = getConfig().getString("spawner.display");
        if (itemDisplayName != null) {
            itemMeta.setDisplayName(getMessage().addColor(itemDisplayName.replace("%entity_type%", getMessage().toTitleCase(entityType))));
        }
        itemMeta.getPersistentDataContainer().set(getInstance().getKey("entity_type"), PersistentDataType.STRING, entityType.toUpperCase());
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public BlockData disableTorch(BlockData blockData) {
        if (blockData instanceof RedstoneWallTorch redstoneWallTorch) {
            redstoneWallTorch.setLit(false);
        } else if (blockData instanceof Lightable lightable) {
            lightable.setLit(false);
        }
        return blockData;
    }
    public boolean isRedstone(Material material) {
        return material.equals(get("redstone")) || material.equals(get("redstone_wire"))
                || material.equals(get("redstone_torch")) || material.equals(get("redstone_wall_torch"))
                || material.equals(get("repeater")) || material.equals(get("comparator"))
                || material.equals(get("redstone_block")) || material.equals(get("observer"));
    }
}