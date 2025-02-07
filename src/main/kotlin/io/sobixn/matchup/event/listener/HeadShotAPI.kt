package io.sobixn.matchup.event.listener

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

interface HeadShotAPI {
    fun registerHeadShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent)
    fun registerBodyShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent)
    fun registerLegShot(player: Player, item: ItemStack, event: EntityDamageByEntityEvent)
}