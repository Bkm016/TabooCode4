package me.skymc.taboocode4.events;

import java.util.LinkedHashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class TabooCodeHoloSpawnEvent extends Event{

	public static final HandlerList handlers = new HandlerList();
	public LivingEntity attacker;
	public LivingEntity victim;
	public Hologram holo;
	LinkedHashMap<String, Double> holodata;
	public EntityDamageByEntityEvent event;
	
	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList() 
	{
		return handlers;
	}
	
	public TabooCodeHoloSpawnEvent(LivingEntity a, LivingEntity v, Hologram h, LinkedHashMap<String, Double> data, EntityDamageByEntityEvent e)
	{
		attacker = a;
		victim = v;
		holo = h;
		event = e;
		holodata = data;
	}
	
	public LinkedHashMap<String, Double> getHoloData() {
		return holodata;
	}
	
	public EntityDamageByEntityEvent getEvent() {
		return event;
	}
	
	public LivingEntity getAttacker() {
		return attacker;
	}
	
	public LivingEntity getVictim() {
		return victim;
	}
	
	public Hologram getHolo() {
		return holo;
	}

}