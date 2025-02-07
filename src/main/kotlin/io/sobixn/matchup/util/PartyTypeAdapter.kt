package io.sobixn.matchup.util

import com.google.gson.*
import io.sobixn.matchup.util.party.PartySystemImpl.Party
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Type
import java.util.*

class PartyTypeAdapter : JsonSerializer<Party>, JsonDeserializer<Party> {
    override fun serialize(src: Party, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("leader", src.leader.uniqueId.toString())
        val membersArray = JsonArray()
        src.members.forEach { membersArray.add(it.uniqueId.toString()) }
        jsonObject.add("members", membersArray)
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Party {
        val jsonObject = json.asJsonObject
        val leaderUUID = UUID.fromString(jsonObject.get("leader").asString)
        val leader = Bukkit.getPlayer(leaderUUID) ?: throw JsonParseException("Leader not found")
        val party = Party(leader)
        val membersArray = jsonObject.getAsJsonArray("members")
        membersArray.forEach {
            val memberUUID = UUID.fromString(it.asString)
            val member = Bukkit.getPlayer(memberUUID)
            if (member != null) {
                party.addMember(member)
            }
        }
        return party
    }
}