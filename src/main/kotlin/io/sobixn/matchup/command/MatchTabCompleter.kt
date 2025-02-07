package io.sobixn.matchup.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MatchTabCompleter : TabCompleter {

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size == 1) {
            return listOf("test", "party","settings")
        } else if (args.size == 2) {
            return when (args[0]) {
                "test" -> listOf("simple")
                "party" -> listOf("invite", "accept", "deny","leave")
                else -> null
            }
        } else if (args.size == 3 && args[0] == "party" && args[1] == "invite") {
            return null // Return null to let the server handle player name completion
        }
        return null
    }
}