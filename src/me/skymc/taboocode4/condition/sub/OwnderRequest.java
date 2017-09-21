package me.skymc.taboocode4.condition.sub;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.condition.NormalCondition;

public class OwnderRequest implements NormalCondition {

	private FileConfiguration file;
	
	private Pattern pattern;
	
	private Integer group_$;
	
	private String pickup;
	private String message;
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "[$] Ownder");
			
			conf.set("Settings.GroupLocation.Value", 1);
			
			conf.set("Settings.Messages.conditions", "§4Your §c%s% §4does not meet the requirements can not be used!");
			conf.set("Settings.Messages.pickupownder", "§4You can not pick up other player''s items");
		}
		group_$ = conf.getInt("Settings.GroupLocation.Value");
		
		message = conf.getString("Settings.Messages.conditions");
		pickup = conf.getString("Settings.Messages.pickupownder");
		
		pattern = Pattern.compile(conf.getString("Settings.Pattern").replace("[$]", "(\\S+)").replace("[?]", "(?:\\s|\\S)").replace("[#]", "(?:,|])"));
		return true;
	}
	
	public String getPickupMessage() {
		return pickup;
	}

	@Override
	public int getPriority() {
		return file.getInt("Settings.Priority");
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public boolean execute(Player player, ItemStack item, String lore, List<String> uncoloredlore) {
		
		Matcher m = pattern.matcher(lore);
		if (m.find()) {
			if (!player.getName().equals(m.group(group_$))) {
				return false;
			}
		}
		return true;
	}

}