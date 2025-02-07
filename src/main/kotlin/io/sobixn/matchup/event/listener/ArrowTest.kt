package io.sobixn.matchup.event.listener

import io.sobixn.matchup.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class ArrowTest(private val headShotAPI: HeadShotAPI, private val plugin: JavaPlugin) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager is Arrow && event.entity is Player) {
            val arrow = event.damager as Arrow
            val player = event.entity as Player

            if (arrow.shooter is Player) {
                val shooter = arrow.shooter as Player
                val hitY = arrow.location.y
                val eyeY = player.eyeLocation.y
                val feetY = player.location.y

                when {
                    hitY >= eyeY -> {
                        headShotAPI.registerHeadShot(shooter, shooter.inventory.itemInMainHand, event)
                        shooter.sendMessage("Headshot!")
                        Main.instance.logger.info("Headshot!")
                    }
                    hitY < eyeY && hitY > feetY + 0.5 -> {
                        headShotAPI.registerBodyShot(shooter, shooter.inventory.itemInMainHand, event)
                        shooter.sendMessage("Body shot!")
                        Main.instance.logger.info("Body shot!")
                    }
                    hitY <= feetY + 0.5 -> {
                        headShotAPI.registerLegShot(shooter, shooter.inventory.itemInMainHand, event)
                        shooter.sendMessage("Leg shot!")
                        Main.instance.logger.info("Leg shot!")
                    }
                    else -> {
                        shooter.sendMessage("실패")
                        Main.instance.logger.info("실패")
                    }
                }
            }
        }
    }
}