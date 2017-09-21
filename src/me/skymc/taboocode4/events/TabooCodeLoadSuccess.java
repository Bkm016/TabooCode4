package me.skymc.taboocode4.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TabooCodeLoadSuccess extends Event{

	public static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList() 
	{
		return handlers;
	}
}