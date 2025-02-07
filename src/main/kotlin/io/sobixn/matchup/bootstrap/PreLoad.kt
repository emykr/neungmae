package io.sobixn.matchup.bootstrap

import io.sobixn.matchup.Main
import io.sobixn.matchup.command.MatchCommand
import io.sobixn.matchup.command.MatchTabCompleter
import io.sobixn.matchup.event.listener.Advancement
import io.sobixn.matchup.event.listener.Recipe
import io.sobixn.matchup.gui.SettingsGUI
import io.sobixn.matchup.skill.item.WindyFirst
import io.sobixn.matchup.skill.windy.WindyBase
import io.sobixn.matchup.util.ResourceUtil
import io.sobixn.matchup.util.world.RandomLobby

class PreLoad(private val plugin: Main) {

    fun registerCommands() {
        plugin.getCommand("match")?.setExecutor(MatchCommand(plugin))
        plugin.getCommand("match")?.tabCompleter = MatchTabCompleter()
        // Command Register
    }

    fun registerEvents() {
        val settingsGUI = SettingsGUI(plugin)
        val windyAPI = WindyBase()
        plugin.server.pluginManager.registerEvents(WindyFirst(plugin, windyAPI), plugin)
        plugin.server.pluginManager.registerEvents(Recipe(settingsGUI), plugin)
        plugin.server.pluginManager.registerEvents(RandomLobby(plugin), plugin)
        plugin.server.pluginManager.registerEvents(Advancement(settingsGUI), plugin)
        plugin.server.pluginManager.registerEvents(ResourceUtil(plugin), plugin)
        // Event Register
    }

    fun applyResourcePackToAllPlayers() {
        val resourceUtil = ResourceUtil(plugin)
        resourceUtil.applyResourcePackToAllPlayers()

    }
}