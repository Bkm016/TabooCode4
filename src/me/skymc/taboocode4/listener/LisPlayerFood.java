package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisPlayerFood implements Listener {
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void eve(FoodLevelChangeEvent e) {
		
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		Bukkit.getServer().getScheduler().runTask(TabooCode4.getInst(), new Runnable() {
			
			public void run()
			{
				AttributesManager.update((Player) e.getEntity());
			}
		});
	}

}