package net.calzoneman.RavenousnessGames;

import java.util.logging.Logger;

import net.calzoneman.RavenousnessGames.Listeners.BlockListener;
import net.calzoneman.RavenousnessGames.Listeners.PlayerListener;
import net.calzoneman.RavenousnessGames.RGStatus;

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
	private Map<String, Integer> playerModes;

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

		// Load playerModes from config file
		if (!this.getConfig().contains("playermodes"))
			this.playerModes = new HashMap<String, Integer>();
		else
			this.playerModes = this.getConfig().getConfigurationSection("playermodes").getValues();

		this.log.info("[RavenousnessGames] Plugin enabled.");
	}

	@Override
	public void onDisable() {
		// Save playerModes to config file
		this.getConfig().createSection("playermodes", this.playerModes);
		this.saveConfig();
		this.log.info("[RavenousnessGames] Plugin disabled.");
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (args.length == 1) {
			player = Bukkit.getServer().getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(args[0] + " is not online!");
			}
		}
		else if (sender instanceof Player) {
			player = (Player) sender;
		}
		String commandName = command.getName().toLowerCase();
		boolean success = false;
		if (commandName.equals("rgstatus") && player != null) {
			sender.sendMessage(this.getRGStatusSummary(player));
			success = true;
		}
		if (commandName.equals("rgspectator") && player != null) {
			if (this.getRGStatus(player) != RGStatus.ADMIN || sender.hasPermission("ravenousnessgames.superadmin"))
				this.setRGStatus(player, RGStatus.SPECTATOR);
			sender.sendMessage(this.getRGStatusSummary(player));
			success = true;
		}
		if (commandName.equals("rgcompetitor") && player != null) {
			if (this.getRGStatus(player) != RGStatus.ADMIN || sender.hasPermission("ravenousnessgames.superadmin"))
				this.setRGStatus(player, RGStatus.COMPETITOR);
			sender.sendMessage(this.getRGStatusSummary(player));
			success = true;
		}
		if (commandName.equals("rgsponsor") && player != null) {
			if (this.getRGStatus(player) != RGStatus.ADMIN || sender.hasPermission("ravenousnessgames.superadmin"))
				this.setRGStatus(player, RGStatus.SPONSOR);
			sender.sendMessage(this.getRGStatusSummary(player));
			success = true;
		}
		if (commandName.equals("rgadmin") && player != null) {
			this.setRGStatus(player, RGStatus.ADMIN);
			sender.sendMessage(this.getRGStatusSummary(player));
			success = true;
		}
		if (commandName.equals("rgbegin")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				switch (this.getRGStatus(player)) {
					case ADMIN:
						break;
					case COMPETITOR:
						break;
					case SPECTATOR:
						// Make invisible, prevent from building
						// no break
					case SPONSOR:
						// Give flying
				}

			success = true;
		}
		if (commandName.equals("rgend")) {
			// End
			success = true;
		}
		return success;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		// Make sure player is in playerModes
		if (!this.playerModes.containsKey(event.getPlayer().getName()))
			this.playerModes.put(event.getPlayer().getName(), RGStatus.SPECTATOR);
		// If the player is supposed to be hidden, hide him.
		if (this.playerModes.get(event.getPlayer().getName()) == RGStatus.SPECTATOR)
			this.hide(event.getPlayer());
		// Hide other invisible players from the player
		for (String playerName : this.playerModes.getKeys()) {
			Player p = Bukkit.getServer().getPlayer(playerName);
			if (this.getRGStatus(p) == RGStatus.SPECTATOR)
				event.getPlayer().hidePlayer(p);
		}
	}

	public RGStatus getRGStatus(Player player) {
		return RGStatus.getByValue(this.playerModes.get(player.getName()));
	}

	public void setRGStatus(Player player, RGStatus status) {
		this.playerModes.put(player.getName(), status.getValue());
	}

	/**
	 * Return a gramatically-correct summary of player's status
	 */
	public String getRGStatusSummary(Player player) {
		switch (this.getRGStatus(player)) {
			case SPECTATOR:
				return player.getName() + " is currently a spectator";
			case COMPETITOR:
				return player.getName() + " is currently a competitor";
			case SPONSOR:
				return player.getName() + " is currently a sponsor";
			case ADMIN:
				return player.getName() + " is currently an admin";
		}
	}

	/**
	 * Make player invisible to all non-admins
	 */
	private void hide(Player player) {
		for (Player other : Bukkit.getOnlinePlayers()) {
			if (!other.hasPermission("ravenousnessgames.admin")) {
				other.hidePlayer(player);
			}
		}
	}

	/**
	 * Make player visible to everyone
	 */
	private void unhide(Player player) {
		for (Player other : Bukkit.getOnlinePlayers()) {
			other.showPlayer(player);
		}
	}
}
