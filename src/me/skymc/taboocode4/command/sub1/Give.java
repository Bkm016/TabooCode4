package me.skymc.taboocode4.command.sub1;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Give implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (args.length < 2) {
			TabooCode4.send(sender, "缺少必要变量");
			return false;
		}
		
		if (ItemManager.getItem(args[1]) == null) {
			TabooCode4.send(sender, "物品不存在, 使用指令 &c/tc4 save " + args[1] + " &7来储存物品");
			return false;
		}
		
		ItemStack item = ItemManager.getFinishItem(args[1]);
		Integer amount = 1;
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		
		if (args.length > 2) {
			player = Bukkit.getPlayerExact(args[2]);
			if (player == null) {
				TabooCode4.send(sender, "玩家 &f" + args[2] + " &7不在线");
				return false;
			}
		}
		else if (player == null) {
			TabooCode4.send(sender, "请输入正确的玩家名");
			return false;
		}
		
		if (args.length > 3) {
			try {
				amount = Integer.valueOf(args[3]);
			}
			catch (Exception e) {
				TabooCode4.send(sender, "数量 &f" + args[3] + " &7不是一个有效的数字");
				return false;
			}
		}
		
		item.setAmount(amount);
		player.getInventory().addItem(item);
		
		if (sender instanceof Player) {
			TabooCode4.send(sender, "物品 &f" + args[1] + " &7已发送至 &f" + player.getName() + " &7背包");
		}
		return true;
	}
	
}