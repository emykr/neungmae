package io.sobixn.matchup.skill.windy

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

abstract class AbstractWindyAPI {

    fun spawnParticles(player: Player) {
        val location = player.location
        val world = location.world ?: return

        val startColor = Color.fromRGB(211, 211, 211) // Bright gray
        val endColor = Color.fromRGB(0, 255, 255) // Cyan
        val dustTransition = Particle.DustTransition(startColor, endColor, 1.5f)

        val leftOffset = Vector(-1.0, 0.0, 0.0)
        val rightOffset = Vector(1.0, 0.0, 0.0)

        for (i in 0..10) {
            val leftParticleLocation = location.clone().add(leftOffset).add(0.0, i * 0.2, 0.0)
            val rightParticleLocation = location.clone().add(rightOffset).add(0.0, i * 0.2, 0.0)

            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, leftParticleLocation, 1, 0.0, 0.0, 0.0, 0.0, dustTransition)
            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, rightParticleLocation, 1, 0.0, 0.0, 0.0, 0.0, dustTransition)
        }
    }
}