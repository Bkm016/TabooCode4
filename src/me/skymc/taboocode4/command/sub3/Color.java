package me.skymc.taboocode4.command.sub3;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.utils.SubCommandExecutor;

public class Color implements SubCommandExecutor {

	@Override
	public boolean command(CommandSender sender, String[] args) {
		
		if (!(sender instanceof Player)) {
			TabooCode4.send(sender, "璇ユ寚浠や笉鑳界敱鍚庡彴杈撳叆");
			return false;
		}
		
		if (args.length < 2) {
			TabooCode4.send(sender, "缂哄皯蹇呰鍙橀噺");
			return false;
		}
		
		Player player = (Player) sender;
		if (ItemFilter.isNull(player.getInventory().getItemInMainHand())) {
			TabooCode4.send(sender, "浣犱笉鑳戒慨鏀圭┖姘�");
			return false;
		}
		
		if (args[1].split("-").length == 3) {
			
			ItemStack item = player.getInventory().getItemInMainHand();
			if (parse(item.getType().toString()) == null) {
				TabooCode4.send(sender, "浣犲彧鑳芥洿鏀圭毊闈╄澶囩殑棰滆壊");
				return false;
			}
			else {
				try {
					if (Integer.valueOf(args[1].split("-")[0]) < 0 || Integer.valueOf(args[1].split("-")[0]) > 255) {
						TabooCode4.send(sender, "棰滆壊浠ｇ爜鍙兘鍦� 0-255 涔嬮棿");
						return false;
					}
					if (Integer.valueOf(args[1].split("-")[1]) < 0 || Integer.valueOf(args[1].split("-")[1]) > 255) {
						TabooCode4.send(sender, "棰滆壊浠ｇ爜鍙兘鍦� 0-255 涔嬮棿");
						return false;
					}
					if (Integer.valueOf(args[1].split("-")[2]) < 0 || Integer.valueOf(args[1].split("-")[2]) > 255) {
						TabooCode4.send(sender, "棰滆壊浠ｇ爜鍙兘鍦� 0-255 涔嬮棿");
						return false;
					}
				}
				catch (Exception e) {
					TabooCode4.send(sender, "璇疯緭鍏ユ纭殑棰滆壊搴忓彿 NNN-NNN-NNN &8(100-100-100)");
					return false;
				}
				
				LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
				
				meta.setColor(org.bukkit.Color.fromBGR(Integer.valueOf(args[1].split("-")[0]), Integer.valueOf(args[1].split("-")[1]), Integer.valueOf(args[1].split("-")[2])));
				item.setItemMeta(meta);
				
				TabooCode4.send(sender, "璁剧疆棰滆壊: &f" + args[1]);
				return true;
			}
		}
		
		TabooCode4.send(sender, "璇疯緭鍏ユ纭殑棰滆壊搴忓彿 NNN-NNN-NNN &8(100-100-100)");
		return false;
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