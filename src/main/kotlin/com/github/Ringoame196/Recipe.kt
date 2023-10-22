package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ShapelessRecipe

class Recipe {
    fun repairKit() {
        val customItem = Item().make(Material.FLINT, "${ChatColor.YELLOW}修理キット", null, 1)
        val recipe = ShapelessRecipe(customItem)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.GOLD_INGOT)

        // レシピをサーバーに登録
        Bukkit.addRecipe(recipe)
    }
}
