package dev.favn.voidstorage.menus;

import dev.favn.voidstorage.VoidStorage;
import dev.favn.voidstorage.itemfactory.FormedVoid;
import dev.favn.voidstorage.utility.KeyCache;
import dev.favn.voidstorage.utility.VoidUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class FormVoidMenuHandler implements Listener {
    private int returnValue;
    private final KeyCache _keyCache;

    public FormVoidMenuHandler(VoidStorage plugin, KeyCache keyCache) {
        this._keyCache = keyCache;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!(e.getView().getTitle().equals("Form Void"))) return;
        String clickedItem;

        if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
            return;
        }

        if (e.getClick() == ClickType.SWAP_OFFHAND || e.getAction() == InventoryAction.HOTBAR_SWAP) {
            e.setCancelled(true);
            return;
        }

        if (e.getSlot() == 22 && e.getClickedInventory().getType() == InventoryType.CHEST) {
            e.setCancelled(true);
            if (e.getCursor().getItemMeta() instanceof PotionMeta) return;
            e.getInventory().setItem(22, new ItemStack(e.getCursor().getType(), 1));
        }

        try {
            clickedItem = e.getCurrentItem().getItemMeta().getDisplayName();
        } catch (NullPointerException exception) {
            return;
        }

        if (clickedItem.equals(ChatColor.AQUA + "")) e.setCancelled(true);

        if (clickedItem.equals("§4Cancel")) {
            e.setCancelled(true);
            p.closeInventory();
            p.sendMessage("Cancelled forming void");
        }

        if (clickedItem.equals("§2Confirm")) {
            e.setCancelled(true);
            if (e.getInventory().getItem(22) != null) {
                FormVoid(e);
            }
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent e) {
        if (!(e.getView().getTitle().equals("Form Void"))) return;
        e.setCancelled(true);
    }

    private void FormVoid(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Material m = e.getInventory().getItem(22).getType();//Slot 22 is the center slot in the GUI


        p.getInventory().addItem(FormedVoid.createVoid(m, 0));
        DetectUnformedVoidIndex(e);
        int oldStackSize = p.getInventory().getItem(returnValue).getAmount();
        p.getInventory().getItem(returnValue).setAmount(oldStackSize - 1);
        p.sendMessage("Successfully formed a " + ChatColor.AQUA + VoidUtils.displayNameFromMaterial(m) + "Void ");
        p.closeInventory();
    }

    private void DetectUnformedVoidIndex(InventoryClickEvent e) {
        NamespacedKey key = _keyCache.getKey("unformedvoid_data");

        Player p = (Player) e.getWhoClicked();
        ItemStack[] inventory = p.getInventory().getContents();

        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        ItemMeta handMeta = itemInHand.getItemMeta();

        if (handMeta != null) {
            String handData = handMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (Objects.equals(handData, "UnformedVoid")) {
                returnValue = p.getInventory().getHeldItemSlot();
                return;
            }
        }

        for (int i = 0; i < inventory.length; i++) {
            ItemStack curItem = inventory[i];
            if (curItem == null) {
                continue;
            }
            ItemMeta curItemMeta = curItem.getItemMeta();
            String curItemData = curItemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (Objects.equals(curItemData, "UnformedVoid")) {
                returnValue = i;
                break;
            }
        }
    }

}
