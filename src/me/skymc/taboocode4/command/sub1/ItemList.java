package me.skymc.taboocode4.command.sub1;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class ItemList implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		List<String> item = ItemManager.getItems().keySet().stream().collect(Collectors.toList());
		List<String> itemdir = ItemManager.getItemsDir().keySet().stream().collect(Collectors.toList());

		TabooCode4.send(sender, "&f&m                                  ");
		TabooCode4.send(sender, "物品总数: &f" + item.size() + "&c(&4" + itemdir.size() + "&c)" + " &7条");
		TabooCode4.send(sender, "物品列表:");
		
		for (String name : item) {
			TabooCode4.send(sender, "&f - &7" + name);
		}
		for (String name : itemdir) {
			TabooCode4.send(sender, "&f - &4" + name);
		}
		
		if (item.size() == 0) {
			TabooCode4.send(sender, "你还没有保存任何物品");
		}
		
		TabooCode4.send(sender, "&f&m                                  ");
		
		return true;
	}
	
}