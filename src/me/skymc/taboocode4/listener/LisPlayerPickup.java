package me.skymc.taboocode4.listener;

import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.condition.NormalCondition;
import me.skymc.taboocode4.condition.sub.OwnderRequest;
import me.skymc.taboocode4.manager.ItemFilter;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LisPlayerPickup implements Listener {
	
	private static OwnderRequest cond = null;
	
	public static void load() {
		for (NormalCondition condi : TabooCode4.getNormalConditions()) {
			if (condi.getClass().getSimpleName().equals("OwnderRequest")) {
				cond = (OwnderRequest) condi;
				return;
			}
		}
	}
	
	@EventHandler
	public void eve(PlayerPickupItemEvent e) {
		
		ItemStack item = e.getItem().getItemStack();
		
		if (!ItemFilter.hasLore(item)) {
			return;
		}
		
		if (!cond.execute(e.getPlayer(), item, ChatColor.stripColor(item.getItemMeta().getLore().toString()), null)) {
			e.setCancelled(true);
			e.getItem().setPickupDelay(10);
			
			e.getPlayer().sendMessage(cond.getPickupMessage());
		}
	}

}