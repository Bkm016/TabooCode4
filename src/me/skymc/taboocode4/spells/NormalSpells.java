package me.skymc.taboocode4.spells;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.SpecialAttribute.DamageType;

public abstract interface NormalSpells {
	
	/**
	 * 属性注册抽象方法
	 * 
	 * @param conf
	 * @param first
	 * @return
	 */
	public abstract boolean register(FileConfiguration conf, boolean first);
	
	/**
	 * 右键点击 -》 判断关键字 -》 判断武器位置 -》 射出
	 */
	
	public abstract void execute(Player player, ItemStack item, String uncolouredlore);

}