package com.islandstudio.neon

import com.islandstudio.neon.stable.primary.nConstructor.NConstructor
import org.bukkit.plugin.java.JavaPlugin

class Neon : JavaPlugin() {
    override fun onEnable() {
        NConstructor.constructPlugin()
    }

    override fun onDisable() {
        NConstructor.sendOutro()
    }
}