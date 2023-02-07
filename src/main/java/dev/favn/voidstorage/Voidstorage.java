package dev.favn.voidstorage;

import org.bukkit.plugin.java.JavaPlugin;

public final class Voidstorage extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getServer().getConsoleSender().sendMessage("§2VoidStorage enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getServer().getConsoleSender().sendMessage("§2VoidStorage enabled.");
    }
}
