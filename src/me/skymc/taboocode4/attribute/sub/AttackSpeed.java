package me.skymc.taboocode4.attribute.sub;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class AttackSpeed implements SpecialAttribute {

	private FileConfiguration file;
	
	private Pattern Primary;
	
	private Integer group_Primary;
	
	private String Name = this.getClass().getSimpleName();
	
	private HashMap<String, Double> confdata = new HashMap<>();
	
	private HashMap<String, String> playerdata = new HashMap<>();
	private HashMap<String, Long> cooldown = new HashMap<>();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Primary", "[$] Attack Speed");
			
			conf.set("Settings.GroupLocation.Primary", 1);
			
			conf.set("Settings.AttackSpeed.VeryFast", 0);
			conf.set("Settings.AttackSpeed.Fast", 0.3);
			conf.set("Settings.AttackSpeed.Normal", 0.6);
			conf.set("Settings.AttackSpeed.Slow", 1.3);
			conf.set("Settings.AttackSpeed.VerySlow", 1.6);
			
			conf.set("Settings.Priority", 10);
		}
		
		for (String type : conf.getConfigurationSection("Settings.AttackSpeed").getKeys(false)) {
			confdata.put(type, conf.getDouble("Settings.AttackSpeed." + type));
		}

		Primary = Pattern.compile(conf.getString("Settings.Primary").replace("[$]", "(\\S+)").replace("[?]", "(?:\\s|\\S)"));
		
		group_Primary = conf.getInt("Settings.GroupLocation.Primary");

		return true;
	}
	
	@Override
	public int getPriority() {
		return file.getInt("Settings.Priority");
	}

	@Override
	public DamageType getType() {
		return DamageType.ATTACK;
	}

	@Override
	public String getHoloSpecies() {
		return "default";
	}

	@Override
	public SpecialAttributeData getAttribute(LivingEntity entity, ItemStack item, List<String> uncoloredlores) {
		
		if (!(entity instanceof Player)) {
			return null;
		}
		
		if (entity.getEquipment().getItemInMainHand().equals(item)) {
			
			Matcher m = Primary.matcher(ChatColor.stripColor(item.getItemMeta().getLore().toString()));
			if (m.find()) {
				if (confdata.containsKey(m.group(group_Primary))) {
					
					playerdata.put(entity.getUniqueId().toString(), m.group(group_Primary));
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public double execute(LivingEntity attacker, LivingEntity victim, HashMap<String, SpecialAttributeData> attackerattr, HashMap<String, SpecialAttributeData> victimattr, Event event) {
		
		if (!(attacker instanceof Player)) {
			return 0;
		}
		
		if (AttributesManager.isBypass((Player) attacker)) {
			return 1;
		}
		
		if (!cooldown.containsKey(attacker.getUniqueId().toString())) {
			cooldown.put(attacker.getUniqueId().toString(), System.currentTimeMillis());
			return 0;
		}
		
		Double cd;
		if (!playerdata.containsKey(attacker.getUniqueId().toString())) {
			cd = confdata.get("Normal")*1000D;
		}
		else {
			cd = confdata.get(playerdata.get(attacker.getUniqueId().toString()))*1000D;
		}
		
		Double cd2 = (double) (System.currentTimeMillis() - cooldown.get(attacker.getUniqueId().toString()));
		if (cd2 <= cd) {
			((EntityDamageByEntityEvent) event).setDamage(((EntityDamageByEntityEvent) event).getDamage() * (cd2/cd));
			
			return cd2/cd;
		}
		else {
			cooldown.put(attacker.getUniqueId().toString(), System.currentTimeMillis());
			return 1;
		}
	}
	
	public boolean canAttack(Player player) {
		if (!cooldown.containsKey(player.getUniqueId().toString())) {
			cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
			return true;
		}
		
		Double cd;
		if (!playerdata.containsKey(player.getUniqueId().toString())) {
			cd = confdata.get("Normal")*1000D;
		}
		else {
			cd = confdata.get(playerdata.get(player.getUniqueId().toString()))*1000D;
		}
		
		if (System.currentTimeMillis() - cooldown.get(player.getUniqueId().toString()) <= cd) {
			return false;
		}
		else {
			cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
			return true;
		}
	}
}