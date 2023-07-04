package dev.favn.voidstorage;

import dev.favn.voidstorage.events.ClickWithVoid;
import dev.favn.voidstorage.events.RightClickUnformedVoid;
import dev.favn.voidstorage.menus.FormVoidMenuHandler;
import dev.favn.voidstorage.recipes.UnformedVoidStorage;
import dev.favn.voidstorage.utility.KeyCache;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidStorage extends JavaPlugin {

    private static KeyCache keyCache;
    public static KeyCache getKeyCache(){ return keyCache;}
    private static VoidStorage _plugin;

    public static VoidStorage get_plugin() {
        return _plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        _plugin = this;
        keyCache = new KeyCache(this);
        UnformedVoidStorage.init();
        new FormVoidMenuHandler(this, keyCache);
        new RightClickUnformedVoid(this, keyCache);
        new ClickWithVoid(this, keyCache);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "VoidStorage enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "VoidStorage enabled.");
    }
}