package me.skymc.taboocode4.attribute.sub;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.events.TabooCodeCritEvent;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.TitleUtils;

public class CritChance implements SpecialAttribute {

	private FileConfiguration file;
	
	private Pattern Regular;
	private Pattern Percent;
	
	private Integer group_Regular;
	private Integer group_Percent;
	
	private String HoloSpecies;
	
	private SoundPack sound;
	
	private String title;
	private String subtitle;
	
	private Double damage;
	
	private String Name = this.getClass().getSimpleName();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Regular", "[$]\\* Crit Chance");
			conf.set("Settings.Percent", "[$]\\% Crit Chance");
			conf.set("Settings.HoloSpecies", "default");
			
			conf.set("Settings.GroupLocation.Regular", 1);
			conf.set("Settings.GroupLocation.Percent", 1);
			
			conf.set("Settings.HoloSpecies", "default");
			
			conf.set("Settings.AdditionalDamage", 2.0);
			
			conf.set("Settings.Messages.Sound", "ENTITY_BAT_DEATH-1-1");
			conf.set("Settings.Messages.Title", "§a§lCrit !!");
			conf.set("Settings.Messages.Subtitle", "");
		}
		
		HoloSpecies = conf.getString("Settings.HoloSpecies");
		
		title = conf.getString("Settings.Messages.Title");
		subtitle = conf.getString("Settings.Messages.Subtitle");
		
		damage = conf.getDouble("Settings.AdditionalDamage");

		Regular = Pattern.compile(conf.getString("Settings.Regular").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		Percent = Pattern.compile(conf.getString("Settings.Percent").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		
		group_Regular = conf.getInt("Settings.GroupLocation.Regular");
		group_Percent = conf.getInt("Settings.GroupLocation.Percent");
		
		sound = new SoundPack(conf.getString("Settings.Messages.Sound"));
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
		return HoloSpecies;
	}

	@Override
	public SpecialAttributeData getAttribute(LivingEntity entity, ItemStack item, List<String> uncoloredlores) {
		
		Double[] primary = new Double[] { 0D, 0D };
		Double regular = 0D;
		Double percent = 0D;
		
		for (String lore : uncoloredlores) {
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
		
		if (attackerattr == null) {
			return 0;
		}
		
		EntityDamageByEntityEvent e = ((EntityDamageByEntityEvent) event);
		
		Double value = AttributesManager.getRegularValue(attacker, Name)/100;
		Double value2 = value + (value * AttributesManager.getPercentValue(attacker, Name));
		
		if (TabooCode4.getRandom().nextDouble() <= value2) {
			
			TabooCodeCritEvent ee = new TabooCodeCritEvent(e);
			if (ee.isCancelled()) {
				return 0;
			}
			
			e.getEntity().getWorld().createExplosion(victim.getLocation(), 0, false);
			e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victim.getEyeLocation(), 1);
			
			Double damage = AttributesManager.getPrimaryValue(attacker, "CritDamage") + AttributesManager.getRegularValue(attacker, "CritDamage");
			Double damage2 = AttributesManager.getPercentValue(attacker, "CritDamage");
			
			if (attacker instanceof Player) {
				sound.play((Player) attacker);
				attacker.playEffect(EntityEffect.HURT);
				TitleUtils.sendTitle((Player) attacker, title, 10, 40, 10, subtitle, 10, 40, 10);
			}
			
			Double cirtdamage = e.getDamage() * this.damage;
			
			if (victimattr == null) {
				Double number = (cirtdamage + damage) + ((cirtdamage + damage) * damage2) - e.getDamage();
				
				e.setDamage(e.getDamage() + number);
				return number;
			}
			else {
				Double defense = AttributesManager.getPrimaryValue(victim, "CritDefense") + AttributesManager.getRegularValue(victim, "CritDefense");
				Double defense2 = defense + (defense * AttributesManager.getPercentValue(victim, "CritDefense"));
				
				Double number = (cirtdamage + damage) + ((cirtdamage + damage) * damage2) - defense2 - e.getDamage();
				
				e.setDamage(e.getDamage() + number);
				return number;
			}
		}
		return 0;
	}

}