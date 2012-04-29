package net.calzoneman.RavenousnessGames.Listeners;

import net.calzoneman.RavenousnessGames.RavenousnessGames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
	private RavenousnessGames plugin;
	
	public PlayerListener(RavenousnessGames plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Handles player movement
	 */
	@EventHandler
	public void handlePlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled())
			return;
	}
	
	/**
	 * According to Bukkit's docs, PlayerInteractEvents are fired
	 * "when a player interacts with an object or air"
	 */
	@EventHandler
	public void handlePlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled())
			return;
	}
	
	/**
	 * According to Bukkit's docs, PlayerInteractEntityEvents are fired
	 * "when a player right clicks an entity"
	 */
	@EventHandler
	public void handlePlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(event.isCancelled())
			return;
	}
}
