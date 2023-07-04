package dev.favn.voidstorage.events;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.menus.FormVoidMenu;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class RightClickUnformedVoid implements Listener {

    private final KeyCache _keyCache;

    public RightClickUnformedVoid(VoidStorage plugin, KeyCache keyCache){
        this._keyCache = keyCache;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if (e.getItem() == null) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String itemData = meta.getPersistentDataContainer().get(_keyCache.getKey("unformedvoid_data"), PersistentDataType.STRING);
        if (Objects.equals(itemData, "UnformedVoid" )){
            new FormVoidMenu(e);
        }
    }
}
