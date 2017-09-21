package me.skymc.taboocode4.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TabooCodeCritEvent extends Event implements Cancellable{

	public static final HandlerList handlers = new HandlerList();
	public EntityDamageByEntityEvent event;
	
	public boolean cancel;

	public TabooCodeCritEvent(EntityDamageByEntityEvent e)
	{
		cancel = false;
		event = e;
	}
	
	public EntityDamageByEntityEvent getEvent()
	{
		return event;
	}
 
	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList() 
	{
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancel = arg0;
	}

}