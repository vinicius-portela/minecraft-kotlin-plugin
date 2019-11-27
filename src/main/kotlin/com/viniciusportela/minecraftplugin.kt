package com.viniciusportela

import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class minecraftplugin: JavaPlugin() {
    override fun onEnable(){
        logger.info("Iniciando")
    }
}