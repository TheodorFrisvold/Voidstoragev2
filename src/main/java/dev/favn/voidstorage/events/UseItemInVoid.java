package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class UseItemInVoid implements Listener {

    private static VoidStorage _plugin;
    private static KeyCache _keyCache;
    private static NamespacedKey key;
    private static NamespacedKey amountKey;
    private static NamespacedKey maxKey;


    public UseItemInVoid(VoidStorage plugin, KeyCache keyCache) {
        _plugin = plugin;
        _keyCache = keyCache;
        key = _keyCache.getKey("formedvoid_type");
        amountKey = _keyCache.getKey("formedvoid_amount");
        maxKey = _keyCache.getKey("formedvoid_max");
        this._plugin.getServer().getPluginManager().registerEvents(this, this._plugin);
    }



    @EventHandler
    public static void onPlayerInteractWithVoid(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item == null) return;
        if (!FormedVoid.isItemVoid(item)) return;

    }

}
