package io.sobixn.matchup

import io.sobixn.matchup.bootstrap.PreLoad
import io.sobixn.matchup.event.listener.ArrowTest
import io.sobixn.matchup.event.listener.HeadShotImpl
import io.sobixn.matchup.util.party.PartySystemImpl
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    private lateinit var boot: PreLoad
    private lateinit var partySystem: PartySystemImpl

    override fun onEnable() {
        instance = this
        val headShotAPI = object : HeadShotImpl() {}
        ArrowTest(headShotAPI, this)
        boot = PreLoad(this)
        partySystem = PartySystemImpl(this)
        logger.info("능력자 매치 플러그인이 활성화 되었습니다.")
        boot.registerEvents()
        boot.registerCommands()
        partySystem.loadPartyData()  // Load party data on startup
        boot.applyResourcePackToAllPlayers()
    }

    override fun onDisable() {
        logger.info("능력자 매치 플러그인이 비활성화 되었습니다.")
        partySystem.savePartyData()  // Save party data on shutdown
    }

    companion object {
        lateinit var instance: Main
            private set
    }
}