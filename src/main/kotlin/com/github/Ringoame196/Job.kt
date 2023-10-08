package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player

class Job {
    val job = mutableListOf<String>("無職", "${ChatColor.YELLOW}料理人", "${ChatColor.GOLD}ハンター", "${ChatColor.GRAY}鍛冶屋")
    fun get(player: Player): String {
        return job[Scoreboard().getValue("job", player.name)]
    }
    fun prefix(player: Player) {
        val displayName = "[${get(player)}${ChatColor.WHITE}]${player.name}" + if (player.isOp) { "${ChatColor.AQUA}@運営" } else {}
        player.setDisplayName(displayName)
        player.setPlayerListName(displayName)
    }
    fun change(player: Player, itemName: String) {
        val jobName = itemName.replace("職業カード", "").replace("(", "").replace(")", "")
        val item = player.inventory.itemInMainHand
        item.amount = item.amount - 1
        for (i in 0 until job.size) {
            if (job.get(i) != jobName.toString()) { continue }
            Scoreboard().set("job", player.name, i)
            prefix(player)
            player.sendMessage("${jobName}に就職しました")
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            player.world.spawnParticle(Particle.EXPLOSION_HUGE, player.location, 1)
            player.inventory.setItemInMainHand(item)
        }
    }
}
