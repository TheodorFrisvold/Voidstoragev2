package dev.favn.voidstorage.events.PlaceWithVoid;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
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
        _plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Registered PlaceWithVoid class properly");
    }

    @EventHandler
    public void onBlockPlaceWithVoid(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
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
        e.setCancelled(true);
        Player p = e.getPlayer();
        ItemStack item = p.getItemInUse();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;

        applyBoneMealFromVoid(item, e);
    }



    private void placeMultiBlock(ItemStack item, BlockMultiPlaceEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage("Cancelled BlockMultiPlaceEvent");
    }

    private void placeBlock(ItemStack storage, BlockPlaceEvent e){
        e.setCancelled(true);
        Player p = e.getPlayer();
        p.sendMessage("placeBlock");
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) {
            _plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Player " +
                    e.getPlayer().getDisplayName() +
                    " has an ItemVoid with a value out of void limits." + " Amount value is: " + amount);
            return;
        }

        Block blockPlaced = e.getBlockPlaced();
        if (FormedVoid.getAmount(storage) <= 0){
            p.sendMessage("Void is empty!");
            return;
        }
        blockPlaced.setType(storage.getType());
        FormedVoid.updateVoid(storage, amount - 1);
    }

    private void applyBoneMealFromVoid(ItemStack storage, BlockFertilizeEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        p.sendMessage("Fertilize event cancelled");
        Block block = e.getBlock();
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;
        e.getBlock().applyBoneMeal(block.getFace(block));

        p.sendMessage("Tried to place bonemeal");

        //seems to work fine but it will also run the code in "ClickWithVoid" to add items to the void itself, figure out how to avoid this when actually applying bonemeal
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
