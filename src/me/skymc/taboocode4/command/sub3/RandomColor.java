package me.skymc.taboocode4.command.sub3;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class RandomColor implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			TabooCode4.send(sender, "璇ユ寚浠や笉鑳界敱鍚庡彴杈撳叆");
			return false;
		}
		
		Player player = (Player) sender;
		if (ItemFilter.isNull(player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "浣犱笉鑳戒慨鏀圭┖姘�");
			return false;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		
		int r = TabooCode4.getRandom().nextInt(255);
		int g = TabooCode4.getRandom().nextInt(255);
		int b = TabooCode4.getRandom().nextInt(255);
		
		meta.setColor(org.bukkit.Color.fromBGR(r, g, b));
		item.setItemMeta(meta);
		
		TabooCode4.send(sender, "璁剧疆棰滆壊: &f" + r + "-" + g + "-" + b);
		return true;
	}
	
	private enum LeatherType {
		
		LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_LEGGINGS;
	}
	
	private LeatherType parse(String s) {
		try {
			return LeatherType.valueOf(s);
		}
		catch (Exception e) {
			return null;
		}
	}

}