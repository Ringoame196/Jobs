package com.github.Ringoame196

import com.github.Ringoame196.Data.CookingData
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.Smoker
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Cook {
    fun furnace(block: Block) {
        val itemFrame = block.world.spawn(block.location.clone().add(0.0, 1.0, 0.0), org.bukkit.entity.ItemFrame::class.java)
        itemFrame.isVisible = false
    }
    fun cuttingBoard(block: Block) {
        val itemFrame = block.world.spawn(block.location.clone().add(0.0, 1.0, 0.0), org.bukkit.entity.ItemFrame::class.java)
        itemFrame.customName = "まな板"
    }
    fun bake(plugin: Plugin, entity: ItemFrame, smoker: Smoker, time: Int) {
        var c = 0
        if (entity.isVisible) { entity.isVisible = false }
        val armorStand = com.github.Ringoame196.Entity.ArmorStand().summon(entity.location, "")
        val world = entity.world
        object : BukkitRunnable() {
            override fun run() {
                val item = entity.item
                c++
                world.playSound(entity.location, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f)
                smoker.burnTime = 40
                armorStand.customName = "${ChatColor.YELLOW}${c}秒"
                smoker.update()
                if (c == 10) {
                    val bakeItem = CookingData().bake(item)
                    if (bakeItem != null) {
                        entity.setItem(bakeItem)
                        world.playSound(entity.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
                    }
                } else if (c == 20 || item.type == Material.AIR) {
                    if (item.type != Material.AIR) {
                        entity.setItem(ItemStack(Material.AIR))
                        world.playSound(entity.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                    }
                    armorStand.remove()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, time.toLong()) // 1秒間隔 (20 ticks) でタスクを実行
    }
    fun cut(item: ItemStack, player: Player, entity: ItemFrame) {
        val playerItem = player.inventory.itemInMainHand
        if (playerItem.itemMeta?.customModelData != 1) { return }
        playerItem.durability = (playerItem.durability + 4).toShort()
        player.inventory.setItemInMainHand(playerItem)
        if (!knife(player)) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            if (playerItem.durability >= playerItem.type.maxDurability) {
                player.inventory.setItemInMainHand(ItemStack(Material.AIR))
                player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            }
            return
        }
        player.inventory.addItem(CookingData().cut(item) ?: return)
        entity.setItem(ItemStack(Material.AIR))
        player.world.playSound(player.location, Sound.ENTITY_SHEEP_SHEAR, 1f, 1f)
        if (playerItem.durability >= playerItem.type.maxDurability) {
            player.inventory.setItemInMainHand(ItemStack(Material.AIR))
            player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
        }
    }
    private fun knife(player: Player): Boolean {
        val knife = player.inventory.itemInMainHand
        val sharpness = knife.itemMeta?.lore?.get(0).toString().replace("切れ味:", "").toInt()
        if (sharpness > 10) {
            val chance = Random.nextInt(11, 25)
            if (chance <= sharpness) {
                chance(player)
            }
            return true
        }
        return Random.nextInt(0, (10 - sharpness)) <= 0
    }
    private fun chance(player: Player) {
        val item = player.inventory.itemInMainHand
        item.durability = (item.durability - 1).toShort()
        player.inventory.setItemInMainHand(item)
    }
    fun knifeSharpness(item: ItemStack): ItemStack {
        val max = when (item.type) {
            Material.STONE_SWORD -> 8
            Material.IRON_SWORD -> 13
            Material.DIAMOND_SWORD -> 20
            else -> 0
        }
        val sharpness = Random.nextInt(0, max)
        val meta = item.itemMeta
        meta?.lore = mutableListOf("切れ味:$sharpness")
        item.setItemMeta(meta)
        return item
    }
}
