package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.itemupdater.UpdateVoidStorage;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ClickWithVoid implements Listener {

    private static VoidStorage _plugin;
    private static KeyCache _keyCache;
    private static NamespacedKey key;
    private static NamespacedKey amountKey;
    private static NamespacedKey maxKey;


    public ClickWithVoid(VoidStorage plugin, KeyCache keyCache) {
        _plugin = plugin;
        _keyCache = keyCache;
        key = _keyCache.getKey("formedvoid_type");
        amountKey = _keyCache.getKey("formedvoid_amount");
        maxKey = _keyCache.getKey("formedvoid_max");
    }

    @EventHandler
    public static void onClickWithVoid(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) return;

        ItemStack itemClicked = e.getPlayer().getInventory().getItemInMainHand();

        if (!FormedVoid.isItemVoid(itemClicked)) return;

        if (e.getAction() == Action.LEFT_CLICK_AIR ||
            e.getAction() == Action.LEFT_CLICK_BLOCK) RemoveFromVoid(itemClicked, e);
        if (e.getAction() == Action.RIGHT_CLICK_AIR ||
            e.getAction() == Action.RIGHT_CLICK_BLOCK) AddToVoid(itemClicked, e);
    }
    private static void AddToVoid(ItemStack storageVoid, PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (p.isSneaking()){
            int i = getItemAmount(false, p, storageVoid.getType(), e);
            if (i == -10001) return;
            ModifyVoid(e, storageVoid, i);
            return;
        } else {
            ModifyVoid(e, storageVoid, 64);
        }
        return;
    }

    private static void RemoveFromVoid(ItemStack storageVoid, PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (p.isSneaking()){
            int i = getItemAmount(false, p, storageVoid.getType(), e);
            if (i == -10001) return;
            ModifyVoid(e, storageVoid, -i);
            return;
        } else {
            ModifyVoid(e, storageVoid, -64);
        }
        return;
    }

    private static void ModifyVoid(PlayerInteractEvent e, ItemStack storageVoid, int amount){
        int newAmount;
        int maxAmount = FormedVoid.getMax(storageVoid);
        newAmount = FormedVoid.getAmount(storageVoid)  + amount;
        if (newAmount > maxAmount) {
            //do stuff
            newAmount = maxAmount;

        }
        UpdateVoidStorage.updateVoid(storageVoid, newAmount);
    }

    private static void ModifyInventory(boolean remove, PlayerInteractEvent e, int amount){
        if (remove) {

        }
        else {

        }
    }


    private static int getItemAmount(boolean leftClick, Player p, Material m, PlayerInteractEvent e) {
        int amount = 0;

        if (leftClick) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null) {
                    amount += m.getMaxStackSize();
                    continue;
                }
                if (item.getType() == m) {
                    if (item.getType() == m && FormedVoid.isItemVoid(item)) {
                        amount += m.getMaxStackSize() - item.getAmount();
                        continue;
                    }
                }
            }
            return amount;
        }

        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType() == m && !FormedVoid.isItemVoid(item)) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
