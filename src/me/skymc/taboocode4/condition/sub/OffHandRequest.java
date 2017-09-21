package me.skymc.taboocode4.condition.sub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.condition.NormalCondition;

public class OffHandRequest implements NormalCondition {

	private FileConfiguration file;
	
	private Pattern pattern;
	
	private Integer group_$;
	
	private String message;
	
	private HashMap<String, List<String>> OffHand = new HashMap<>();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "[$] OffHand Request");
			
			conf.set("Settings.GroupLocation.Value", 1);
			
			conf.set("Settings.OffHand.Air", Arrays.asList("AIR"));
			conf.set("Settings.OffHand.Air/Shield", Arrays.asList("AIR", "SHIELD"));

			conf.set("Settings.Messages.conditions", "§4Your §c%s% §4does not meet the requirements can not be used!");
		}
		group_$ = conf.getInt("Settings.GroupLocation.Value");
		
		message = conf.getString("Settings.Messages.conditions");
		
		for (String type : conf.getConfigurationSection("Settings.OffHand").getKeys(false)) {
			OffHand.put(type, conf.getStringList("Settings.OffHand." + type));
		}
		
		pattern = Pattern.compile(conf.getString("Settings.Pattern").replace("[$]", "(\\S+)").replace("[?]", "(?:\\s|\\S)").replace("[#]", "(?:,|])"));
		return true;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public int getPriority() {
		return file.getInt("Settings.Priority");
	}

	@Override
	public boolean execute(Player player, ItemStack item, String lore, List<String> uncoloredlore) {
		
		Matcher m = pattern.matcher(lore);
		if (m.find()) {
			
			if (OffHand.containsKey(m.group(group_$))) {
				if (!OffHand.get(m.group(group_$)).contains(player.getInventory().getItemInOffHand().getType().toString())) {
					return false;
				}
			}
		}
		return true;
	}

}