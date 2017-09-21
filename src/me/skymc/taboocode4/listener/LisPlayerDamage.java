package me.skymc.taboocode4.listener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttribute.DamageType;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.attribute.sub.AttackSpeed;
import me.skymc.taboocode4.events.TabooCodeHoloSpawnEvent;

public class LisPlayerDamage implements Listener {
	
	public DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * 当 @非玩家生物 为攻击者时
	 * 
	 * @param e
	 */
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damage_entity(EntityDamageByEntityEvent e) {
		
		LivingEntity entity = null;
		
		if (e.getDamager() instanceof Arrow 
				|| e.getDamager() instanceof WitherSkull 
				|| e.getDamager() instanceof ShulkerBullet 
				|| e.getDamager() instanceof Fireball
				|| e.getDamager() instanceof LargeFireball) {
			if (((Projectile) e.getDamager()).getShooter() == null) {
				return;
			}
			
			if (((Projectile) e.getDamager()).getShooter() instanceof LivingEntity) {
				entity = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
			}
		}
		if (e.getDamager() instanceof LivingEntity) {
			entity = (LivingEntity) e.getDamager();
		}
		
		if (entity == null || entity instanceof Player || TabooCode4.getDisableWorlds().contains(e.getEntity().getWorld().getName())) {
			return;
		}
		
		if (e.getEntity() instanceof Player) {
			
			for (SpecialAttribute attr : TabooCode4.getSpecialAttributes()) {
				/**
				 *  当怪物攻击玩家且拥有属性时，触发怪物的攻击效果，这里 @攻击者属性 及 @被攻击者属性 一定存在
				 */
				if (attr.getType() == DamageType.ATTACK && AttributesManager.specialAttributeData.containsKey(entity.getUniqueId().toString())) {
					
					attr.execute(entity, (Player) e.getEntity(), AttributesManager.getDataMap(entity), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
				}
				/**
				 *  当玩家收到伤害时，触发被动效果，这里 @被攻击者属性 一定存在， @攻击者属性 不一定存在
				 */
				if (attr.getType() == DamageType.DAMAGED) {
					
					if (AttributesManager.specialAttributeData.containsKey(entity.getUniqueId().toString())) {
						attr.execute(entity, (Player) e.getEntity(), AttributesManager.getDataMap(entity), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
					}
					else {
						attr.execute(entity, (Player) e.getEntity(), null, AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
					}
				}
			}
			if (e.getDamage() < TabooCode4.getBottomDamage()) {
				e.setDamage(TabooCode4.getBottomDamage());
			}
		}
	}
	
	/**
	 * 当 @玩家 为攻击者时
	 * 
	 * @param e
	 */
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damage_player(EntityDamageByEntityEvent e) {
		Player player = null;
		
		if (e.getDamager() instanceof Arrow 
				|| e.getDamager() instanceof WitherSkull 
				|| e.getDamager() instanceof ShulkerBullet 
				|| e.getDamager() instanceof Fireball
				|| e.getDamager() instanceof LargeFireball) {
			if (((Projectile) e.getDamager()).getShooter() == null) {
				return;
			}
			
			if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
				player = (Player) ((Projectile) e.getDamager()).getShooter();
			}
		}
		if (e.getDamager() instanceof Player) {
			player = (Player) e.getDamager();
		}
		if (player == null || e.getEntity().hasMetadata("NPC") || !(e.getEntity() instanceof LivingEntity) || TabooCode4.getDisableWorlds().contains(player.getWorld().getName())) {
			return;
		}
		
		long time = System.currentTimeMillis();
		
		Double attackspeedvalue = 1D;
		
		LinkedHashMap<String, Double> holodata = new LinkedHashMap<>();
		holodata.put("default", e.getDamage());
		
		for (SpecialAttribute attr : TabooCode4.getSpecialAttributes()) {
			
			if (attr.getType() == DamageType.ATTACK) {
				Double value = 0D;
				
				if (e.getEntity() instanceof Player) {
					value = attr.execute(player, (Player) e.getEntity(), AttributesManager.getDataMap(player), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
				}
				else {
					if (AttributesManager.specialAttributeData.containsKey(e.getEntity().getUniqueId().toString())) {
						value = attr.execute(player, (LivingEntity) e.getEntity(), AttributesManager.getDataMap(player), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
					}
					else {
						value = attr.execute(player, (LivingEntity) e.getEntity(), AttributesManager.getDataMap(player), null, e);
					}
				}
				
				if (attr.getClass().getSimpleName().equals(AttackSpeed.class.getSimpleName())) {
					attackspeedvalue = value;
				}
				else {
					if (TabooCode4.isHolo() && value > 0) {
						
						if (holodata.containsKey(attr.getHoloSpecies())) {
							holodata.put(attr.getHoloSpecies(), holodata.get(attr.getHoloSpecies()) + value);
						}
						else {
							holodata.put(attr.getHoloSpecies(), value);
						}
					}
				}
			}
			if (attr.getType() == DamageType.DAMAGED) {
				
				if (e.getEntity() instanceof Player) {
					attr.execute(player, (Player) e.getEntity(), AttributesManager.getDataMap(player), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
				}
				else {
					if (AttributesManager.specialAttributeData.containsKey(e.getEntity().getUniqueId().toString())) {
						attr.execute(player, (LivingEntity) e.getEntity(), AttributesManager.getDataMap(player), AttributesManager.getDataMap((LivingEntity) e.getEntity()), e);
					}
					else {
						attr.execute(player, (LivingEntity) e.getEntity(), AttributesManager.getDataMap(player), null, e);
					}
				}
			}
		}
		
		if (e.isCancelled()) {
			return;
		}
		
		if (e.getDamage() <= TabooCode4.getBottomDamage()) {
			e.setDamage(TabooCode4.getBottomDamage());
		}
		
		if (TabooCode4.isHolo()) {
			String uid = String.valueOf(System.currentTimeMillis());
			
			Location loc = e.getEntity().getLocation().add(0, 2, 0);
			loc.add(Vector.getRandom());
			
			Hologram holo = HologramsAPI.createHologram(TabooCode4.getInst(), loc);
			
			TabooCode4.getHoloData().put(uid, holo);
			Bukkit.getPluginManager().callEvent(new TabooCodeHoloSpawnEvent(player, (LivingEntity) e.getEntity(), holo, holodata, e));
			
			Iterator<String> i = holodata.keySet().iterator(); 
			while (i.hasNext()) {
				String type = i.next();
				if (holodata.get(type) > 0) {
					holo.appendTextLine(TabooCode4.getHoloSpeciesString(type).replace("%d%", df.format(holodata.get(type) * attackspeedvalue)));
				}
			}
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					if (!holo.isDeleted()) {
						holo.delete();
					}
					TabooCode4.getHoloData().remove(uid);
				}
			}.runTaskLater(TabooCode4.getInst(), TabooCode4.getHoloStayTicks());
		}
		
		if (TabooCode4.isDebug()) {
			TabooCode4.send("本次伤害事件处理耗时: " + (System.currentTimeMillis() - time) + " 毫秒");
		}
	}

}