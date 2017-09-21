package me.skymc.taboocode4.command.sub1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Find implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (args.length < 2) {
			TabooCode4.send(sender, "缺少必要变量");
			return false;
		}

		List<String> item = ItemManager.getItems().keySet().stream().collect(Collectors.toList());
		List<String> itemdir = ItemManager.getItemsDir().keySet().stream().collect(Collectors.toList());

		List<String> finditem = new ArrayList<>();
		List<String> finditemdir = new ArrayList<>();
		
		TabooCode4.send(sender, "&f&m                                  ");
		TabooCode4.send(sender, "匹配名称: \"&f" + args[1] + "&7\"");
		
		for (String name : item) {
			if (name.contains(args[1])) {
				finditem.add(name);
			}
		}
		for (String name : itemdir) {
			if (name.contains(args[1])) {
				finditemdir.add(name);
			}
		}
		
		if (item.size() == 0 && itemdir.size() == 0) {
			TabooCode4.send(sender, "你还没有保存任何物品");
		}
		else if (finditem.size() == 0 && finditemdir.size() == 0) {
			TabooCode4.send(sender, "没有找到符合要求的物品");
		}
		else {
			TabooCode4.send(sender, "匹配数量: &f" + finditem.size() + "&c(&4" + finditem.size() + "&c)" + " &7条");
			TabooCode4.send(sender, "匹配物品: &8");
			for (String name : finditem) {
				TabooCode4.send(sender, "&f - &7" + name);
			}
			for (String name : finditemdir) {
				TabooCode4.send(sender, "&f - &4" + name);
			}
		}
		
		TabooCode4.send(sender, "&f&m                                  ");
		return true;
	}
	
}