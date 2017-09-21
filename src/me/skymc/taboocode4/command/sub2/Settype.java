package me.skymc.taboocode4.command.sub2;

import org.bukkit.Material;
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

public class Settype implements SubCommandExecutor {

	@SuppressWarnings("deprecation")
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
		
		try {
			item.setType(Material.valueOf(args[1]));
			TabooCode4.send(sender, "设置材质: &f" + Material.valueOf(args[1].toUpperCase()));
		}
		catch (Exception e) {
			try {
				item.setTypeId(Integer.valueOf(args[1]));
				TabooCode4.send(sender, "设置材质: &f" + Integer.valueOf(args[1]));
			}
			catch (Exception e2) {
				TabooCode4.send(sender, "&f" + args[1] + " &7不是一个有效的材质");
				return false;
			}
		}
		
		
		new SoundPack(Sound.BLOCK_ANVIL_USE, 1F, 1F).play(player);
		return true;
	}
	
}