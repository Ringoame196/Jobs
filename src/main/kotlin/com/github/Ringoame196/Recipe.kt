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
        bunMold()
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
    fun bunMold() {
        val resultItem = Item().make(Material.PAPER, "${ChatColor.GOLD}バンズの型", null, 2) // 作成したいアイテム

        // 2. レシピの登録
        val recipe = ShapelessRecipe(resultItem)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)
        recipe.addIngredient(1, Material.COPPER_INGOT)

        // レシピをサーバーに登録
        Bukkit.addRecipe(recipe)
    }
    fun stoneKnife() {
        val resultItem = knife(Material.STONE_SWORD, "石")
        val recipe = ShapelessRecipe(resultItem ?: return)
        recipe.addIngredient(1, Material.STICK)
        recipe.addIngredient(1, Material.STONE)
        recipe.addIngredient(1, Material.STONE)

        Bukkit.addRecipe(recipe)
    }
    fun ironKnife() {
        val resultItem = knife(Material.IRON_SWORD, "${ChatColor.YELLOW}鉄")
        val recipe = ShapelessRecipe(resultItem ?: return)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.IRON_BLOCK)
        recipe.addIngredient(1, Material.IRON_BLOCK)

        Bukkit.addRecipe(recipe)
    }
    fun diamondKnife() {
        val resultItem = knife(Material.DIAMOND_SWORD, "${ChatColor.AQUA}ダイヤモンド")
        val recipe = ShapelessRecipe(resultItem ?: return)
        recipe.addIngredient(1, Material.IRON_INGOT)
        recipe.addIngredient(1, Material.DIAMOND_BLOCK)
        recipe.addIngredient(1, Material.DIAMOND_BLOCK)

        Bukkit.addRecipe(recipe)
    }
    private fun knife(material: Material, kinds: String): ItemStack? {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName("${kinds}の包丁")
        meta?.setCustomModelData(1)
        item.setItemMeta(meta)
        return item
    }
}
