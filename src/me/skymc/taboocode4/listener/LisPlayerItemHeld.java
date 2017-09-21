package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisPlayerItemHeld implements Listener {
	
	@EventHandler
	public void hand(PlayerItemHeldEvent e)
	{
		Bukkit.getServer().getScheduler().runTask(TabooCode4.getInst(), new Runnable() {
			
			public void run()
			{
				AttributesManager.update(e.getPlayer());
			}
		});
	}

}