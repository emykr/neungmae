// TestSkill.kt
package io.sobixn.matchup.skill

import io.sobixn.matchup.command.MatchCommand
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TestSkill(private val matchCommand: MatchCommand) : SimpleSkill {

    private var lastDirection: String? = null
    private val damagedPlayers = mutableSetOf<Player>()
    private val triangleVertices = mutableListOf<Vector>()

    fun spawnTriangleParticles(player: Player) {
        val currentDirection = getCardinalDirection(player)

        // Clear the set when direction changes
        if (currentDirection != lastDirection) {
            lastDirection = currentDirection
            damagedPlayers.clear()
        }

        val location = player.location.clone().apply { y = player.location.y + 0.1 } // Adjust y-coordinate slightly above player's feet
        val world = location.world ?: return

        val sideLength = 5.0
        val height = (sideLength * Math.sqrt(3.0)) / 2

        // Calculate the vertices of the triangle with the pointed vertex at the player's feet
        triangleVertices.clear()
        val vertices = when (currentDirection) {
            "NORTH" -> arrayOf(
                location.clone().add(0.0, 0.0, 0.0).toVector(), // Pointed vertex at player's feet
                location.clone().add(-sideLength / 2, 0.0, height).toVector(),
                location.clone().add(sideLength / 2, 0.0, height).toVector()
            )
            "SOUTH" -> arrayOf(
                location.clone().add(0.0, 0.0, 0.0).toVector(), // Pointed vertex at player's feet
                location.clone().add(sideLength / 2, 0.0, -height).toVector(),
                location.clone().add(-sideLength / 2, 0.0, -height).toVector()
            )
            "EAST" -> arrayOf(
                location.clone().add(0.0, 0.0, 0.0).toVector(), // Pointed vertex at player's feet
                location.clone().add(-height, 0.0, sideLength / 2).toVector(),
                location.clone().add(-height, 0.0, -sideLength / 2).toVector()
            )
            "WEST" -> arrayOf(
                location.clone().add(0.0, 0.0, 0.0).toVector(), // Pointed vertex at player's feet
                location.clone().add(height, 0.0, -sideLength / 2).toVector(),
                location.clone().add(height, 0.0, sideLength / 2).toVector()
            )
            else -> arrayOf(location.toVector(), location.toVector(), location.toVector()) // Default case
        }

        triangleVertices.addAll(vertices)

        val startColor = Color.fromRGB(255, 0, 0)
        val endColor = Color.fromRGB(255, 0, 0)
        val dustTransition = Particle.DustTransition(startColor, endColor, 1.5f)

        for (i in vertices.indices) {
            val start = vertices[i]
            val end = vertices[(i + 1) % vertices.size]
            val distance = start.distance(end)
            val steps = (distance * 5).toInt() // Reduce the number of steps to optimize

            val stepX = (end.x - start.x) / steps
            val stepY = (end.y - start.y) / steps
            val stepZ = (end.z - start.z) / steps

            for (j in 0..steps) {
                val particleLocation = start.clone().add(Vector(stepX * j, stepY * j, stepZ * j))
                world.spawnParticle(Particle.DUST_COLOR_TRANSITION, particleLocation.toLocation(world), 1, 0.0, 0.0, 0.0, 0.0, dustTransition)
            }
        }
    }

    fun applyDamage(player: Player) {
        val location = player.location
        val world = location.world ?: return

        // Clear the set to allow damage to be applied again
        damagedPlayers.clear()

        // Check for entities in a 360-degree radius and apply damage
        val nearbyEntities = world.getNearbyEntities(location, 5.0, 5.0, 5.0)
        for (entity in nearbyEntities) {
            if (entity is Player && entity != player && !damagedPlayers.contains(entity)) {
                if (isInsideTriangle(entity.location.toVector())) {
                    entity.damage(1.0)
                    damagedPlayers.add(entity) // Add player to the set to prevent further damage
                }
            }
        }
    }

    private fun isInsideTriangle(point: Vector): Boolean {
        val v0 = triangleVertices[2].clone().subtract(triangleVertices[0])
        val v1 = triangleVertices[1].clone().subtract(triangleVertices[0])
        val v2 = point.clone().subtract(triangleVertices[0])

        val dot00 = v0.dot(v0)
        val dot01 = v0.dot(v1)
        val dot02 = v0.dot(v2)
        val dot11 = v1.dot(v1)
        val dot12 = v1.dot(v2)

        val invDenom = 1 / (dot00 * dot11 - dot01 * dot01)
        val u = (dot11 * dot02 - dot01 * dot12) * invDenom
        val v = (dot00 * dot12 - dot01 * dot02) * invDenom

        return (u >= 0) && (v >= 0) && (u + v < 1)
    }

    private fun getCardinalDirection(player: Player): String {
        var yaw = player.location.yaw
        if (yaw < 0) yaw += 360
        return when (yaw) {
            in 0.0..22.5, in 337.5..360.0 -> "NORTH"
            in 22.5..67.5 -> "NORTH-EAST"
            in 67.5..112.5 -> "EAST"
            in 112.5..157.5 -> "SOUTH-EAST"
            in 157.5..202.5 -> "SOUTH"
            in 202.5..247.5 -> "SOUTH-WEST"
            in 247.5..292.5 -> "WEST"
            in 292.5..337.5 -> "NORTH-WEST"
            else -> "NORTH"
        }
    }
}