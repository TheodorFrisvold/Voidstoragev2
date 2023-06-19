package dev.favn.voidstorage.itemfactory;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class UnformedVoid {
    private static final KeyCache _keyCache = VoidStorage.getKeyCache();

    public static ItemStack GetItem() {

        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);

        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "VoidStorage error: ItemMeta of ItemStack in UnformedVoid.GetItem is null!");
        }
        meta.setDisplayName(ChatColor.RESET + "§bUnformed Void Storage");
        List<String> lore = new ArrayList<>();
        lore.add("A storage void that can takes the");
        lore.add("shape of the provided item.");
        lore.add("Right-click to form void.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey key = _keyCache.getKey("unformedvoid_data");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "UnformedVoid");

        item.setItemMeta(meta);

        return item;
    }
}
