package me.skymc.taboocode4.command.sub1;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Export implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		ItemManager.exportConf(true);
		TabooCode4.send(sender, "物品导出成功");
		
		return true;
	}
	
}