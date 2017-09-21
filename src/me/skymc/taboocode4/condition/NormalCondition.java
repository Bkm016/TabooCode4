package me.skymc.taboocode4.condition;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract interface NormalCondition {

	public abstract boolean register(FileConfiguration conf, boolean first);
	
	public abstract int getPriority();
	
	public abstract String getMessage();
	
	public abstract boolean execute(Player player, ItemStack item, String lore, List<String> uncoloredlore);
	
}