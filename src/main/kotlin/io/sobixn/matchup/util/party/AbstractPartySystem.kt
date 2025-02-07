package io.sobixn.matchup.util.party

import org.bukkit.entity.Player
import java.util.*

abstract class AbstractPartySystem {
    abstract fun createParty(leader: Player): PartySystemImpl.Party
    abstract fun getParty(player: Player): PartySystemImpl.Party?
    abstract fun invitePlayer(party: PartySystemImpl.Party, player: Player)
    abstract fun acceptInvitation(player: Player)
    abstract fun denyInvitation(player: Player)
    abstract fun leaveParty(player: Player)
}