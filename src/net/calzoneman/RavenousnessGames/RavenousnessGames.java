package net.calzoneman.RavenousnessGames;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class RavenousnessGames extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		log.info("[RavenousnessGames] Plugin enabled.");
	}
	
	@Override
	public void onDisable() {
		
	}
}
