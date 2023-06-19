package dev.favn.voidstorage.recipes;

import dev.favn.voidstorage.itemfactory.UnformedVoid;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class UnformedVoidStorage {

    public static void init(){
        UnformedVoid();
    }

    private static void UnformedVoid() {

        ItemStack item = UnformedVoid.GetItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("unformedvoid"), item);

        recipe.shape(   "GSG",
                        "SES",
                        "GSG");

        recipe.setIngredient('G', Material.GHAST_TEAR);
        recipe.setIngredient('E', Material.DRAGON_EGG);
        recipe.setIngredient('S', Material.NETHER_STAR);

        Bukkit.addRecipe(recipe);
    }
}
