package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class InventoryHandler {
    private final HashMap<Player, Inventory> inventories = new HashMap<>();
    public HashMap<Player, Inventory> getInventories() {
        return inventories;
    }
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private PaperHandler getPaperHandler() {
        return getInstance().getPaperHandler();
    }
    private boolean isBukkit() {
        return getInstance().isBukkit();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public Inventory create(int size, String title) {
        return getInstance().getServer().createInventory(null, size, getMessage().addColor(title));
    }
    public InventoryView openAnvil(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openAnvil(player);
        } else return null;
    }
    public InventoryView openCartographyTable(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openCartographyTable(player);
        } else return null;
    }
    public InventoryView openEnchanting(Player player) {
        return player.openEnchanting(null, true);
    }
    public InventoryView openEnderchest(Player player, Player target) {
        return player.openInventory(target.getEnderChest());
    }
    public InventoryView openGrindstone(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openGrindstone(player);
        } else return null;
    }
    public InventoryView openLoom(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openLoom(player);
        } else return null;
    }
    public InventoryView openSmithingTable(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openSmithingTable(player);
        } else return null;
    }
    public InventoryView openStonecutter(Player player) {
        if (!isBukkit()) {
            return getPaperHandler().openStonecutter(player);
        } else return null;
    }
    public InventoryView openWorkbench(Player player) {
        return player.openWorkbench(null, true);
    }
    public boolean setHelmet(PlayerInventory inventory, ItemStack itemStack) {
        if (getMaterialHandler().isAir(inventory.getHelmet())) {
            inventory.setHelmet(getMaterialHandler().getItemStack(itemStack, 1));
            return true;
        } else return false;
    }
}