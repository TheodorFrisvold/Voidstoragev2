package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickWithVoid implements Listener {

    private static VoidStorage _plugin;



    public ClickWithVoid(VoidStorage plugin) {
        _plugin = plugin;
        this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
    }

    @EventHandler
    public static void onClickWithVoid(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("onClickWithVoid");
        ItemStack item = e.getItem();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;
        if(item.getAmount() > 1) {
            e.getPlayer().sendMessage("Can't use stacked voids!");
            return;
        }
        Action action = e.getAction();

        if (action == Action.LEFT_CLICK_AIR ||
                action == Action.LEFT_CLICK_BLOCK) {
            removeFromVoid(item, e);
        }
        if (action == Action.RIGHT_CLICK_AIR) {
            addToVoid(item, e);
        }
    }

    private static void addToVoid(ItemStack storage, PlayerInteractEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        p.sendMessage("Cancelled event in addToVoid method");

        int storedAmount = FormedVoid.getAmount(storage);
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
        int storedAmount = FormedVoid.getAmount(storage);
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

}
