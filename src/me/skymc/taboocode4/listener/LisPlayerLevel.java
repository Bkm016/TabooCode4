package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LisPlayerLevel implements Listener {
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void eve(PlayerLevelChangeEvent e) {
		
		Bukkit.getServer().getScheduler().runTask(TabooCode4.getInst(), new Runnable() {
			
			public void run()
			{
				AttributesManager.update(e.getPlayer());
			}
		});
	}

}