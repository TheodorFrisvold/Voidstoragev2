package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class UseVoid implements Listener {

    public UseVoid(VoidStorage plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlaceWithVoid(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (!FormedVoid.isItemVoid(item)) return;
        if (item.getAmount()>1) {
            e.getPlayer().sendMessage("You can't use voids that are stacked");
            e.setCancelled(true);
            return;
        }
        placeBlock(item, e);
    }

    @EventHandler
    public void onFertilizeEvent(BlockFertilizeEvent e) {
        Player p = e.getPlayer();
        if (p == null) return;//this could potentially be changed to let bone_meal voids work in dispensers.

        ItemStack mainHand = e.getPlayer().getInventory().getItemInMainHand();
        ItemStack offHand = e.getPlayer().getInventory().getItemInOffHand();
        ItemStack item = (mainHand.getType() == Material.BONE_MEAL) ? mainHand : offHand;
        if (!FormedVoid.isItemVoid(item)) return;
        if (item.getAmount()>1) {
            e.getPlayer().sendMessage("You can't use voids that are stacked");
            e.setCancelled(true);
            return;

        }
        applyBoneMealFromVoid(item, e, p);
    }

    private void placeBlock(ItemStack storage, BlockPlaceEvent e){
        Player p = e.getPlayer();
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;

        if (amount == 0){
            p.sendMessage("Void is empty!");
            e.setCancelled(true);
            return;
        }

        FormedVoid.updateVoid(storage, amount - 1);
    }

    private void applyBoneMealFromVoid(ItemStack storage, BlockFertilizeEvent e, Player p) {
        int amount = FormedVoid.getAmount(storage);
        if (amount > 10000 || amount < 0) return;
        if (FormedVoid.getAmount(storage) <= 0){
            p.sendMessage("Void is empty!");
            e.setCancelled(true);
            return;
        }

        FormedVoid.updateVoid(storage, amount - 1);
    }

}