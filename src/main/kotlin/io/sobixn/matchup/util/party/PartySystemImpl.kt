package io.sobixn.matchup.util.party

import io.sobixn.matchup.Main
import io.sobixn.matchup.util.SobinFileSystem
import org.bukkit.entity.Player
import java.util.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor

class PartySystemImpl(private val plugin: Main) {
    private val parties = mutableMapOf<UUID, Party>()
    private val invitations = mutableMapOf<UUID, MutableList<PartyInvitation>>()
    private val fileSystem = SobinFileSystem()

    val partySystem = object : AbstractPartySystem() {
        override fun createParty(leader: Player): Party {
            return Party(leader).apply { parties[leader.uniqueId] = this }
        }

        override fun getParty(player: Player): Party? = parties[player.uniqueId]

        override fun invitePlayer(party: Party, player: Player) {
            if (player.uniqueId == party.leader.uniqueId) {
                player.sendMessage(Component.text("자신에게는 초대 할수 없습니다.").color(NamedTextColor.RED))
                return
            }

            val existingParty = getParty(player)
            existingParty?.let {
                it.removeMember(player)
                if (it.members.isEmpty()) parties.remove(it.leader.uniqueId)
            }

            val invitation = PartyInvitation(party, player)
            invitations.computeIfAbsent(player.uniqueId) { mutableListOf() }.add(invitation)
            val message = Component.text("${party.leader.name}님의 파티에 초대 되었습니다")
                .append(Component.text("여기를 클릭")
                    .color(NamedTextColor.YELLOW)
                    .clickEvent(ClickEvent.runCommand("/match party accept")))
                .append(Component.text(" 하면 참여합니다"))
            player.sendMessage(message)
        }

        override fun acceptInvitation(player: Player) {
            invitations[player.uniqueId]?.firstOrNull()?.let {
                val existingParty = getParty(player)
                existingParty?.let { party ->
                    party.removeMember(player)
                    if (party.members.isEmpty()) parties.remove(party.leader.uniqueId)
                }

                it.party.addMember(player)
                invitations.remove(player.uniqueId)

                val leaderMessage = Component.text("${player.name} has joined the party!")
                    .color(NamedTextColor.GREEN)
                it.party.leader.sendMessage(leaderMessage)

                val memberMessage = Component.text("${player.name} has joined the party!")
                    .color(NamedTextColor.BLUE)
                it.party.members.forEach { member ->
                    if (member != it.party.leader) {
                        member.sendMessage(memberMessage)
                    }
                }
            }
        }

        override fun denyInvitation(player: Player) {
            invitations.remove(player.uniqueId)
        }

        override fun leaveParty(player: Player) {
            getParty(player)?.let { party ->
                party.removeMember(player)
                if (party.members.isEmpty()) parties.remove(party.leader.uniqueId)
            }
        }
    }

    fun savePartyData() {
        fileSystem.saveFile("${plugin.dataFolder}/party/party_data.json", parties)
    }

    fun loadPartyData() {
        val loadedParties = fileSystem.loadFile("${plugin.dataFolder}/party/party_data.json", Map::class.java) as? Map<UUID, Party>
        loadedParties?.let { parties.putAll(it) }
    }

    open class Party(val leader: Player) {
        val members = mutableSetOf<Player>(leader)

        open fun addMember(player: Player) {
            members.add(player)
        }

        open fun removeMember(player: Player) {
            members.remove(player)
        }
    }

    class PartyInvitation(val party: Party, val player: Player)
}