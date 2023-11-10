package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player

class Status {
    fun addHP(player: Player) {
        Scoreboard().add("statusHP", player.uniqueId.toString(), 1)
        player.maxHealth = 20.0 + Scoreboard().getValue("statusHP", player.uniqueId.toString())
        player.sendMessage("${ChatColor.RED}最大HPが上がりました")
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
    fun addPower(player: Player) {
        Scoreboard().add("statusPower", player.uniqueId.toString(), 1)
        player.sendMessage("${ChatColor.YELLOW}パワーが上がりました")
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
}
