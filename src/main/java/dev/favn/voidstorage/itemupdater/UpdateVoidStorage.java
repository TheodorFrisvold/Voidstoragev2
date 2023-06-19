package dev.favn.voidstorage.itemupdater;

import dev.favn.voidstorage.VoidStorage;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class UpdateVoidStorage {
    private static final NamespacedKey amountKey = VoidStorage.getKeyCache().getKey("formedvoid_amount");
    private static final NamespacedKey maxKey = VoidStorage.getKeyCache().getKey("formedvoid_max");


    public static void updateVoid(ItemStack StorageVoid, int amount){
        ItemMeta meta = StorageVoid.getItemMeta();
        int maxAmount = meta.getPersistentDataContainer().get(maxKey, PersistentDataType.INTEGER);
        meta.getPersistentDataContainer().set(amountKey, PersistentDataType.INTEGER, amount);
        List<String> lore = meta.getLore();
        String UpdatedAmount = ChatColor.RESET + "Stored: " + getNewStoredAmount(meta, amountKey) + "/" + maxAmount;
        lore.set(1, UpdatedAmount);
        meta.setLore(lore);
        StorageVoid.setItemMeta(meta);
    }

    public static int getNewStoredAmount(ItemMeta StorageVoidMeta, NamespacedKey amountKey){
        int newAmount = 0;
        int storedAmount = StorageVoidMeta.getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);
        newAmount += storedAmount;
        return newAmount;
    }
}
