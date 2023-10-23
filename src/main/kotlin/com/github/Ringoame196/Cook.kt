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
                        entity.setItem(Item().make(Material.CHARCOAL, "${ChatColor.BLACK}炭", null, null))
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
        if (!knife(player)) {
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
            return
        }
        player.inventory.addItem(CookingData().cut(item) ?: return)
        entity.setItem(ItemStack(Material.AIR))
        player.world.playSound(player.location, Sound.ENTITY_SHEEP_SHEAR, 1f, 1f)
    }
    private fun knife(player: Player): Boolean {
        val knife = player.inventory.itemInMainHand
        val count = when (knife.type) {
            Material.STONE_SWORD -> 5
            Material.IRON_SWORD -> 3
            Material.DIAMOND_SWORD -> 1
            else -> return false
        }
        return Random.nextInt(0, count) == 0
    }
}
