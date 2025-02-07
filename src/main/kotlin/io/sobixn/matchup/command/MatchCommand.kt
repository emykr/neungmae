package io.sobixn.matchup.command

import io.sobixn.matchup.Main
import io.sobixn.matchup.gui.SettingsGUI
import io.sobixn.matchup.skill.TestSkill
import io.sobixn.matchup.util.party.PartySystemImpl
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class MatchCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val testSkill = TestSkill(this)
    private val partySystem = PartySystemImpl(plugin as Main)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command can only be used by players.")
            return true
        }

        if (args.isEmpty()) {
            return false
        }

        when (args[0]) {
            "test" -> {
                if (args.size == 2 && args[1] == "simple") {
                    testSkill.spawnTriangleParticles(sender)
                    testSkill.applyDamage(sender)
                    return true
                }


            }

            "settings" -> {
                val settingsGUI = SettingsGUI(plugin as Main)
                settingsGUI.openSettings(sender)
                // Open settings GUI
                return true
            }

            "party" -> {
                when (args[1]) {
                    "invite" -> {
                        val targetPlayer = sender.server.getPlayer(args[2])
                        if (targetPlayer != null) {
                            val party =
                                partySystem.partySystem.getParty(sender) ?: partySystem.partySystem.createParty(sender)
                            partySystem.partySystem.invitePlayer(party, targetPlayer)
                            sender.sendMessage("${targetPlayer.name}를 초대했습니다 5분후 만료됩니다!")
                        } else {
                            sender.sendMessage("Player not found.")
                        }
                    }

                    "accept" -> {
                        partySystem.partySystem.acceptInvitation(sender)
                        partySystem.savePartyData()
                    }

                    "deny" -> {
                        partySystem.partySystem.denyInvitation(sender)
                    }

                    "leave" -> {
                        partySystem.partySystem.leaveParty(sender)
                    }
                }

                return true
            }
        }
        return false
}
}