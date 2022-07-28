package com.islandstudio.neon.stable.utils

import com.islandstudio.neon.stable.primary.nCommand.CommandSyntax
import com.islandstudio.neon.stable.primary.nConstructor.NConstructor
import com.islandstudio.neon.stable.primary.nProfile.NProfile
import com.islandstudio.neon.stable.secondary.nRank.NRank
import com.islandstudio.neon.stable.utils.nGUI.NGUI
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket
import net.minecraft.world.item.crafting.Recipe
import org.bukkit.ChatColor
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerLoadEvent

object ServerHandler {
    /**
     * Broadcast join message to all players when a player join the server.
     *
     * @param player The player who join the server.
     */
    fun broadcastPlayerJoinMessage(player: Player) {
        val server: Server = player.server

        server.broadcastMessage(CommandSyntax.createSyntaxMessage("${ChatColor.GOLD}Welcome back, ${ChatColor.GREEN}${player.name}${ChatColor.GOLD}!"))
        server.broadcastMessage(CommandSyntax.createSyntaxMessage("${ChatColor.GREEN}${server.onlinePlayers.size}${ChatColor.GOLD} of ${ChatColor.RED}${server.maxPlayers}${ChatColor.GOLD} player(s) Online!"))
    }

    /**
     * Broadcast quit message to all players when a player quit the server.
     *
     * @param player The player who quit the server.
     */
    fun broadcastPlayerQuitMessage(player: Player) {
        val server: Server = player.server

        server.broadcastMessage(CommandSyntax.createSyntaxMessage("${ChatColor.GREEN}${player.name}${ChatColor.GOLD} left, ${ChatColor.GREEN}${server.onlinePlayers.size - 1}${ChatColor.GOLD} other(s) here!"))
    }

    /**
     * Get the mode, either Online Mode or Offline Mode.
     *
     * @return The mode. (String)
     */
    fun getMode(): String {
        return if (NConstructor.plugin.server.onlineMode) {
            "online"
        } else {
            "offline"
        }
    }

    /**
     * Update player recipes once the server reloaded.
     *
     * @param player The player to update the recipes for. (Player)
     */
    @Suppress("UNCHECKED_CAST")
    fun updateRecipe(player: Player) {
        val serverRecipes: Map<Any, Map<Any, Any>>

        when (NConstructor.getVersion()) {
            "1.17" -> {
                serverRecipes = (NPacketProcessor.getNPlayer(player).server!!).recipeManager!!.recipes!! as Map<Any, Map<Any, Any>>

                NPacketProcessor.sendGamePacket(player, ClientboundUpdateRecipesPacket(
                    serverRecipes.values.parallelStream().flatMap { map -> map.values.parallelStream() }.toList()!! as MutableCollection<Recipe<*>>
                ))
            }

            "1.18" -> {
                val craftingManager: Any = (NPacketProcessor.getNPlayer(player).server!!).javaClass.getMethod("aC").invoke(NPacketProcessor.getNPlayer(player).server)!!

                serverRecipes = craftingManager.javaClass.getField("c").get(craftingManager)!! as Map<Any, Map<Any, Any>>

                NPacketProcessor.sendGamePacket(player, ClientboundUpdateRecipesPacket(
                    serverRecipes.values.parallelStream().flatMap { map -> map.values.parallelStream() }.toList()!! as MutableCollection<Recipe<*>>
                ))
            }
        }
    }

    class EventController: Listener {
        @EventHandler
        private fun onServerLoad(e: ServerLoadEvent) {
            when (e.type) {
                ServerLoadEvent.LoadType.STARTUP, ServerLoadEvent.LoadType.RELOAD -> {
                    NConstructor.plugin.server.onlinePlayers.parallelStream().forEach {player ->
                        updateRecipe(player)

                        player.closeInventory()
                    }
                }
            }
        }

        @EventHandler
        private fun onPlayerJoin(e: PlayerJoinEvent) {
            val player: Player = e.player

            NProfile.Handler.createProfile(player)
            NRank.updateTag()
            e.joinMessage = ""
            broadcastPlayerJoinMessage(player)
        }

        @EventHandler
        private fun onPlayerQuit(e: PlayerQuitEvent) {
            val player: Player = e.player

            NGUI.Handler.nGUIContainer.remove(player)
            e.quitMessage = ""
            broadcastPlayerQuitMessage(player)
        }

        @EventHandler
        private fun onPlayerChat(e: AsyncPlayerChatEvent) {
            e.isCancelled = true
            NRank.sendMessage(e.player, e.message)
        }
    }
}