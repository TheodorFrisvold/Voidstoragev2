package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class UseVoid implements Listener {

    private static VoidStorage _plugin;
    private static KeyCache _keyCache;
    private static NamespacedKey key;
    private static NamespacedKey amountKey;
    private static NamespacedKey maxKey;


    public UseVoid(VoidStorage plugin, KeyCache keyCache){
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
        if (item.getAmount()>1) {
            e.getPlayer().sendMessage("You can't use voids that are stacked");
            e.setCancelled(true);
            return;
        }
        placeBlock(item, e);
    }

    @EventHandler
    public void onBlockMultiPlaceWithVoid(BlockMultiPlaceEvent e) {
        e.getPlayer().sendMessage("MultiPlace Event");
        ItemStack item = e.getItemInHand();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        if (item.getAmount()>1) {
            e.getPlayer().sendMessage("You can't use voids that are stacked");
            e.setCancelled(true);
            return;
        }

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
        if (item.getAmount()>1) {
            e.getPlayer().sendMessage("You can't use voids that are stacked");
            e.setCancelled(true);
            return;

        }
        applyBoneMealFromVoid(item, e);
    }





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

        Block block = e.getBlockPlaced().getLocation().getBlock();
        block.setType(storage.getType());
        FormedVoid.updateVoid(storage, amount - 1);
    }

    private void applyBoneMealFromVoid(ItemStack storage, BlockFertilizeEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("applyBoneMealFromVoid");
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;
        if (FormedVoid.getAmount(storage) <= 0){
            p.sendMessage("Void is empty!");
            return;
        }

        FormedVoid.updateVoid(storage, amount - 1);


    }
}