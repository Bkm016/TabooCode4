package me.skymc.taboocode4.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.skymc.taboocode4.TabooCode4;

public class LisEntityDeath implements Listener {
	
	@EventHandler
	public void death(EntityDeathEvent e) {
		
		if (e.getEntity().getKiller() == null) {
			return;
		}
		
		TabooCode4.getKilledSound().play(e.getEntity().getKiller());
	}

}