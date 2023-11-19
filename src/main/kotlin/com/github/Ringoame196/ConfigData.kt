package com.github.Ringoame196

import java.util.UUID

class ConfigData {
    object DataManager {
        var databaseHost: String? = null
        var databasePort: Int? = null
        var databaseUsername: String? = null
        var databasePassword: String? = null
        val playerDataMap: MutableMap<UUID, Player.PlayerData> = mutableMapOf()
    }
}
