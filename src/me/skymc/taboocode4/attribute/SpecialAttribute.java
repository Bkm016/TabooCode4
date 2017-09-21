package me.skymc.taboocode4.attribute;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract interface SpecialAttribute {
	
	/**
	 * 属性注册抽象方法
	 * 
	 * @param conf
	 * @param first
	 * @return
	 */
	public abstract boolean register(FileConfiguration conf, boolean first);
	
	/**
	 * 获取属性优先级
	 * 
	 * @return
	 */
	public abstract int getPriority();
	
	/**
	 * 获取属性生效类型 ( ATTACK || DAMAGED )
	 * 
	 * @return
	 */
	public abstract DamageType getType();
	
	/**
	 * 获取属性全息类型 ( ATTACK ONLY )
	 * 
	 * @return
	 */
	public abstract String getHoloSpecies();
	
	/**
	 * 属性获取方法
	 * 
	 * @param player 持有者
	 * @param item 传递物品
	 * 
	 * @return 最终属性值
	 */
	public abstract SpecialAttributeData getAttribute(LivingEntity entity, ItemStack item, List<String> uncoloredlores);
	
	/**
	 * 属性执行方法（伤害事件时）
	 * 
	 * @param player 攻击玩家
	 * @param victim 攻击目标
	 * @param attribute 属性值
	 * @param event 当前事件
	 */
	public abstract double execute(LivingEntity attacker, LivingEntity victim, HashMap<String, SpecialAttributeData> attackerattr, HashMap<String, SpecialAttributeData> victimattr, Event event);
	
	public enum DamageType {
		
		ATTACK, DAMAGED, UPDATE, NONE;
	}
}