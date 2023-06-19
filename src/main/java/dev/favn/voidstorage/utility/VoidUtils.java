package dev.favn.voidstorage.utility;

import org.bukkit.Material;

public class VoidUtils {

    public static String displayNameFromMaterial(Material material) {
        StringBuilder itemNameBuilder = new StringBuilder();
        String[] splitString = material.name().toLowerCase().split("_");
        for (String s : splitString) {
            itemNameBuilder.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1))
                    .append(" ");
        }
        return itemNameBuilder.toString();
    }







}
