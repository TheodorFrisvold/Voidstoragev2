package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        _plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Registered PlaceWithVoid class properly");
    }

    @EventHandler
    public void onBlockPlaceWithVoid(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        e.getPlayer().sendMessage("onBlockPlaceWithVoid event");
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        placeBlock(item, e);
    }

    @EventHandler
    public void onBlockMultiPlaceWithVoid(BlockMultiPlaceEvent e) {
        e.getPlayer().sendMessage("MultiPlace Event");
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        placeMultiBlock(item, e);

    }
    @EventHandler
    public void onFertilizeEvent(BlockFertilizeEvent e) {
        Player p = e.getPlayer();
        if (p == null) return;
        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        ItemStack offHand = e.getPlayer().getInventory().getItemInOffHand();
        ItemStack item = (mainHand.getType() == Material.BONE_MEAL) ? mainHand : offHand;

        p.sendMessage("After is null?");
        if (!FormedVoid.isItemVoid(item)) return;
        p.sendMessage("After is void?");
        applyBoneMealFromVoid(item, e);
        e.setCancelled(true);
    }

//    @EventHandler
//    public void onStructureGrowEvent(StructureGrowEvent e) {
//        Player p = e.getPlayer();
//        p.sendMessage("onStructureGrowEvent");
//        _plugin.getServer().getConsoleSender().sendMessage("onStructureGrowEvent");
//        e.setCancelled(true);
//    }




//    @EventHandler
//    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
//        Player p = e.getPlayer();
//        p.sendMessage("onPlayerItemConsumeEvent");
//        ItemStack item = p.getItemInUse();
//        p.sendMessage(item.toString());
//        if (item == null) return;
//        p.sendMessage("After \"is null\" check");
//        if (!FormedVoid.isItemVoid(item)) return;
//        p.sendMessage("After \"is void\" check");
//        e.setCancelled(true);
//        p.sendMessage("After event cancelled");
//
//    }





    private void placeMultiBlock(ItemStack item, BlockMultiPlaceEvent e) {

    }

    private void placeBlock(ItemStack storage, BlockPlaceEvent e){
        Player p = e.getPlayer();
        p.sendMessage("placeBlock");
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;

        if (FormedVoid.getAmount(storage) <= 0){
            p.sendMessage("Void is empty!");
            return;
        }

//        Block block = e.getBlockPlaced().getLocation().getBlock();
//        block.setType(storage.getType());
        FormedVoid.updateVoid(storage, amount - 1);
    }

    private void applyBoneMealFromVoid(ItemStack storage, BlockFertilizeEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("applyBoneMealFromVoid");
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;

        List<BlockState> blocksChanged = e.getBlocks();
        for (BlockState block : blocksChanged) {
            p.sendMessage(block.toString());
        }

        if (FormedVoid.getAmount(storage) <= 0){
            p.sendMessage("Void is empty!");
            return;
        }

        FormedVoid.updateVoid(storage, amount - 1);


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
