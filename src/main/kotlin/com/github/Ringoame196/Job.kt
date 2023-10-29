package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Job {
    val job = mutableListOf("無職", "${ChatColor.YELLOW}料理人", "${ChatColor.GOLD}ハンター", "${ChatColor.GRAY}鍛冶屋")
    fun get(player: Player): String {
        return job[Scoreboard().getValue("job", player.uniqueId.toString())]
    }
    fun prefix(player: Player) {
        val displayName = when (get(player)) {
            "${ChatColor.YELLOW}料理人" -> "${ChatColor.DARK_PURPLE}"
            "${ChatColor.GOLD}ハンター" -> "${ChatColor.DARK_RED}"
            "${ChatColor.GRAY}鍛冶屋" -> "${ChatColor.GRAY}"
            else -> ""
        } + player.name
        val prefix = if (player.isOp) {
            "${ChatColor.YELLOW}[運営]"
        } else {
            ""
        }
        player.setDisplayName("${prefix}$displayName@${get(player)}")
        player.setPlayerListName("${prefix}$displayName")
    }
    fun change(player: Player, jobName: String) {
        val item = player.inventory.itemInMainHand
        item.amount = item.amount - 1
        for (i in 0 until job.size) {
            if (job.get(i) != jobName) { continue }
            Scoreboard().remove("job", get(player), 1)
            Scoreboard().set("job", player.uniqueId.toString(), i)
            Scoreboard().add("job", get(player), 1)
            prefix(player)
            player.sendMessage("${jobName}に就職しました")
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            player.world.spawnParticle(Particle.EXPLOSION_HUGE, player.location, 1)
            player.inventory.setItemInMainHand(item)
            player.closeInventory()
        }
    }
    fun selectGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.BLUE}職業選択")
        gui.setItem(2, jobGUI(Material.IRON_SWORD, "${ChatColor.GOLD}ハンター"))
        gui.setItem(4, jobGUI(Material.MILK_BUCKET, "${ChatColor.YELLOW}料理人"))
        gui.setItem(6, jobGUI(Material.ANVIL, "${ChatColor.GRAY}鍛冶屋"))
        player.openInventory(gui)
    }
    private fun jobGUI(material: Material, jobName: String): ItemStack {
        val employmentRate = "${Scoreboard().getValue("job",jobName)}人が就職しています"
        return Item().make(material, jobName, employmentRate, null)
    }
    fun tool(): List<Material> {
        val list = mutableListOf<Material>(
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE,
            Material.IRON_SHOVEL,
            Material.GOLDEN_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.IRON_HOE,
            Material.GOLDEN_HOE,
            Material.DIAMOND_HOE,
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.SHIELD
        )
        return list
    }
    fun giveVegetables(location: Location) {
        val vegetables = mutableListOf(
            Food().item("${ChatColor.GREEN}キュウリ", 1),
            Food().item("${ChatColor.GOLD}キャベツ", 2),
            Food().item("${ChatColor.GOLD}スパイス", 3),
            Food().item("稲", 4),
            Food().item("${ChatColor.DARK_PURPLE}なす", 5),
            Food().item("${ChatColor.GOLD}たまねぎ", 6),
            Food().item("${ChatColor.RED}トマト", 7),
        )
        location.world?.dropItem(location, vegetables[Random.nextInt(0, vegetables.size)])
    }
    fun givefish(player: Player): ItemStack {
        val fish = mutableListOf(
            Food().item("${ChatColor.RED}マグロ", 31),
            Food().item("${ChatColor.GOLD}サーモン", 32),
            Item().make(Material.EXPERIENCE_BOTTLE, "${ChatColor.GREEN}経験値瓶", "", null),
            Item().enchant(Enchantment.LURE, 1)
        )
        if (Job().get(player) == "${ChatColor.GOLD}ハンター") {
            fish.add(Food().item("${ChatColor.RED}タコ", 38))
            fish.add(Food().item("イカ", 35))
            fish.add(Food().item("${ChatColor.AQUA}エビ", 28))
        }
        if (Job().get(player) == "${ChatColor.GOLD}ハンター" || Random.nextInt(0, 10) == 0) {
            return fish.get(Random.nextInt(0, fish.size))
        }
        val rubbish = mutableListOf(
            ItemStack(Material.STRING),
            ItemStack(Material.FEATHER),
            ItemStack(Material.LILY_PAD),
            ItemStack(Material.BONE)
        )
        return rubbish[Random.nextInt(0, rubbish.size)]
    }
    fun enchantGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.RED}エンチャント")
        for (i in 0..7) {
            gui.setItem(i, Item().make(Material.RED_STAINED_GLASS_PANE, " ", null, null))
        }
        gui.setItem(8, Item().make(Material.ENCHANTING_TABLE, "${ChatColor.AQUA}エンチャント", null, null))
        gui.setItem(4, ItemStack(Material.AIR))
        player.openInventory(gui)
    }
    fun enchant(player: Player, gui: InventoryView, plugin: Plugin) {
        val enchantBook = mutableListOf(
            Item().enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1),
            Item().enchant(Enchantment.PROTECTION_FIRE, 1),
            Item().enchant(Enchantment.PROTECTION_FALL, 1),
            Item().enchant(Enchantment.PROTECTION_EXPLOSIONS, 1),
            Item().enchant(Enchantment.PROTECTION_PROJECTILE, 1),
            Item().enchant(Enchantment.WATER_WORKER, 1),
            Item().enchant(Enchantment.THORNS, 1),
            Item().enchant(Enchantment.DEPTH_STRIDER, 1),
            Item().enchant(Enchantment.SOUL_SPEED, 1),
            Item().enchant(Enchantment.BINDING_CURSE, 1),
            Item().enchant(Enchantment.VANISHING_CURSE, 1),
            Item().enchant(Enchantment.DAMAGE_ALL, 1),
            Item().enchant(Enchantment.DAMAGE_ARTHROPODS, 1),
            Item().enchant(Enchantment.DAMAGE_UNDEAD, 1),
            Item().enchant(Enchantment.KNOCKBACK, 1),
            Item().enchant(Enchantment.PROTECTION_FIRE, 1),
            Item().enchant(Enchantment.LOOT_BONUS_MOBS, 1),
            Item().enchant(Enchantment.SWEEPING_EDGE, 1),
            Item().enchant(Enchantment.DIG_SPEED, 1),
            Item().enchant(Enchantment.SILK_TOUCH, 1),
            Item().enchant(Enchantment.ARROW_DAMAGE, 1),
            Item().enchant(Enchantment.ARROW_KNOCKBACK, 1),
            Item().enchant(Enchantment.ARROW_FIRE, 1),
            Item().enchant(Enchantment.ARROW_INFINITE, 1),
            Item().enchant(Enchantment.LOYALTY, 1),
            Item().enchant(Enchantment.LUCK, 1),
            Item().enchant(Enchantment.IMPALING, 1),
            Item().enchant(Enchantment.RIPTIDE, 1),
            Item().enchant(Enchantment.CHANNELING, 1),
            Item().enchant(Enchantment.QUICK_CHARGE, 1),
            Item().enchant(Enchantment.PIERCING, 1),
            Item().enchant(Enchantment.MULTISHOT, 1),
            Item().enchant(Enchantment.SWIFT_SNEAK, 1),
        )
        val max = enchantBook.size - 1
        var c = Random.nextInt(10, 20)
        object : BukkitRunnable() {
            override fun run() {
                c--
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                gui.setItem(4, enchantBook[Random.nextInt(0, max)])
                if (c == 0) {
                    player.inventory.addItem(gui.getItem(4))
                    player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 1f)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 5L) // 1秒間隔 (20 ticks) でタスクを実行
    }
}
