package dev.favn.voidstorage.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FormVoidMenu {
    public FormVoidMenu(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        int InventorySize = 45;

        Inventory gui = Bukkit.createInventory(p, InventorySize, "Form Void");

        ItemStack blank = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName(ChatColor.AQUA + "");
        blank.setItemMeta(blankMeta);

        for (int i = 0; i < InventorySize; i++) {
            gui.setItem(i, blank);
        }

        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName("§2Confirm");
        confirm.setItemMeta(confirmMeta);
        gui.setItem(25, confirm);

        ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName("§4Cancel");
        cancel.setItemMeta(cancelMeta);
        gui.setItem(19, cancel);

        gui.setItem(22, null);

        p.openInventory(gui);

    }
}
