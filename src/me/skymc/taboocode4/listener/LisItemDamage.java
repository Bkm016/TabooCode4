package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisItemDamage implements Listener {
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void eve(PlayerItemDamageEvent e) {
		
		if (e.getItem().equals(e.getPlayer().getInventory().getItemInMainHand())) {
			return;
		}
		
		Bukkit.getServer().getScheduler().runTask(TabooCode4.getInst(), new Runnable() {
			
			public void run()
			{
				AttributesManager.update(e.getPlayer());
			}
		});
	}

}