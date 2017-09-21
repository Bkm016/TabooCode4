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
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.netty.util.AttributeMap;
import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.events.TabooCodeCritEvent;
import me.skymc.taboocode4.events.TabooCodeDodgeEvent;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.TitleUtils;

public class DodgeChance implements SpecialAttribute {

	private FileConfiguration file;
	
	private Pattern Regular;
	private Pattern Percent;
	
	private Integer group_Regular;
	private Integer group_Percent;
	
	private String titlev;
	private String subtitlev;
	
	private String titlea;
	private String subtitlea;
	
	private SoundPack sound;
	
	private String Name = this.getClass().getSimpleName();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Regular", "[$]\\* Dodge Chance");
			conf.set("Settings.Percent", "[$]\\% Dodge Chance");
			
			conf.set("Settings.GroupLocation.Regular", 1);
			conf.set("Settings.GroupLocation.Percent", 1);
			
			conf.set("Settings.Messages.Sound", "ENTITY_HORSE_ARMOR-1-1");
			
			conf.set("Settings.Messages.Title.victim", "§3§lDodge !!");
			conf.set("Settings.Messages.Subtitle.victim", "§7You dodged the attack");
			
			conf.set("Settings.Messages.Title.attacker", "§3§lDodge !!");
			conf.set("Settings.Messages.Subtitle.attacker", "§7The target dodged the attack");
			
			conf.set("Settings.Priority", -1);
		}
		
		titlev = conf.getString("Settings.Messages.Title.victim");
		subtitlev = conf.getString("Settings.Messages.Subtitle.victim");
		
		titlea = conf.getString("Settings.Messages.Title.attacker");
		subtitlea = conf.getString("Settings.Messages.Subtitle.attacker");
		
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
		
		EntityDamageByEntityEvent e = ((EntityDamageByEntityEvent) event);
		
		Double value = AttributesManager.getRegularValue(victim, Name)/100;
		Double value2 = value + (value * AttributesManager.getPercentValue(victim, Name));
		
		if (TabooCode4.getRandom().nextDouble() <= value2) {
			
			TabooCodeDodgeEvent ee = new TabooCodeDodgeEvent(e);
			if (ee.isCancelled()) {
				return 0;
			}
			
			e.setDamage(0);
			e.setCancelled(true);
			
			if (victim instanceof Player) {
				sound.play((Player) victim);
				victim.playEffect(EntityEffect.HURT);
				TitleUtils.sendTitle((Player) victim, titlev, 10, 40, 10, subtitlev, 10, 40, 10);
			}
			
			if (attacker instanceof Player) {
				sound.play((Player) attacker);
				TitleUtils.sendTitle((Player) attacker, titlea, 10, 40, 10, subtitlea, 10, 40, 10);
			}
			
			return 0;
		}
		return 0;
	}
}