package dev.favn.voidstorage.itemfactory;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.utility.KeyCache;
import dev.favn.voidstorage.utility.VoidUtils;
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
import java.util.Objects;

public class FormedVoid {
    private static final KeyCache _keyCache = VoidStorage.getKeyCache();
    private static final NamespacedKey key = _keyCache.getKey("formedvoid_type");
    private static final NamespacedKey amountKey = _keyCache.getKey("formedvoid_amount");
    private static final NamespacedKey maxKey = _keyCache.getKey("formedvoid_max");

    private static final int maxAmount = 10000;
    //TODO: Add additional data to unformed voids depending on the recipe, changing how much they can store.

    public static ItemStack createVoid(Material material, int amount) {

        ItemStack item = new ItemStack(material, 1);

        ItemMeta meta = item.getItemMeta();

        List<String> lore = createLore(material, amount, meta);
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "normal");
        meta.getPersistentDataContainer().set(amountKey, PersistentDataType.INTEGER, amount);
        meta.getPersistentDataContainer().set(maxKey, PersistentDataType.INTEGER, maxAmount);

        item.setItemMeta(meta);
        return item;
    }


    public static void updateVoid(ItemStack voidStorage, int amount){
        if (amount == -10001) return;
        ItemMeta meta = voidStorage.getItemMeta();
        meta.getPersistentDataContainer().set(amountKey, PersistentDataType.INTEGER, amount);
        List<String> lore = createLore(voidStorage.getType(), amount, meta);
        meta.setLore(lore);

        voidStorage.setItemMeta(meta);
    }


    private static List<String> createLore(Material material, int amount, ItemMeta meta) {
        String itemName = VoidUtils.displayNameFromMaterial(material);
        meta.setDisplayName(ChatColor.RESET + itemName + "Void");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RESET + "Void Storage: " + itemName);
        lore.add(ChatColor.RESET + "Stored: " + amount + "/" + maxAmount);
        return lore;
    }

    public static int getAmount(ItemStack storageVoid) {
        int amount;
        if (!isItemVoid(storageVoid)) return -10001;

        ItemMeta meta = storageVoid.getItemMeta();

        amount = meta.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);

        return amount;
    }

    public static boolean isItemVoid(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        try {
            String itemData = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (Objects.equals(itemData, "normal")) return true;
        } catch (NullPointerException exception) {
            VoidStorage.get_plugin().getServer().getConsoleSender().sendMessage(ChatColor.RED + "There was a NullPointerException in isItemVoid method");
        }
        return false;
    }


    public static int getMax(ItemStack storageVoid) {
        int max = 0;
        if (!isItemVoid(storageVoid)) return -10001;

        ItemMeta meta = storageVoid.getItemMeta();

        max = meta.getPersistentDataContainer().get(maxKey, PersistentDataType.INTEGER);

        return max;
    }
}
