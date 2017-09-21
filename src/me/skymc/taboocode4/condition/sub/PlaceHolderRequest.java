package me.skymc.taboocode4.condition.sub;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.condition.NormalCondition;

public class PlaceHolderRequest implements NormalCondition {

	private FileConfiguration file;
	
	private Pattern pattern;
	private HashMap<String, String> placeholder = new HashMap<>();
	
	private Integer group_$;
	private Integer group_P;
	
	private String message;
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "[$][?]PlaceHolder[?][P][?]Request");
			
			conf.set("Settings.GroupLocation.Value", 1);
			conf.set("Settings.GroupLocation.PlaceHolder", 2);
			
			conf.set("Settings.PlaceHolder.EXP", "%player_exp%");
			conf.set("Settings.PlaceHolder.Loc-X", "%player_x%");
			conf.set("Settings.PlaceHolder.Loc-Y", "%player_y%");
			conf.set("Settings.PlaceHolder.Loc-Z", "%player_z%");

			conf.set("Settings.Messages.conditions", "§4Your §c%s% §4does not meet the requirements can not be used!");
		}
		message = conf.getString("Settings.Messages.conditions");
		
		for (String type : conf.getConfigurationSection("Settings.PlaceHolder").getKeys(false)) {
			placeholder.put(type, conf.getString("Settings.PlaceHolder." + type));
		}
		
		group_$ = conf.getInt("Settings.GroupLocation.Value");
		group_P = conf.getInt("Settings.GroupLocation.PlaceHolder");
		
		pattern = Pattern.compile(conf.getString("Settings.Pattern").replace("[#]", "(?:,|])").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)").replace("[P]", "(\\S+)"));
		
		if (Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") == null) {
			return false;
		}
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
	public boolean execute(Player player, ItemStack item, String unclore, List<String> uncoloredlore) {
		
		for (String lore : uncoloredlore) {
			Matcher m = pattern.matcher(lore);
			
			if (m.find()) {
				Double number = 0D;
					
				if (!placeholder.containsKey(m.group(group_P))) {
					TabooCode4.send("变量错误: &4" + m.group(group_P) + " &7(未知的格式)");
					return true;
				}
					
				try {
					number = Double.valueOf(PlaceholderAPI.setPlaceholders(player, placeholder.get(m.group(group_P))));
				}
				catch (Exception e) {
					TabooCode4.send("变量错误: &4" + m.group(group_P) + " &7(识别后不是一个数字)");
					return true;
				}
					
				if (number < Double.valueOf(m.group(group_$))) {
					return false;
				}
			}
		}
		return true;
	}

}