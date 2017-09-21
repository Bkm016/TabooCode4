package me.skymc.taboocode4.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.TabooCode4Loader;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.command.sub1.Edit;
import me.skymc.taboocode4.command.sub1.Export;
import me.skymc.taboocode4.command.sub1.Find;
import me.skymc.taboocode4.command.sub1.Give;
import me.skymc.taboocode4.command.sub1.Import;
import me.skymc.taboocode4.command.sub1.Info;
import me.skymc.taboocode4.command.sub1.ItemList;
import me.skymc.taboocode4.command.sub1.Mold;
import me.skymc.taboocode4.command.sub1.Save;
import me.skymc.taboocode4.command.sub2.Addlore;
import me.skymc.taboocode4.command.sub2.Dellore;
import me.skymc.taboocode4.command.sub2.Setdata;
import me.skymc.taboocode4.command.sub2.Setlore;
import me.skymc.taboocode4.command.sub2.Setname;
import me.skymc.taboocode4.command.sub2.Settype;
import me.skymc.taboocode4.command.sub3.AddMana;
import me.skymc.taboocode4.command.sub3.Color;
import me.skymc.taboocode4.command.sub3.RandomColor;
import me.skymc.taboocode4.command.sub3.SetMana;
import me.skymc.taboocode4.events.TabooCodeLoadSuccess;
import me.skymc.taboocode4.identifie.IdentifieManager;
import me.skymc.taboocode4.listener.LisPlayerPickup;
import me.skymc.taboocode4.mana.ManaManager;
import ru.endlesscode.rpginventory.RPGInventory;

public class MainCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (args.length == 0) {
			
			sender.sendMessage("§b§l------ TabooCode4 Commands ------");
			sender.sendMessage("");
			sender.sendMessage(" §b/tc4 save §8[name] §f- §7save the tc4 item");
			sender.sendMessage(" §b/tc4 edit §8[name] §f- §7edit the tc4 item");
			sender.sendMessage(" §b/tc4 mold §8[name] §f- §7give the model of tc4 item");
			sender.sendMessage(" §b/tc4 find §8[name] §f- §7find the name from tc4 items");
			sender.sendMessage(" §b/tc4 give §8[name] [player] [amount] §f- §7give the tc4 item");
			sender.sendMessage(" §b/tc4 list/info §f- §7check all tc4 items/item in hand");
			sender.sendMessage(" §b/tc4 import/export §f- §7import/export items by Configuration");
			sender.sendMessage("");
			sender.sendMessage(" §3/tc4 setname §8[text] §f- §7set name of item in hand");
			sender.sendMessage(" §3/tc4 addlore §8[text] §f- §7add lore to item in hand");
			sender.sendMessage(" §3/tc4 dellore §8[line] §f- §7remove lore from item in hand");
			sender.sendMessage(" §3/tc4 setlore §8[line] [text] §f- §7set line of lore of item in hand");
			sender.sendMessage(" §3/tc4 settype §8[type] §f- §7change type of item in hand");
			sender.sendMessage(" §3/tc4 setdata §8[data] §f- §7change data of item in hand");
			sender.sendMessage("");
			sender.sendMessage(" §b/tc4 color §8[NNN-NNN-NNN] §f- §7set colour of item in hand");
			sender.sendMessage(" §b/tc4 randomcolor §f- §7set random colour of item in hand");
			sender.sendMessage("");
			sender.sendMessage(" §3/tc4 addmana §8[name] [mana] §f- §7add mana to player");
			sender.sendMessage(" §3/tc4 setmana §8[name] [mana] §f- §7set mana for player");
			return false;
		}
		
		switch (parse(args[0].toUpperCase())) {
			case SAVE: {
				return new Save().command(sender, args);
			}
			case EDIT: {
				return new Edit().command(sender, args);
			}
			case MOLD: {
				return new Mold().command(sender, args);
			}
			case GIVE: {
				return new Give().command(sender, args);
			}
			case LIST: {
				return new ItemList().command(sender, args);
			}
			case FIND: {
				return new Find().command(sender, args);
			}
			case INFO: {
				return new Info().command(sender, args);
			}
			case IMPORT: {
				return new Import().command(sender, args);
			}
			case EXPORT: {
				return new Export().command(sender, args);
			}
			case SETNAME: {
				return new Setname().command(sender, args);
			}
			case ADDLORE: {
				return new Addlore().command(sender, args);
			}
			case DELLORE: {
				return new Dellore().command(sender, args);
			}
			case SETLORE: {
				return new Setlore().command(sender, args);
			}
			case SETTYPE: {
				return new Settype().command(sender, args);
			}
			case SETDATA: {
				return new Setdata().command(sender, args);
			}
			case COLOR: {
				return new Color().command(sender, args);
			}
			case RANDOMCOLOR: {
				return new RandomColor().command(sender, args);
			}
			case ADDMANA: {
				return new AddMana().command(sender, args);
			}
			case SETMANA: {
				return new SetMana().command(sender, args);
			}
			case RELOAD: {
				TabooCode4.loadConfig();
				
				TabooCode4Loader.loadAttributes();
				AttributesManager.loadManager();
				IdentifieManager.loadIdentifiePacks();
				TabooCode4.updatePriority(); 
				
				ManaManager.startRunnable();
				LisPlayerPickup.load();

				Bukkit.getPluginManager().callEvent(new TabooCodeLoadSuccess());
				TabooCode4.send(sender, "relaod ok");
				return true;
			}
			default: {
				TabooCode4.send(sender, "未知的指令");
				return false;
			}
		}
	}
	
	private enum command {
		
		HELP, ERROR, RELOAD,
		
		SAVE, EDIT, GIVE, MOLD, LIST, FIND, INFO, IMPORT, EXPORT,
		
		SETNAME, ADDLORE, DELLORE, SETLORE,
		
		SETTYPE, SETDATA,
		
		ADDMANA, SETMANA,
		
		COLOR, RANDOMCOLOR
	}
	
	private command parse(String s) {
		try {
			return command.valueOf(s);
		}
		catch (Exception e) {
			return command.ERROR;
		}
	}
}