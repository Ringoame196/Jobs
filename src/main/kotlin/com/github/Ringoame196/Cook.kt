package com.github.Ringoame196

import com.github.Ringoame196.Data.CookingData
import com.github.Ringoame196.Entity.ArmorStand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.Barrel
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
    fun bake(plugin: Plugin, player: Player, entity: ItemFrame, smoker: Smoker) {
        var c = 0
        val level = Scoreboard().getValue("cookingLevel", player.uniqueId.toString())
        val completeTime = 10 - (level * 2)
        if (entity.isVisible) { entity.isVisible = false }
        val armorStand = ArmorStand().summon(entity.location, "")
        val world = entity.world
        object : BukkitRunnable() {
            override fun run() {
                val item = entity.item
                if (Food().isExpirationDate(player, entity.item)) {
                    this.cancel()
                    armorStand.remove()
                    return
                }
                c++
                world.playSound(entity.location, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f)
                smoker.burnTime = 40
                armorStand.customName = "${ChatColor.YELLOW}${c}秒"
                smoker.update()
                if (c == completeTime) {
                    val bakeItem = CookingData().bake(item) ?: return
                    if (!isCookLevel(bakeItem.itemMeta?.displayName?:return, player)) {
                        return
                    }
                    entity.setItem(bakeItem)
                    world.playSound(entity.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
                } else if (c == (completeTime * 2) || item.type == Material.AIR) {
                    if (item.type != Material.AIR) {
                        entity.setItem(ItemStack(Material.AIR))
                        world.playSound(entity.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                    }
                    armorStand.remove()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20) // 1秒間隔 (20 ticks) でタスクを実行
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
        if (Food().isExpirationDate(player, entity.item)) { return }
        val cutItem = CookingData().cut(item) ?: return
        if (!isCookLevel(cutItem.itemMeta?.displayName?:return, player)) {
            return
        }
        player.inventory.addItem(cutItem)
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
    fun dressing(player: Player, entity: ItemFrame) {
        val item = player.inventory.itemInMainHand
        val playerItem = player.inventory.itemInMainHand
        if (Food().isExpirationDate(player, entity.item)) { return }
        playerItem.amount = playerItem.amount - 1
        val dressingItem = CookingData().dressing(item) ?: return
        if (!isCookLevel(dressingItem.itemMeta?.displayName?:return, player)) {
            return
        }
        player.inventory.setItemInMainHand(playerItem)
        player.inventory.addItem(dressingItem)
        entity.world.spawnParticle(Particle.EXPLOSION_HUGE, entity.location.add(0.0, 1.0, 0.0), 1)
        player.world.playSound(player.location, Sound.ITEM_BUCKET_EMPTY, 1f, 1f)
    }
    fun pot(block: Block, player: Player, plugin: Plugin) {
        val barrel = block.state as Barrel
        if (barrel.customName != null) { return }
        val ingredients = mutableListOf<String>()
        for (item in barrel.inventory) {
            item ?: continue
            ingredients.add(item.itemMeta?.displayName ?: continue)
            if (Food().isExpirationDate(player, item)) { return }
        }
        val food = CookingData().pot(ingredients) ?: return
        if (!isCookLevel(food.itemMeta?.displayName?:return, player)) {
            return
        }
        for (item in barrel.inventory) {
            item ?: continue
            item.amount = item.amount - 1
        }
        posCooking(plugin, block, food, player)
    }
    fun mix(player: Player, barrel: Barrel) {
        if (Random.nextInt(0, 8) != 0) { return }
        val recipe = mutableListOf<String>()
        var expiration = false
        for (i in 0 until barrel.inventory.size) {
            val item = barrel.inventory.getItem(i) ?: continue
            recipe.add(item.itemMeta?.displayName ?: continue)
            if (Food().isExpirationDate(player, item)) {
                expiration = true
            }
        }
        val food = if (expiration) { CookingData().fermentationMix(recipe) } else { CookingData().mix(recipe) } ?: return
        if (!isCookLevel(food.itemMeta?.displayName?:return, player)) {
            return
        }
        barrel.world.dropItem(barrel.location.clone().add(0.5, 1.5, 0.5), food)
        for (i in 0 until barrel.inventory.size) {
            val item = barrel.inventory.getItem(i) ?: continue
            item.amount = barrel.inventory.getItem(i)!!.amount - 1
        }
        player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
    }
    fun fry(player: Player, block: Block, item: ItemStack, plugin: Plugin) {
        if (Food().isExpirationDate(player, item)) { return }
        val armorStand = ArmorStand().summon(block.location.clone().add(0.5, -0.2, 0.5), " ")
        val timer = ArmorStand().summon(block.location.clone().add(0.5, 1.0, 0.5), " ")
        armorStand.equipment?.helmet = item
        val level = Scoreboard().getValue("cookingLevel", player.uniqueId.toString())
        var c = 15 - (level * 2)
        object : BukkitRunnable() {
            override fun run() {
                if (block.location.block.type != Material.LAVA_CAULDRON) {
                    armorStand.remove()
                    timer.remove()
                    this.cancel()
                }
                c--
                timer.customName = "${ChatColor.YELLOW}${c}秒"
                block.world.playSound(block.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
                if (c == 0) {
                    Item().drop(block.location.clone().add(0.5, 1.0, 0.5), CookingData().fly(item) ?: return)
                    armorStand.remove()
                    timer.remove()
                    block.world.playSound(block.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L) // 1秒間隔 (20 ticks) でタスクを実行
    }
    private fun posCooking(plugin: Plugin, block: Block, item: ItemStack, player: Player) {
        val armorStand = ArmorStand().summon(block.location.clone().add(0.5, 1.0, 0.5), "")
        val level = Scoreboard().getValue("cookingLevel", player.uniqueId.toString())
        var c = 30 - (level * 2)
        val barrel = block.state as Barrel
        barrel.customName = "${ChatColor.RED}オープン禁止"
        barrel.update()
        object : BukkitRunnable() {
            override fun run() {
                c--
                armorStand.customName = "${ChatColor.YELLOW}${c}秒"
                block.world.playSound(block.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
                if (block.location.block.type != Material.BARREL || block.location.clone().add(0.0, -1.0, 0.0).block.type != Material.CAMPFIRE) {
                    armorStand.remove()
                    this.cancel()
                }

                if (c == 0) {
                    barrel.customName = null
                    barrel.update()
                    Item().drop(block.location.clone().add(0.5, 1.0, 0.5), item)
                    armorStand.remove()
                    block.world.playSound(block.location, Sound.BLOCK_ANVIL_USE, 1f, 1f)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L) // 1秒間隔 (20 ticks) でタスクを実行
    }
    fun findArmorStandsInRadius(location: Location, radius: Double): List<org.bukkit.entity.ArmorStand> {
        val world: World? = location.world?.name?.let { Bukkit.getWorld(it) }

        val armorStandsInRange = mutableListOf<org.bukkit.entity.ArmorStand>()

        if (world != null) {
            for (entity in world.entities) {
                if (entity is org.bukkit.entity.ArmorStand) {
                    val entityLocation = entity.location
                    val distance = location.distance(entityLocation)

                    if (distance <= radius) {
                        armorStandsInRange.add(entity)
                    }
                }
            }
        }

        return armorStandsInRange
    }
    fun isCookLevel(itemName: String, player: Player): Boolean {
        val level = Scoreboard().getValue("cookingLevel", player.uniqueId.toString())
        val cookLevel = getcookLevel(itemName)
        levelUP(player, itemName)
        return cookLevel <= level
    }
    private fun getcookLevel(itemName: String): Int {
        return when (itemName) {
            else -> 0
        }
    }
    fun levelUP(player: Player, itemName: String) {
        if (!itemName.contains("[完成品]")) { return }
        val cookCount = Database().getInt(player.uniqueId.toString(), "aoringoserver", "cookcount", "count") + 1
        Database().setPlayerPoint(player.uniqueId.toString(), "aoringoserver", "cookcount", "count", cookCount)
        Database().setPlayerPoint(
            player.uniqueId.toString(), "aoringoserver", "cooklevel", "level",
            + when (cookCount) {
                100 -> 1
                1000 -> 2
                10000 -> 3
                else -> return
            }
        )
        Job().titleStar(player)
    }
}
