package me.skymc.taboocode4.command.sub1;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Info implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			TabooCode4.send(sender, "该指令不能由后台输入");
			return false;
		}
		
		Player player = (Player) sender;
		if (ItemFilter.isNull(player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "你不查看空气");
			return false;
		}

		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		
		TabooCode4.send(sender, "&f&m                                  ");
		TabooCode4.send(sender, "物品材质: &f" + item.getType() + "(" + item.getTypeId() + ":" + item.getDurability() + ")");
		if (meta.hasDisplayName()) {
			TabooCode4.send(sender, "物品名称: &f" + meta.getDisplayName());
		}
		if (meta.hasLore()) {
			TabooCode4.send(sender, "物品描述: &f");
			
			int l = 0;
			for (String lore : meta.getLore()) {
				l++;
				TabooCode4.send(sender, " &7" + l + ". &f" + lore);
			}
		}
		if (meta.hasEnchants()) {
			TabooCode4.send(sender, "物品附魔: &f");
			
			Iterator<Enchantment> i = meta.getEnchants().keySet().iterator();
			while (i.hasNext()) {
				Enchantment enc = i.next();
				Integer level = meta.getEnchants().get(enc);
				TabooCode4.send(sender, " &7- &8" + enc.getName() + "(&7" + enc.getId() + "&8):&f " + level);
			}
		}
		if (meta.getItemFlags().size() > 0) {
			TabooCode4.send(sender, "物品标签: &f");
			
			for (ItemFlag flag : meta.getItemFlags()) {
				TabooCode4.send(sender, " &7- &f" + flag);
			}
		}
		
		TabooCode4.send(sender, "&f&m                                  ");
		
		return true;
	}
	
}