package io.sobixn.matchup.entity

import org.bukkit.entity.EntityType as BukkitEntityType

enum class CustomEntityType(val type: BukkitEntityType) {
    GUNE(BukkitEntityType.BLOCK_DISPLAY)
}