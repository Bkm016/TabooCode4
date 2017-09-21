package me.skymc.taboocode4.command.sub1;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Edit implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			TabooCode4.send(sender, "该指令不能由后台输入");
			return false;
		}
		
		if (args.length < 2) {
			TabooCode4.send(sender, "缺少必要变量");
			return false;
		}
		
		if (ItemManager.getItem(args[1]) == null) {
			TabooCode4.send(sender, "物品不存在, 使用指令 &c/tc4 save " + args[1] + " &7来储存物品");
			return false;
		}
		
		Player player = (Player) sender;
		if (ItemFilter.isNull(player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "你不能修改空气");
			return false;
		}
		
		if (ItemManager.setItem(args[1], player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "物品 &f" + args[1] + " &7保存成功");
		}
		else {
			TabooCode4.send(sender, "物品 &f" + args[1] + " &7与固定物品库重复");
		}
		return true;
	}
	
}