package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

class Recipe {
    fun add() {
        repairKit()
        cuttingBoard()
        stoneKnife()
        ironKnife()
        diamondKnife()
    }
    fun repairKit() {
        val customItem = Item().make(Material.FLINT, "${ChatColor.YELLOW}修理キット", null, 1)
        val recipe = ShapelessRecipe(customItem)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.GOLD_INGOT)

        // レシピをサーバーに登録
        Bukkit.addRecipe(recipe)
    }
    fun cuttingBoard() {
        val resultItem = Item().make(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "まな板", null, null) // 作成したいアイテム

        // 2. レシピの登録
        val recipe = ShapelessRecipe(resultItem)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.OAK_PLANKS)
        recipe.addIngredient(1, Material.OAK_PLANKS)

        // レシピをサーバーに登録
        Bukkit.addRecipe(recipe)
    }
    fun stoneKnife() {
        val resultItem = knife(Material.STONE_SWORD)
        val recipe = ShapelessRecipe(resultItem)
        recipe.addIngredient(1, Material.STONE_SWORD)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)

        Bukkit.addRecipe(recipe)
    }
    fun ironKnife() {
        val resultItem = knife(Material.IRON_SWORD)
        val recipe = ShapelessRecipe(resultItem)
        recipe.addIngredient(1, Material.IRON_SWORD)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)

        Bukkit.addRecipe(recipe)
    }
    fun diamondKnife() {
        val resultItem = knife(Material.DIAMOND_SWORD)
        val recipe = ShapelessRecipe(resultItem)
        recipe.addIngredient(1, Material.DIAMOND_SWORD)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)

        Bukkit.addRecipe(recipe)
    }
    private fun knife(material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName("${ChatColor.YELLOW}包丁")
        meta?.setCustomModelData(1)
        item.setItemMeta(meta)
        item.durability = 0
        return item
    }
}
