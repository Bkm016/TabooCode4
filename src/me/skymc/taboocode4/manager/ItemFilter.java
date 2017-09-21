package me.skymc.taboocode4.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemFilter {
	
	public static boolean isNull(ItemStack item) {
		if (item == null) {
			return true;
		}
		
		if (item.getType().equals(Material.AIR)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean hasLore(ItemStack item) {
		if (isNull(item)) {
			return false;
		}
		
		if (!item.getItemMeta().hasLore()) {
			return false;
		}
		
		return true;
	}

}