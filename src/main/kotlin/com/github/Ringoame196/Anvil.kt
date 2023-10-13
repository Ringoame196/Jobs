package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class Anvil {
    fun open(player: Player) {
        if (Job().get(player) != "${ChatColor.GRAY}鍛冶屋") {
            Player().errorMessage(player, "金床は鍛冶屋以外使用できません")
            return
        }
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.YELLOW}カスタム金床")
        for (i in 0..8) {
            gui.setItem(i, Item().make(Material.RED_STAINED_GLASS_PANE, " ", null, null))
        }
        gui.setItem(2, Item().make(Material.AIR, " ", null, null))
        gui.setItem(4, Item().make(Material.AIR, " ", null, null))
        gui.setItem(7, Item().make(Material.ANVIL, "${ChatColor.YELLOW}合成", null, null))
        player.openInventory(gui)
    }
    fun click(player: Player, item: ItemStack, e: InventoryClickEvent) {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        when (item.type) {
            Material.RED_STAINED_GLASS_PANE -> e.isCancelled = true
            Material.ANVIL -> {
                e.isCancelled = true
                synthesis(player, player.openInventory.topInventory)
            }
            else -> return
        }
    }
    fun close(gui: InventoryView, player: Player) {
        if (gui.getItem(2) != null) {
            player.inventory.addItem(gui.getItem(2))
        }
        if (gui.getItem(4) != null) {
            player.inventory.addItem(gui.getItem(4))
        }
    }
    private fun synthesis(player: Player, gui: Inventory) {
        val syntheticItems1 = gui.getItem(2) ?: return
        val syntheticItems2 = gui.getItem(4) ?: return
        if (!enchantItemCheck(syntheticItems1.type) || !enchantItemCheck(syntheticItems2.type)) {
            return
        }
        if (syntheticItems2.itemMeta?.displayName != "" && syntheticItems2.itemMeta?.displayName != "${ChatColor.YELLOW}修理キット") {
            Player().errorMessage(player, "右側に名前付きのアイテムを設置することはできません")
            return
        }

        var completedItem = syntheticItems1.clone()
        if (syntheticItems1.type == syntheticItems2.type) {
            completedItem = enchant(syntheticItems2, completedItem)
            completedItem = durability(syntheticItems2, completedItem)
        } else if (syntheticItems2.type == Material.ENCHANTED_BOOK) {
            completedItem = enchantBook(completedItem, syntheticItems2.itemMeta as EnchantmentStorageMeta)
        } else if (syntheticItems2.itemMeta!!.displayName == "${ChatColor.YELLOW}修理キット") {
            if (syntheticItems2.durability.toInt() > syntheticItems2.amount) {
                Player().errorMessage(player, "修理キットが多すぎます")
                return
            }
            completedItem.durability = (completedItem.durability - syntheticItems2.amount).toShort()
        }
        if (overEnchant(completedItem)) {
            Player().errorMessage(player, "オーバーエンチャント")
            return
        }
        complete(player, completedItem, gui)
    }
    private fun overEnchant(item: ItemStack): Boolean {
        var allLevel = 0
        for ((enchant, level) in item.itemMeta?.enchants ?: return false) {
            allLevel += level
            if (allLevel > 10) { return true }
        }
        return false
    }
    private fun complete(player: Player, completedItem: ItemStack, gui: Inventory) {
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
        player.inventory.addItem(completedItem)
        gui.setItem(2, Item().make(Material.AIR, " ", null, null))
        gui.setItem(4, Item().make(Material.AIR, " ", null, null))
    }
    private fun enchant(beforeItem: ItemStack, afterItem: ItemStack): ItemStack {
        for ((enchant, level) in beforeItem.itemMeta?.enchants ?: return afterItem) {
            afterItem.addUnsafeEnchantment(enchant, level)
        }
        return afterItem
    }
    private fun enchantBook(afterItem: ItemStack, meta: EnchantmentStorageMeta): ItemStack {
        for ((enchantment, level) in meta.storedEnchants) {
            afterItem.addUnsafeEnchantment(enchantment, level)
        }
        return afterItem
    }
    private fun durability(beforeItem: ItemStack, afterItem: ItemStack): ItemStack {
        val maxdurability = beforeItem.type.maxDurability
        val durability = (afterItem.durability - (maxdurability - beforeItem.durability)).toShort()
        afterItem.durability = if (durability >= 0) { durability } else { 0 }
        return afterItem
    }
    private fun enchantItemCheck(material: Material): Boolean {
        val allowedItems = mutableListOf(
            "_SWORD",
            "_AXE",
            "_PICKAXE",
            "_SHOVEL",
            "_HOE",
            "SHIELD",
            "ENCHANTED_BOOK",
            "_HELMET",
            "_CHESTPLATE",
            "_LEGGINGS",
            "_BOOTS",
        )
        for (id in allowedItems) {
            if (material.toString().contains(id)) { return true }
        }
        return false
    }
}
