package me.skymc.taboocode4.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisEntityEquip implements Listener {
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void equ(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Player) {
			return;
		}
		
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		new BukkitRunnable() {
		
			public void run() {
				
				if (e.getEntity().getCustomName() == null) {
					return;
				}
				
				AttributesManager.update_mob((LivingEntity) e.getEntity());
			}
		}.runTask(TabooCode4.getInst());
	}

	@EventHandler
	public void eve(EntityTargetLivingEntityEvent e) {
		if (!(e.getEntity() instanceof Player) && e.getEntity() instanceof LivingEntity) {
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					AttributesManager.update_mob((LivingEntity) e.getEntity());
				}
			}.runTask(TabooCode4.getInst());
		}
	}
	
	@EventHandler
	public void eve(EntityDeathEvent e) {
		AttributesManager.specialAttributeData.remove(e.getEntity().getUniqueId().toString());
	}
}