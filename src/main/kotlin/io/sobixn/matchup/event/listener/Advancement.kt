package io.sobixn.matchup.event.listener

import io.sobixn.matchup.event.PlayerAdvancementCancelEvent
import io.sobixn.matchup.gui.SettingsGUI
import org.bukkit.Bukkit
import org.bukkit.advancement.Advancement
import org.bukkit.advancement.AdvancementProgress
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class Advancement(private val settingsGUI: SettingsGUI) : Listener {

    @EventHandler
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        if (!settingsGUI.areAdvancementsEnabled()) {
            val cancelEvent = PlayerAdvancementCancelEvent(event.player)
            Bukkit.getServer().pluginManager.callEvent(cancelEvent)
            if (cancelEvent.isCancelled) {
                // Handle the cancellation logic here
                val player = event.player
                val advancement: Advancement = event.advancement
                val advancementProgress: AdvancementProgress = player.getAdvancementProgress(advancement)

                // Iterate through the criteria and revoke them
                for (criteria in advancementProgress.awardedCriteria) {
                    advancementProgress.revokeCriteria(criteria)
                }
            }
        }
    }
}