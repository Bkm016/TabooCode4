package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisInventoryClose implements Listener {

	@EventHandler
	public void hand(InventoryCloseEvent e)
	{
		Bukkit.getServer().getScheduler().runTask(TabooCode4.getInst(), new Runnable() {
			
			public void run()
			{
				if (((Player) e.getPlayer()).isOnline()) {
					AttributesManager.update((Player) e.getPlayer());
				}
			}
		});
	}
	
}