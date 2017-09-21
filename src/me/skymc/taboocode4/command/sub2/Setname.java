package me.skymc.taboocode4.command.sub2;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.SubCommandExecutor;
import net.md_5.bungee.api.ChatColor;

public class Setname implements SubCommandExecutor {

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
		
		Player player = (Player) sender;
		if (ItemFilter.isNull(player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "你不能修改空气");
			return false;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		
		String name = "";
		
		for (int j = 1; j < args.length; j++) {
			name += args[j] + " ";
		}
		
		name = ChatColor.translateAlternateColorCodes('&', name.substring(0, name.length() - 1));
			
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		
		TabooCode4.send(sender, "设置展示名: &f" + name);
		new SoundPack(Sound.BLOCK_ANVIL_USE, 1F, 1F).play(player);
		return true;
	}
	
}