package me.skymc.taboocode4.utils;

import org.bukkit.command.CommandSender;

public abstract interface SubCommandExecutor {
	
	public abstract boolean command(CommandSender sender, String[] args);

}