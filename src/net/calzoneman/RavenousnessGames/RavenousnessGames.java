package net.calzoneman.RavenousnessGames;

import java.util.logging.Logger;

import net.calzoneman.RavenousnessGames.Listeners.BlockListener;
import net.calzoneman.RavenousnessGames.Listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RavenousnessGames extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	
	private PlayerListener playerListener;
	private BlockListener blockListener;

	@Override
	public void onEnable() {
		playerListener = new PlayerListener(this);
		blockListener = new BlockListener(this);
		
		// Set up event executors
		EventExecutor playerEventExecutor = new EventExecutor() {
			@Override
			public void execute(Listener listener, Event event) throws EventException {
				PlayerListener pl = (PlayerListener) listener;
				if(event instanceof PlayerMoveEvent)
					pl.handlePlayerMove((PlayerMoveEvent) event);
				else if(event instanceof PlayerInteractEvent)
					pl.handlePlayerInteract((PlayerInteractEvent) event);
				else if(event instanceof PlayerInteractEntityEvent)
					pl.handlePlayerInteractEntity((PlayerInteractEntityEvent) event);
				else
					log.severe("Event got sent to the wrong executor!");
			}
		};
		
		EventExecutor blockEventExecutor = new EventExecutor() {
			@Override
			public void execute(Listener listener, Event event) throws EventException {
				BlockListener bl = (BlockListener) listener;
				if(event instanceof BlockDamageEvent)
					bl.handleBlockDamage((BlockDamageEvent) event);
				else if(event instanceof BlockBreakEvent)
					bl.handleBlockBreak((BlockBreakEvent) event);
				else
					log.severe("Event got sent to the wrong executor!");
			}
		};
		
		// Register event callbacks
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(PlayerMoveEvent.class, playerListener, EventPriority.NORMAL, playerEventExecutor, this);
		pm.registerEvent(PlayerInteractEvent.class, playerListener, EventPriority.NORMAL, playerEventExecutor, this);
		pm.registerEvent(PlayerInteractEntityEvent.class, playerListener, EventPriority.NORMAL, playerEventExecutor, this);
		
		pm.registerEvent(BlockDamageEvent.class, blockListener, EventPriority.NORMAL, blockEventExecutor, this);
		pm.registerEvent(BlockBreakEvent.class, blockListener, EventPriority.NORMAL, blockEventExecutor, this);

		log.info("[RavenousnessGames] Plugin enabled.");
	}
	
	@Override
	public void onDisable() {
		
	}
}
