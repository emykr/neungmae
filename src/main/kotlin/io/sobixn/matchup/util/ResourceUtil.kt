package io.sobixn.matchup.util

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.plugin.java.JavaPlugin

class ResourceUtil(private val plugin: JavaPlugin) : Listener {

    private val resourcePackUrl = "https://www.dropbox.com/scl/fi/kcammsbqi2t4a4mzcmiez/.zip?rlkey=h2bzonqqgh1mybn3l8a52f8gb&st=0w5ms3au&dl=1"
    private val resourcePackHash = "6c8d1b8ea1afc1fe08dde130b5a82b8c0c3aff8c"

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.setResourcePack(resourcePackUrl, resourcePackHash)
        plugin.logger.info("Resource pack applying to player: ${player.name}")
    }

    @EventHandler
    fun onPlayerResourcePackStatus(event: PlayerResourcePackStatusEvent) {
        val player = event.player
        when (event.status) {
            PlayerResourcePackStatusEvent.Status.DECLINED -> {
                player.kickPlayer("You must accept the resource pack to play on this server.")
                plugin.logger.info("Player ${player.name} was kicked for declining the resource pack.")
            }
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                player.kickPlayer("Failed to download the resource pack. Please try again.")
                plugin.logger.info("Player ${player.name} was kicked for failing to download the resource pack.")
            }
            else -> {
                // Do nothing for SUCCESSFULLY_LOADED and ACCEPTED statuses
            }
        }
    }

    fun applyResourcePackToAllPlayers() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.setResourcePack(resourcePackUrl, resourcePackHash)
            plugin.logger.info("Resource pack applying to player: ${player.name}")
        }
    }
}