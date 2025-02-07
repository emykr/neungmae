package io.sobixn.matchup.skill.item

import io.sobixn.matchup.skill.windy.AbstractWindyAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class WindyFirst(private val plugin: JavaPlugin, private val windyAPI: AbstractWindyAPI) : Listener {

    private val activePlayers = mutableSetOf<Player>()

    init {
        object : BukkitRunnable() {
            override fun run() {
                activePlayers.forEach { player ->
                    player.inventory.itemInMainHand?.takeIf { it.isWindDagger() }?.let {
                        windyAPI.spawnParticles(player)
                        plugin.logger.info("Spawning particles for player: ${player.name}")
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L)
    }

    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT_CLICK")) {
            event.item?.takeIf { it.isWindDagger() }?.let {
                event.player.launchProjectile(Arrow::class.java)
                activePlayers.remove(event.player)
                plugin.logger.info("Player ${event.player.name} right-clicked with Wind Dagger and particles stopped")
                plugin.logger.info("You have used the Wind Dagger!")
            }
        }
    }

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val player = event.player
        val newItem = player.inventory.getItem(event.newSlot)

        if (newItem != null && newItem.isWindDagger()) {
            // 바람 단검을 들었을 때
            if (activePlayers.add(player)) {
                plugin.logger.info("Player ${player.name} is holding Wind Dagger")
                plugin.logger.info("You are holding the Wind Dagger!")
            }
        } else {
            // 바람 단검을 내려놨을 때
            if (activePlayers.remove(player)) {
                plugin.logger.info("Player ${player.name} stopped holding Wind Dagger")
                plugin.logger.info("You have stopped holding the Wind Dagger!")
            }
        }
    }


    private fun org.bukkit.inventory.ItemStack.isWindDagger(): Boolean {
        return type == Material.POPPED_CHORUS_FRUIT &&
                itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin, "ItemName"), PersistentDataType.STRING) == "바람 단검"
    }
}