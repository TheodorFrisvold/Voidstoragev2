package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceWithVoid implements Listener {

    private static VoidStorage _plugin;
    private static KeyCache _keyCache;
    private static NamespacedKey key;
    private static NamespacedKey amountKey;
    private static NamespacedKey maxKey;


    public PlaceWithVoid(VoidStorage plugin, KeyCache keyCache){
        _plugin = plugin;
        _keyCache = keyCache;
        key = _keyCache.getKey("formedvoid_type");
        amountKey = _keyCache.getKey("formedvoid_amount");
        maxKey = _keyCache.getKey("formedvoid_max");
        this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
    }

    @EventHandler
    public static void onBlockPlaceWithVoid(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        placeBlock(item, e);
    }

    @EventHandler
    public static void onBlockMultiPlaceWithVoid(BlockMultiPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        placeMultiBlock(item, e);
    }



    private static void placeMultiBlock(ItemStack item, BlockMultiPlaceEvent e) {
        e.setCancelled(true);

    }

    private static void placeBlock(ItemStack storage, BlockPlaceEvent e){
        e.setCancelled(true);
        Player p = e.getPlayer();
    }

    private static void applyBoneMealFromVoid(ItemStack storage, BlockPlaceEvent e) {
        e.setCancelled(true);
    }

//    private static void oldPlaceBlock(ItemStack itemClicked, BlockPlaceEvent e) {
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
//            ClickWithVoid.addToVoid(itemClicked, e);
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
}
