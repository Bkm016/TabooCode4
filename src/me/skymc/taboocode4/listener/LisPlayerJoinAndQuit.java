package me.skymc.taboocode4.listener;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.library.EffLib;
import me.skymc.taboocode4.mana.ManaManager;

public class LisPlayerJoinAndQuit implements Listener { 
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		AttributesManager.specialAttributeData.remove(e.getPlayer().getUniqueId().toString());
		
		// MANA CLEAR
		ManaManager.getPlayerData().remove(e.getPlayer().getName());
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void join(PlayerJoinEvent e) {
		AttributesManager.specialAttributeData.put(e.getPlayer().getUniqueId().toString(), new HashMap<>());
		AttributesManager.removeBypass(e.getPlayer());
		
		// MANA RESET
		ManaManager.getPlayerData().put(e.getPlayer().getName(), 0D);
	}
	
	public void cmd(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() != EquipmentSlot.OFF_HAND && e.getItem().getType().equals(Material.BLAZE_ROD)) {
			e.setCancelled(true);
		}
	}

}