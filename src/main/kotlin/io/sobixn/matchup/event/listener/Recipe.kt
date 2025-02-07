package io.sobixn.matchup.event.listener

import io.sobixn.matchup.gui.SettingsGUI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent

class Recipe(private val settingsGUI: SettingsGUI) : Listener {

    @EventHandler
    fun onServerLoad(event: ServerLoadEvent) {
        if (!settingsGUI.areRecipesEnabled()) {
            removeAllRecipes()
        }
    }

    private fun removeAllRecipes() {
        val recipes = Bukkit.recipeIterator()
        while (recipes.hasNext()) {
            recipes.next()
            recipes.remove()
        }
    }
}