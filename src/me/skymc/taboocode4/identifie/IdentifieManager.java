package me.skymc.taboocode4.identifie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.type.WeightCategory;

public class IdentifieManager {
	
	private static FileConfiguration conf;
	private static HashMap<String, List<Material>> displaypack = new HashMap<>();
	private static HashMap<String, IdentifiePack> identifiepack = new HashMap<>();
	
	public static IdentifiePack getidentifiePack(ItemStack item) {
		for (IdentifiePack pack : identifiepack.values()) {
			if (item.getItemMeta().getDisplayName().equals(pack.getName())) {
				return pack;
			}
		}
		return null;
	}
	
	public static FileConfiguration getConf() {
		return conf;
	}
	
	public static List<Material> getDisplayMaterials(String id) {
		return displaypack.get(id);
	}
	
	public static void loadIdentifiePacks() {
		
		TabooCode4.getInst().saveResource("identifie.yml", false);
		File file = new File(TabooCode4.getInst().getDataFolder(), "identifie.yml");
		if (file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		conf = YamlConfiguration.loadConfiguration(file);
		if (conf.get("DisplaySection") != null) {
			for (String id : conf.getConfigurationSection("DisplaySection").getKeys(false)) {
				
				List<Material> list = new ArrayList<>();
				for (String material : conf.getStringList("DisplaySection." + id)) {
					try {
						list.add(Material.valueOf(material));
					}
					catch (Exception e) {
						if (TabooCode4.isDebug()) TabooCode4.send("閿欒鐨勬潗璐�: 搂4" + material);
					}
				}
				
				displaypack.put(id, list);
			}
		}
		if (conf.get("IdentifieSection") != null) {
			for (String id : conf.getConfigurationSection("IdentifieSection").getKeys(false)) {
				
				String name = conf.getString("IdentifieSection." + id + ".name");
				String display = conf.getString("IdentifieSection." + id + ".display");
				if (!displaypack.containsKey(display)) {
					if (TabooCode4.isDebug()) TabooCode4.send("閿欒鐨勫睍绀�: 搂4" + display);
					continue;
				}
				
				List<WeightCategory> list = new ArrayList<>();
				for (String item : conf.getStringList("IdentifieSection." + id + ".item")) {
					WeightCategory wei = new WeightCategory(item.split(" ")[1], Integer.valueOf(item.split(" ")[0]));
					
					list.add(wei);
				}
				
				SoundPack sound = new SoundPack(conf.getString("IdentifieSection." + id + ".sound"));
				identifiepack.put(id, new IdentifiePack(id, name, display, sound, list));
			}
 		}
	}

}