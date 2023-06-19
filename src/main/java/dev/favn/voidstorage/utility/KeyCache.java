package dev.favn.voidstorage.utility;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class KeyCache {

    private final JavaPlugin plugin;

    private static Map<String, NamespacedKey> keyMap = new HashMap<>();

    public KeyCache(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey getKey(String key) {
        return this.keyMap.computeIfAbsent(key, k -> new NamespacedKey(plugin, k));
    }

}
