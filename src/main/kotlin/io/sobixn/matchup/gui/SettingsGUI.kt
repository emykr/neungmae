package io.sobixn.matchup.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.inventory.meta.ItemMeta

class SettingsGUI(private val plugin: JavaPlugin) : Listener {

    private var advancementsEnabled = true
    private var recipesEnabled = true

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun openSettings(player: Player) {
        val inventory = Bukkit.createInventory(null, 27, Component.text("Settings"))

        val grayGlass = createItemStack(Material.GRAY_STAINED_GLASS_PANE, Component.text(" "))

        for (i in 0 until 27) {
            if (i != 10 && i != 13 && i != 16) {
                inventory.setItem(i, grayGlass)
            }
        }

        inventory.setItem(10, createItemStack(Material.BOOK, Component.text("발전과제 ${if (advancementsEnabled) "비활성화" else "활성화"}")))
        inventory.setItem(13, createItemStack(Material.KNOWLEDGE_BOOK, Component.text("제작법 ${if (recipesEnabled) "비활성화" else "활성화"}")))

        player.openInventory(inventory)
    }

    private fun createItemStack(material: Material, name: Component): ItemStack {
        val itemStack = ItemStack(material)
        val itemMeta = itemStack.itemMeta!!
        itemMeta.displayName(name)
        itemMeta.isUnbreakable = true
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title() != Component.text("Settings")) return

        event.isCancelled = true

        val currentItem = event.currentItem ?: return
        if (currentItem.type == Material.AIR) return

        val player = event.whoClicked as Player

        when (event.slot) {
            10 -> {
                advancementsEnabled = !advancementsEnabled
                player.sendMessage(Component.text("발전과제가 ${if (advancementsEnabled) "활성화 되었습니다" else "비활성화 되었습니다"}"))
                openSettings(player) // Refresh the settings GUI
            }
            13 -> {
                recipesEnabled = !recipesEnabled
                player.sendMessage(Component.text("제작법이 ${if (recipesEnabled) "활성화 되었습니다" else "비활성화 되었습니다"}"))
                openSettings(player) // Refresh the settings GUI
            }
        }
    }

    fun areAdvancementsEnabled(): Boolean {
        return advancementsEnabled
    }

    fun areRecipesEnabled(): Boolean {
        return recipesEnabled
    }
}