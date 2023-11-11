package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Food {
    fun recovery(player: Player, item: ItemStack) {
        val itemName = item.itemMeta?.displayName
        val addfood = when (itemName) {
            "おにぎり" -> 6
            "ステーキ" -> 5
            "からあげ" -> 2
            "とんかつ" -> 2
            "ハンバーグ" -> 4
            else -> 2
        }
        when (itemName) {
            "${ChatColor.YELLOW}力のプロテイン" -> Status().addPower(player)
            "${ChatColor.RED}ハートのハーブ" -> Status().addHP(player)
        }
        player.foodLevel += addfood
    }
    fun item(name: String, customModelData: Int): ItemStack {
        val item = ItemStack(Material.MELON_SLICE)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.setCustomModelData(customModelData)
        meta?.lore = mutableListOf(giveExpirationDate(14))
        item.setItemMeta(meta)
        return item
    }
    fun giveExpirationDate(add: Int): String {
        val now = Calendar.getInstance()
        now.add(Calendar.DAY_OF_WEEK, add)

        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1 // 月は0から始まるため+1
        val day = now.get(Calendar.DAY_OF_MONTH)
        return "消費期限: $year/$month/$day"
    }
    fun isExpirationDate(player: Player, item: ItemStack): Boolean {
        val expiration = item.itemMeta?.lore?.get(0) ?: return false
        val dateStr = expiration.replace("消費期限:", "")
        val dateFormat = SimpleDateFormat("yyyy/MM/dd") // フォーマットに合わせて変更
        val date = dateFormat.parse(dateStr) ?: return false

        // 現在の日付を取得
        val currentDate = Date()

        // 日付の差を計算
        val diff = (currentDate.time - date.time) / (1000 * 60 * 60 * 24) // ミリ秒から日数に変換
        if (diff > 0) {
            Player().errorMessage(player, "消費期限が切れています")
            return true
        }
        return false
    }
}
