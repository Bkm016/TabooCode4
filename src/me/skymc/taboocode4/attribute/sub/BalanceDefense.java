package me.skymc.taboocode4.attribute.sub;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.netty.util.AttributeMap;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class BalanceDefense implements SpecialAttribute {

	private FileConfiguration file;
	
	private Pattern Primary;
	private Pattern Regular;
	private Pattern Percent;
	
	private Integer group_Primary1;
	private Integer group_Primary2;
	private Integer group_Regular;
	private Integer group_Percent;
	
	private String Name = this.getClass().getSimpleName();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Primary", "Balance Defense: [$][?][$]");
			conf.set("Settings.Regular", "[$]\\* Balance Defense");
			conf.set("Settings.Percent", "[$]\\% Balance Defense");
			
			conf.set("Settings.GroupLocation.Primary-min", 1);
			conf.set("Settings.GroupLocation.Primary-max", 2);
			conf.set("Settings.GroupLocation.Regular", 1);
			conf.set("Settings.GroupLocation.Percent", 1);
		}

		Primary = Pattern.compile(conf.getString("Settings.Primary").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		Regular = Pattern.compile(conf.getString("Settings.Regular").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		Percent = Pattern.compile(conf.getString("Settings.Percent").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		
		group_Primary1 = conf.getInt("Settings.GroupLocation.Primary-min");
		group_Primary2 = conf.getInt("Settings.GroupLocation.Primary-max");
		group_Regular = conf.getInt("Settings.GroupLocation.Regular");
		group_Percent = conf.getInt("Settings.GroupLocation.Percent");
		
		return true;
	}
	
	@Override
	public int getPriority() {
		return file.getInt("Settings.Priority");
	}

	@Override
	public DamageType getType() {
		return DamageType.DAMAGED;
	}

	@Override
	public String getHoloSpecies() {
		return null;
	}

	@Override
	public SpecialAttributeData getAttribute(LivingEntity entity, ItemStack item, List<String> uncoloredlores) {
		
		Double[] primary = new Double[] { 0D, 0D };
		Double regular = 0D;
		Double percent = 0D;
		
		for (String lore : uncoloredlores) {
			Matcher m = Primary.matcher(lore);
			if (m.find()) {
				primary[0] = Double.valueOf(m.group(group_Primary1));
				primary[1] = Double.valueOf(m.group(group_Primary2));
			}
			
			Matcher m2 = Regular.matcher(lore);
			if (m2.find()) {
				regular += Double.valueOf(m2.group(group_Regular));
			}
			
			Matcher m3 = Percent.matcher(lore);
			if (m3.find()) {
				percent += Double.valueOf(m3.group(group_Percent));
			}
		}
		
		return new SpecialAttributeData(primary, regular, percent);
	}

	@Override
	public double execute(LivingEntity attacker, LivingEntity victim, HashMap<String, SpecialAttributeData> attackerattr, HashMap<String, SpecialAttributeData> victimattr, Event event) {
		
		if (((EntityDamageByEntityEvent) event).getDamager() instanceof LivingEntity) {
			
			Double def = AttributesManager.getPrimaryValue(victim, Name) + AttributesManager.getRegularValue(attacker, Name);
			Double def2 = def + (def * AttributesManager.getPercentValue(victim, Name));
			
			((EntityDamageByEntityEvent) event).setDamage(((EntityDamageByEntityEvent) event).getDamage() - def2);
		}
		return 0;
	}
}