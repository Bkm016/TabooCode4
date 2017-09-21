package me.skymc.taboocode4.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.utility.MinecraftReflection;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.utils.CopyUtils;

public class ItemManager {
	
	private static LinkedHashMap<String, ItemStack> item = new LinkedHashMap<>();
	private static LinkedHashMap<String, ItemStack> item_dir = new LinkedHashMap<>();
	
	private static Pattern randomInteger = Pattern.compile("(\\{i:(?:\\d+)-(?:\\d+)\\}|\\{d:(?:(?:\\-|\\+)?\\d+(?:\\.\\d+)?)-(?:(?:\\-|\\+)?\\d+(?:\\.\\d+)?)\\})");
	
	private static int getRandomInteger(Integer ll, Integer uu) {
		return TabooCode4.getRandom().nextInt(uu) % (uu-ll + 1) + ll;
	}
	
	private static Double getRandomDouble(Double ll, Double uu) {
		return TabooCode4.getFormatedDouble(ll + TabooCode4.getRandom().nextDouble() * (uu - ll));
	}
	
	public static HashMap<String, ItemStack> getItems() {
		return item;
	}
	
	public static HashMap<String, ItemStack> getItemsDir() {
		return item_dir;
	}
	
	public static HashMap<String, ItemStack> getItem() {
		return item;
	}
	public static HashMap<String, ItemStack> getItemDir() {
		return item_dir;
	}
	
	public static ItemStack getItem(String name) {
		if (item_dir.containsKey(name)) {
			return item_dir.get(name).clone();
		}
		else if (item.containsKey(name)) {
			return item.get(name).clone();
		}
		return null;
	}
	
	public static ItemStack getFinishItem(String name) {
		ItemStack i = null;
		
		if (item_dir.containsKey(name)) {
			i = item_dir.get(name).clone();
		}
		else if (item.containsKey(name)) {
			i = item.get(name).clone();
		}
		
		if (i != null) {
			ItemMeta meta = i.getItemMeta();
			if (ItemFilter.hasLore(i)) {
				meta.setLore(formatRandom(meta.getLore()));
			}
			i.setItemMeta(meta);
		}
		return i;
	}
	
	public static boolean setItem(String name, ItemStack i) {
		
		if (!item_dir.containsKey(name)) {
			item.put(name, i);
			return true;
		}
		return false;
	}

	public static void importConf(boolean debug) {
		File file = new File(TabooCode4.getInst().getDataFolder(), "item.yml");
		if (!file.exists()) {
			if (debug) TabooCode4.send("物品库不存在无需导入");
			return;
		}
		
		item.clear();
		
		File itemdir = new File(TabooCode4.getInst().getDataFolder(), "ItemsDirectory");
		if (!itemdir.exists()) {
			itemdir.mkdir();
		}
		
		for (File _file : itemdir.listFiles()) {
			FileConfiguration _conf = YamlConfiguration.loadConfiguration(_file);
			for (String name : _conf.getConfigurationSection("items").getKeys(false)) {
				item_dir.put(name, _conf.getItemStack("items." + name));
			}
		}
		
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if (conf.get("items") == null) {
			if (debug) TabooCode4.send("物品库不存在无需导入");
			return;
		}
		for (String name : conf.getConfigurationSection("items").getKeys(false)) {
			if (!item_dir.containsKey(name)) {
				item.put(name, conf.getItemStack("items." + name));
			}
			else {
				if (debug) TabooCode4.send("物品库中的 " + name + " 与固定物品库重复");
			}
		}
	}
	
	public static void exportConf(boolean debug) {
		
		File file = new File(TabooCode4.getInst().getDataFolder(), "item.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				if (debug) TabooCode4.send("物品库文件创建失败");
			}
		}
		
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		conf.set("items", null);
		
		Iterator<String> i = item.keySet().iterator();
		while (i.hasNext()) {
			String name = i.next();
			conf.set("items." + name, item.get(name));
		}
		
		if (TabooCode4.isBackup()) {
			File backupdir = new File(TabooCode4.getInst().getDataFolder(), "ItemsBackup");
			if (!backupdir.exists()) {
				backupdir.mkdir();
			}
			
			File backupfile = new File(backupdir, TabooCode4.getDateFormat().format(System.currentTimeMillis()) + ".yml");
			try {
				CopyUtils.Copy(file, backupfile);
				
				if (debug) TabooCode4.send("物品库备份成功: &f" + backupfile.getName());
			} catch (IOException e1) {
				if (debug) TabooCode4.send("物品库备份失败");
			}
		}
		
		try {
			conf.save(file);
		} catch (IOException e) {
			if (debug) TabooCode4.send("物品库导出失败");
		}
	}
	
	public static List<String> formatRandom(List<String> lore) {
		
		for (int i = 0; i < lore.size() ; i++) {
			lore.set(i, formatRandom(lore.get(i)));
		}
		return lore;
	}
	
	public static String formatRandom(String value) {
		
		Matcher m = randomInteger.matcher(value);
		
		while (m.find()) {
			String key[] = m.group(1).substring(3).substring(0, m.group(1).length() - 4).split("-");
			
			if (m.group(1).contains("{i")) {
				value = value.replace(m.group(1), String.valueOf(getRandomInteger(Integer.valueOf(key[0]), Integer.valueOf(key[1]))));
			}
			else if (m.group(1).contains("{d")) {
				value = value.replace(m.group(1), String.valueOf(getRandomDouble(Double.valueOf(key[0]), Double.valueOf(key[1]))));
			}
			
			m = randomInteger.matcher(value);
		}
		
		return value;
	}

}