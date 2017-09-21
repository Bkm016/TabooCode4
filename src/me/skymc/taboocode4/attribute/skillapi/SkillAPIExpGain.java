package me.skymc.taboocode4.attribute.skillapi;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.event.PlayerManaGainEvent;

import io.netty.util.AttributeMap;
import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.events.TabooCodeCritEvent;
import me.skymc.taboocode4.events.TabooCodeDodgeEvent;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.TitleUtils;

public class SkillAPIExpGain implements SpecialAttribute, Listener {

	private FileConfiguration file;
	
	private Pattern Percent;
	
	private Integer group_Percent;
	
	private String Name = this.getClass().getSimpleName();
	
	@EventHandler
	public void eve(PlayerExperienceGainEvent e) {
		Double value = AttributesManager.getPercentValue(e.getPlayerData().getPlayer(), Name);
		Double value2 = e.getExp() + (e.getExp() * value);
		
		if (e.getExp() >= 0 && value2 >= 0) {
			e.setExp(value2.intValue());
		}
	}
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Percent", "[$]\\% SkillAPI ExpGain");
			
			conf.set("Settings.GroupLocation.Percent", 1);
		}
		
		Percent = Pattern.compile(conf.getString("Settings.Percent").replace("[$]", "((?:\\-|\\+)?\\d+(?:\\.\\d+)?)").replace("[?]", "(?:\\s|\\S)"));
		
		group_Percent = conf.getInt("Settings.GroupLocation.Percent");
		
		if (Bukkit.getPluginManager().getPlugin("SkillAPI") == null) {
			return false;
		}
		
		Bukkit.getPluginManager().registerEvents(this, TabooCode4.getInst());
		return true;
	}
	
	@Override
	public int getPriority() {
		return file.getInt("Settings.Priority");
	}

	@Override
	public DamageType getType() {
		return DamageType.NONE;
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
			Matcher m3 = Percent.matcher(lore);
			
			if (m3.find()) {
				percent += Double.valueOf(m3.group(group_Percent));
			}
		}
		
		return new SpecialAttributeData(primary, regular, percent);
	}

	@Override
	public double execute(LivingEntity attacker, LivingEntity victim, HashMap<String, SpecialAttributeData> attackerattr, HashMap<String, SpecialAttributeData> victimattr, Event event) {
		return 0;
	}
}