package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.Plugin

class Recipe {
    fun add(plugin: Plugin) {
        repairKit()
        cuttingBoard(plugin)
        bunMold(plugin)
        knife(Material.STONE_SWORD, Material.STONE, "${ChatColor.GOLD}石", "stoneKnife", plugin)
        knife(Material.IRON_SWORD, Material.IRON_BLOCK, "鉄", "ironKnife", plugin)
        knife(Material.DIAMOND_SWORD, Material.DIAMOND_BLOCK, "${ChatColor.AQUA}ダイヤモンド", "diamondKnife", plugin)
    }
    fun repairKit() {
        val customItem = Item().make(Material.FLINT, "${ChatColor.YELLOW}修理キット", null, 1)
        val recipe = ShapelessRecipe(customItem)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.GOLD_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)

        // レシピをサーバーに登録
        Bukkit.addRecipe(recipe)
    }
    fun cuttingBoard(plugin: Plugin) {
        val customResultItem = Item().make(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "まな板", null, null)
        val customRecipe = ShapedRecipe(NamespacedKey(plugin, "cuttingBoard"), customResultItem)
        customRecipe.shape("AAA", "III", "OOO")
        customRecipe.setIngredient('I', Material.IRON_INGOT)
        customRecipe.setIngredient('O', Material.OAK_PLANKS)

        // レシピをサーバーに登録
        Bukkit.addRecipe(customRecipe)
    }
    fun bunMold(plugin: Plugin) {
        val customResultItem = Item().make(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, "まな板", null, null)
        val customRecipe = ShapedRecipe(NamespacedKey(plugin, "bunMold"), customResultItem)
        customRecipe.shape("AAA", "CIC", "CCC")
        customRecipe.setIngredient('I', Material.IRON_INGOT)
        customRecipe.setIngredient('C', Material.COPPER_INGOT)

        // レシピをサーバーに登録
        Bukkit.addRecipe(customRecipe)
    }
    private fun knife(sword: Material, block: Material, kinds: String, id: String, plugin: Plugin) {
        val item = ItemStack(sword)
        val meta = item.itemMeta
        meta?.setDisplayName("${kinds}の包丁")
        meta?.setCustomModelData(1)
        item.setItemMeta(meta)

        val customRecipe = ShapedRecipe(NamespacedKey(plugin, id), item)
        customRecipe.shape("AAB", "ABA", "SAA")
        customRecipe.setIngredient('B', block)
        customRecipe.setIngredient('S', sword)

        Bukkit.addRecipe(customRecipe)
    }
}
