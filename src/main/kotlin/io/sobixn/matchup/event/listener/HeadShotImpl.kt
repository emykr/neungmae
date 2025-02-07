package io.sobixn.matchup.event.listener

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

abstract class HeadShotImpl : HeadShotAPI {
    private val headShotCounts = ConcurrentHashMap<UUID, Int>()
    private val bodyShotCounts = ConcurrentHashMap<UUID, Int>()
    private val legShotCounts = ConcurrentHashMap<UUID, Int>()

    override fun registerHeadShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent) {
        val playerId = player.uniqueId
        headShotCounts[playerId] = headShotCounts.getOrDefault(playerId, 0) + 1
        event.damage *= 2.5 // 3 times damage
        player.sendMessage("Headshot registered! Total headshots: ${headShotCounts[playerId]}")
    }

    override fun registerBodyShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent) {
        val playerId = player.uniqueId
        bodyShotCounts[playerId] = bodyShotCounts.getOrDefault(playerId, 0) + 1
        event.damage *= 1.0 // 1 times damage (no change)
        player.sendMessage("Body shot registered! Total body shots: ${bodyShotCounts[playerId]}")
    }

    override fun registerLegShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent) {
        val playerId = player.uniqueId
        legShotCounts[playerId] = legShotCounts.getOrDefault(playerId, 0) + 1
        event.damage *= 0.75 // 0.5 times damage
        player.sendMessage("Leg shot registered! Total leg shots: ${legShotCounts[playerId]}")
    }
}