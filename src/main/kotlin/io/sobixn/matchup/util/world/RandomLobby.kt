package io.sobixn.matchup.util.world

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class RandomLobby(private val plugin: JavaPlugin) : Listener {

    private val spawnLocations = listOf(
        Location(Bukkit.getWorld("world"), 11.5, 36.0, 0.5),
        Location(Bukkit.getWorld("world"), 0.5, 36.0, 11.5),
        Location(Bukkit.getWorld("world"), -10.5, 36.0, 0.5),
        Location(Bukkit.getWorld("world"), 0.5, 36.0, -10.5)
    )

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val randomIndex = Random.nextInt(spawnLocations.size)
        val location = spawnLocations[randomIndex]
        if (location.world != null) {
            player.teleport(location)
        } else {
            player.sendMessage("World not found!")
        }
    }
}