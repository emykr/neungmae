package io.sobixn.matchup.event.listener

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

abstract class HeadShotImpl : HeadShotAPI {
    private val protocolManager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    override fun registerHeadShot(shooter: Player, weapon: ItemStack, event: EntityDamageByEntityEvent) {
        event.damage = 100.0
        sendDamageAnimation(event.entity as Player)
    }

    override fun registerBodyShot(shooter: Player, weapon: ItemStack, event: EntityDamageByEntityEvent) {
        event.damage = 50.0
        sendDamageAnimation(event.entity as Player)
    }

    override fun registerLegShot(shooter: Player, weapon: ItemStack, event: EntityDamageByEntityEvent) {
        event.damage = 25.0
        sendDamageAnimation(event.entity as Player)
    }

    private fun sendDamageAnimation(player: Player) {
        val packet = PacketContainer(PacketType.Play.Server.ANIMATION)
        packet.integers.write(0, player.entityId)
        packet.integers.write(1, 1) // 데미지 애니메이션 ID

        try {
            protocolManager.broadcastServerPacket(packet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}