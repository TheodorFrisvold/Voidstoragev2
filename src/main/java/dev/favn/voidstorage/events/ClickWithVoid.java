package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

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
        this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
    }

    @EventHandler
    public static void onClickWithVoid(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("onClickWithVoid");
        ItemStack item = e.getItem();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        Action action = e.getAction();

        if (action == Action.LEFT_CLICK_AIR ||
                action == Action.LEFT_CLICK_BLOCK) {
            removeFromVoid(item, e);
            return;
        }
        if (action == Action.RIGHT_CLICK_AIR) {
            addToVoid(item, e);
            return;
        }
    }

    private static void addToVoid(ItemStack storage, PlayerInteractEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();

        int storedAmount = storage.getItemMeta().getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);
        int depositedAmount = 0;
        int total = storedAmount + depositedAmount;

        for (ItemStack item : p.getInventory().getStorageContents()) {
            if (item == null || item.getType() != storage.getType() || FormedVoid.isItemVoid(item)) continue;

            if (total + item.getAmount() <= 10000) {
                depositedAmount += item.getAmount();
                item.setAmount(0);

                if (!p.isSneaking()) break;

                continue;
            }

            int overflow = total + item.getAmount() - 10000;
            depositedAmount += item.getAmount() - overflow;
            item.setAmount(overflow);
            break;
        }

        FormedVoid.updateVoid(storage, storedAmount + depositedAmount);
    }

    private static void removeFromVoid(ItemStack storage, PlayerInteractEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        int storedAmount = storage.getItemMeta().getPersistentDataContainer().get(amountKey, PersistentDataType.INTEGER);
        int newAmount = storedAmount;
        Material m = storage.getType();

        for (ItemStack item : p.getInventory().getStorageContents()) {
            if (!(item == null) || FormedVoid.isItemVoid(item)) continue;
            if (m.getMaxStackSize() > storedAmount) {
                p.getInventory().addItem(new ItemStack(m, storedAmount));
                newAmount = 0;
                break;
            }
            if (newAmount - m.getMaxStackSize() < 0) {
                p.getInventory().addItem(new ItemStack(m, newAmount));
                newAmount = 0;
                break;
            }
            p.getInventory().addItem((new ItemStack(m, m.getMaxStackSize())));
            newAmount -= m.getMaxStackSize();
            if (!p.isSneaking()) {
                break;
            }
            continue;
        }
        FormedVoid.updateVoid(storage, newAmount);
    }

//    private static void placeBlock(ItemStack itemClicked, PlayerInteractEvent e) {
//        e.setCancelled(true);
//
//        Player p = e.getPlayer();
//        int amount;
//        int voidAmount = FormedVoid.getAmount(itemClicked);
//        if (voidAmount > 10000 || voidAmount < 0) return;
//        amount = voidAmount;
//
//        Block blockToBePlaced = e.getClickedBlock().getRelative(e.getBlockFace());
//
//        if (blockToBePlaced.getType() != Material.AIR) {
//            addToVoid(itemClicked, e);
//            return;
//        }
//
//        if (getAirBoundingBox(blockToBePlaced).overlaps(p.getBoundingBox())) {
//            addToVoid(itemClicked, e);
//            return;
//        }
//
//        if (FormedVoid.getAmount(itemClicked) <= 0) {
//            p.sendMessage("Void is empty!");
//            return;
//        }
//
//        blockToBePlaced.setType(itemClicked.getType());
//        FormedVoid.updateVoid(itemClicked, amount - 1);
//    }

    private static BoundingBox getAirBoundingBox(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block parameter in getAirBoundingBox cannot be null!");
        }
        int posX = block.getX();
        int posY = block.getY();
        int posZ = block.getZ();

        return new BoundingBox(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
    }

}
