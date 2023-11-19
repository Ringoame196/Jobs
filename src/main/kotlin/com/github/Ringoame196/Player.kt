package com.github.Ringoame196

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player

class Player {
    data class PlayerData(
        var maxHP: Int = 0,
        var power: Int = 0
    )
    fun errorMessage(player: Player, message: String) {
        player.sendMessage("${ChatColor.RED}$message")
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }

    fun sendActionBar(player: Player, message: String) {
        val actionBarMessage = ChatColor.translateAlternateColorCodes('&', message)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(actionBarMessage))
    }

    fun join(player: Player) {
        Job().prefix(player)
        val playerData = ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }
        playerData.maxHP = Database().getInt(player.uniqueId.toString(), "aoringoserver", "status", "hp")
        playerData.power = Database().getInt(player.uniqueId.toString(), "aoringoserver", "status", "power")
        player.maxHealth = 20.0 + ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.maxHP
    }
    fun addHP(player: Player) {
        val maxHP = ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { com.github.Ringoame196.Player.PlayerData() }.maxHP + 1
        ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { com.github.Ringoame196.Player.PlayerData() }.maxHP = maxHP
        Database().setPlayerPoint(player.uniqueId.toString(), "aoringoserver", "status", "hp", maxHP)
        player.maxHealth = 20.0 + maxHP.toDouble()
        levelupMessage(player, "${ChatColor.RED}最大HPアップ！！")
    }
    fun addPower(player: Player) {
        val power = ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { com.github.Ringoame196.Player.PlayerData() }.power + 1
        ConfigData.DataManager.playerDataMap.getOrPut(player.uniqueId) { com.github.Ringoame196.Player.PlayerData() }.power = power
        Database().setPlayerPoint(player.uniqueId.toString(), "aoringoserver", "status", "power", power)
        levelupMessage(player, "${ChatColor.YELLOW}パワーアップ！！")
    }
    private fun levelupMessage(player: Player, message: String) {
        player.sendMessage(message)
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
}
