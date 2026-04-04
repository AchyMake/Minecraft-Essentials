package org.achymake.essentials.handlers;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class PaperHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    public InventoryView openAnvil(Player player) {
        return player.openAnvil(null, true);
    }
    public InventoryView openCartographyTable(Player player) {
        return player.openCartographyTable(null, true);
    }
    public InventoryView openGrindstone(Player player) {
        return player.openGrindstone(null, true);
    }
    public InventoryView openLoom(Player player) {
        return player.openLoom(null, true);
    }
    public InventoryView openSmithingTable(Player player) {
        return player.openSmithingTable(null, true);
    }
    public InventoryView openStonecutter(Player player) {
        return player.openStonecutter(null, true);
    }
    public NumberFormat getBlank() {
        return NumberFormat.blank();
    }
    public ItemStack getCustomHead(String skullName, String key, int amount) {
        var skullItem = getMaterialHandler().getItemStack("player_head", amount);
        var skullMeta = (SkullMeta) skullItem.getItemMeta();
        if (16 >= skullName.length()) {
            var profile = getInstance().getServer().createProfile(UUID.randomUUID(), skullName);
            profile.setProperty(new ProfileProperty("textures", key));
            profile.update();
            skullMeta.setPlayerProfile(profile);
            skullItem.setItemMeta(skullMeta);
        }
        return skullItem;
    }
}