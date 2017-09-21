package me.skymc.taboocode4.condition.skillapi;

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

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.condition.NormalCondition;

public class SkillAPIAttributeRequest implements NormalCondition {

	private FileConfiguration file;
	
	private Pattern pattern;
	
	private Integer group_$;
	private Integer group_P;
	
	private String message;
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "[$][?]Attribute[?][A][?]Request");
			
			conf.set("Settings.GroupLocation.Value", 1);
			conf.set("Settings.GroupLocation.Attribute", 2);
			
			conf.set("Settings.Messages.conditions", "§4Your §c%s% §4does not meet the requirements can not be used!");
		}
		message = conf.getString("Settings.Messages.conditions");
		
		group_$ = conf.getInt("Settings.GroupLocation.Value");
		group_P = conf.getInt("Settings.GroupLocation.Attribute");
		
		pattern = Pattern.compile(conf.getString("Settings.Pattern").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)").replace("[A]", "(\\S+)"));
		
		if (Bukkit.getPluginManager().getPlugin("SkillAPI") == null) {
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
	public boolean execute(Player player, ItemStack item, String _lore, List<String> uncoloredlore) {
		
		for (String lore : uncoloredlore) {
			Matcher m = pattern.matcher(lore);
			
			if (m.find()) {
				PlayerData data = SkillAPI.getPlayerData(player);
				if (data == null) {
					return false;
				}
				
				if (data.getAttribute(m.group(group_P)) < Double.valueOf(m.group(group_$))) {
					return false;
				}
			}
		}
		
		return true;
	}

}