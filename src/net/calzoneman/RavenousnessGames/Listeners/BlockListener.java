package net.calzoneman.RavenousnessGames.Listeners;

import net.calzoneman.RavenousnessGames.RavenousnessGames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockListener implements Listener {
	
	private RavenousnessGames plugin;
	
	public BlockListener(RavenousnessGames plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Fired when a block is damaged, but not necessarily broken
	 */
	@EventHandler
	public void handleBlockDamage(BlockDamageEvent event) {
		
	}
	
	/**
	 * Fired when a block is broken
	 */
	@EventHandler
	public void handleBlockBreak(BlockBreakEvent event) {
		
	}
}
