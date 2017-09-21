package me.skymc.taboocode4.command.sub2;

import java.util.ArrayList;
import java.util.List;

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

public class Addlore implements SubCommandExecutor {

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
		
		String value = "";
		
		for (int j = 1; j < args.length; j++) {
			value += args[j] + " ";
		}
		
		value = ChatColor.translateAlternateColorCodes('&', value.substring(0, value.length() - 1));
			
		List<String> lore = new ArrayList<>();
		if (meta.hasLore()) {
			lore = meta.getLore();
		}
		
		lore.add(value);
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		TabooCode4.send(sender, "添加描述: &f" + value);
		new SoundPack(Sound.BLOCK_ANVIL_USE, 1F, 1F).play(player);
		return true;
	}
	
}