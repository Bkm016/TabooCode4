package me.skymc.taboocode4.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TabooCodeDodgeEvent extends Event implements Cancellable{

	public static final HandlerList handlers = new HandlerList();
	public EntityDamageByEntityEvent event;
	
	public boolean canceled;

	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList() 
	{
		return handlers;
	}
	
	public TabooCodeDodgeEvent(EntityDamageByEntityEvent e)
	{
		canceled = false;
		event = e;
	}
	
	public EntityDamageByEntityEvent getEvent()
	{
		return event;
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		canceled = arg0;
	}

}