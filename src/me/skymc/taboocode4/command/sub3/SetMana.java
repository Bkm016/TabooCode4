package me.skymc.taboocode4.command.sub3;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.mana.ManaManager;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.SubCommandExecutor;
import me.skymc.taboocode4.utils.TitleUtils;
import net.md_5.bungee.api.ChatColor;

public class SetMana implements SubCommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (args.length < 2) {
			TabooCode4.send(sender, "缂哄皯蹇呰鍙橀噺");
			return false;
		}
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if (player == null) {
			TabooCode4.send(sender, "鐜╁涓嶅湪绾�");
			return false;
		}
		
		double manaregen = 0;
		
		try {
			manaregen = Double.valueOf(args[1]);
		}
		catch (Exception e) {
			TabooCode4.send(sender, "&f" + args[1] + " &7涓嶆槸涓�涓湁鏁堢殑鏁板瓧");
			return false;
		}
		
		double manamax = ManaManager.getMaxMana(player);
		
		if (manaregen > manamax) {
			ManaManager.getPlayerData().put(player.getName(), manamax);
		}
		else {
			ManaManager.getPlayerData().put(player.getName(), manaregen);
			
			if (ManaManager.isTitle()) {
				TitleUtils.sendTitle(player, "", 10, 30, 10, ManaManager.getManaString(player), 10, 30, 10);
			}
		}
		return true;
	}
	
}