package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class Job {
    val job = mutableListOf("無職", "${ChatColor.YELLOW}料理人", "${ChatColor.GOLD}ハンター", "${ChatColor.GRAY}鍛冶屋")
    fun get(player: Player): String {
        return job[Scoreboard().getValue("job", player.uniqueId.toString())]
    }
    fun prefix(player: Player) {
        val displayName = "[${get(player)}${ChatColor.WHITE}]${player.name}" + if (player.isOp) { "${ChatColor.AQUA}@運営" } else { "" }
        player.setDisplayName(displayName)
        player.setPlayerListName(displayName)
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
            Item().make(Material.POTATO, "${ChatColor.GREEN}キューリ", null, 5),
            Item().make(Material.WHEAT, "${ChatColor.GOLD}キャベツ", null, 1),
            Item().make(Material.SWEET_BERRIES, "${ChatColor.GOLD}スパイス", null, 1),
            Item().make(Material.WHEAT, "稲", null, 3),
            Item().make(Material.POTATO, "${ChatColor.DARK_PURPLE}なす", null, 7),
            Item().make(Material.POTATO, "${ChatColor.GOLD}たまねぎ", null, 2),
            Item().make(Material.POTATO, "${ChatColor.RED}トマト", null, 4)
        )
        location.world?.dropItem(location, vegetables[Random.nextInt(0, vegetables.size - 1)])
    }
}
