package me.skymc.taboocode4.attribute.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.SpecialAttribute.DamageType;
import me.skymc.taboocode4.attribute.SpecialAttributeData;
import me.skymc.taboocode4.attribute.sub.AttackSpeed;
import me.skymc.taboocode4.condition.NormalCondition;
import me.skymc.taboocode4.condition.sub.OwnderRequest;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboorunes.manager.RunesExecutor;
import ru.endlesscode.rpginventory.RPGInventory;
import ru.endlesscode.rpginventory.inventory.InventoryManager;
import ru.endlesscode.rpginventory.inventory.PlayerWrapper;
import ru.endlesscode.rpginventory.inventory.slot.Slot;
import ru.endlesscode.rpginventory.inventory.slot.SlotManager;

public class AttributesManager {
	
	public static HashMap<String, HashMap<String, SpecialAttributeData>> specialAttributeData = new HashMap<>(); 
	
	private static int checkLine = 0;
	
	private static String mainHand;
	private static HashMap<Integer, String> itemtype = new HashMap<>();
	private static HashMap<String, String> itemtype_rpginv = new HashMap<>();
	
	private static String conditionMessage;
	private static String conditionMessage_hand;
	
	private static boolean userpginv;
	
	private static AttackSpeed attackspeed;
	
	public static String getMain() {
		return mainHand;
	}
	
	public static boolean isRPGInv() {
		return userpginv;
	}
	
	public static HashMap<Integer, String> getItemTypes() {
		return itemtype;
	}
	
	public static HashMap<String, String> getItemTypes_RPGInv() {
		return itemtype_rpginv;
	}
	
	public static Integer getCheckLine() {
		return checkLine;
	}
	
	public static String getConditionMessage() {
		return conditionMessage;
	}
	
	public static String getConditionMessage_hand() {
		return conditionMessage_hand;
	}
	
	public static String getUUID(LivingEntity e) {
		return e.getUniqueId().toString();
	}
	
	public static AttackSpeed getAttackSpeedAddon() {
		return attackspeed;
	}
	
	public static HashMap<String, SpecialAttributeData> getDataMap(LivingEntity entity) {
		return specialAttributeData.get(entity.getUniqueId().toString());
	}
	
	public static Double getPrimaryValue(LivingEntity entity, String name) {
		if (!specialAttributeData.containsKey(getUUID(entity))) {
			return 0D;
		}
		
		HashMap<String, SpecialAttributeData> data = specialAttributeData.get(getUUID(entity));
		if (!data.containsKey(name)) {
			return 0D;
		}
		
		return data.get(name).getNeutralPrimary();
	}
	
	public static Double getRegularValue(LivingEntity entity, String name) {
		if (!specialAttributeData.containsKey(getUUID(entity))) {
			return 0D;
		}
		
		HashMap<String, SpecialAttributeData> data = specialAttributeData.get(getUUID(entity));
		if (!data.containsKey(name)) {
			return 0D;
		}
		
		return data.get(name).getRegular();
	}
	
	public static Double getPercentValue(LivingEntity entity, String name) {
		if (!specialAttributeData.containsKey(getUUID(entity))) {
			return 0D;
		}
		
		HashMap<String, SpecialAttributeData> data = specialAttributeData.get(getUUID(entity));
		if (!data.containsKey(name)) {
			return 0D;
		}
		
		return data.get(name).getPercent()/100;
	}
	
	public static SpecialAttributeData getSpecialAttributeData(LivingEntity entity, String name) {
		if (!specialAttributeData.containsKey(getUUID(entity))) {
			return null;
		}
		
		HashMap<String, SpecialAttributeData> data = specialAttributeData.get(getUUID(entity));
		if (!data.containsKey(name)) {
			return null;
		}
		
		return data.get(name);
	}
	
	public static void addBypass(Player player) {
		player.setMetadata("AttackSpeedBypass", new FixedMetadataValue(TabooCode4.getInst(), true));
	}
	
	public static void removeBypass(Player player) {
		if (player.hasMetadata("AttackSpeedBypass")) {
			player.removeMetadata("AttackSpeedBypass", TabooCode4.getInst());
		}
	}
	
	public static boolean isBypass(Player player) {
		if (player.hasMetadata("AttackSpeedBypass")) {
			return true;
		}
		return false;
	}
	
	public static void update_mob(LivingEntity entity) {
		
		TabooCode4.run(new BukkitRunnable() {
			
			public void run() {
				
				long time = System.currentTimeMillis();
				
				HashMap<String, SpecialAttributeData> data = new HashMap<>();
				
				List<SpecialAttribute> attrs = TabooCode4.getSpecialAttributes();
				List<ItemStack> items = new ArrayList<>();
				
				items.add(entity.getEquipment().getItemInMainHand());
				items.add(entity.getEquipment().getHelmet());
				items.add(entity.getEquipment().getChestplate());
				items.add(entity.getEquipment().getLeggings());
				items.add(entity.getEquipment().getBoots());
				
				for (ItemStack _item : items) {
					
					if (ItemFilter.hasLore(_item)) {
						List<String> lores = _item.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList());
						
						for (SpecialAttribute attr : attrs) {
							String name = attr.getClass().getSimpleName();
							SpecialAttributeData value = attr.getAttribute(entity, _item, lores);
							
							if (value != null) {
								if (data.containsKey(name)) {
									data.put(name, data.get(name).importData(value));
								}
								else {
									data.put(name, value);
								}
							}
						}
					}
				}
				specialAttributeData.put(getUUID(entity), data);
				
				if (TabooCode4.isDebug()) {
					TabooCode4.send("本次属性读取耗时: " + (System.currentTimeMillis() - time) + " 毫秒 (" + entity.getType() + ")");
				}
				
				for (SpecialAttribute attr : attrs) {
					if (attr.getType() == DamageType.UPDATE) {
						attr.execute(entity, null, getDataMap(entity), null, null);
					}
				}
			}
		});
	}
	
	public static void update(Player player) {
		
		TabooCode4.run(new BukkitRunnable() {
			
			public void run() {
				long time = System.currentTimeMillis();
				
				HashMap<String, SpecialAttributeData> data = new HashMap<>();
				
				List<ItemStack> items = new ArrayList<>();
				List<String> alllore = new ArrayList<>();

				ItemStack item = player.getInventory().getItemInMainHand();
				if (ItemFilter.hasLore(item)) {
					
					boolean bypass = false;
					if (TabooCode4.isItemType()) {
						if (!item.getItemMeta().getLore().get(checkLine).contains(mainHand)) {
							bypass = true;
						}
					}
					
					if (!bypass) {
						boolean itemload = true;
						List<String> lores = item.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList());
						String lore = lores.toString();
						
						for (NormalCondition cond : TabooCode4.getNormalConditions()) {
							if (!cond.execute(player, item, lore, lores)) {
								itemload = false;
								
								player.sendMessage(cond.getMessage().replace("%s%", mainHand));
								break;
							}
						}
						
						if (itemload) {
							items.add(item);
							alllore.addAll(lores);
						}
					}
				}
				
				if (!isRPGInv()) {
					Iterator<Integer> i = itemtype.keySet().iterator();
					while (i.hasNext()) {
						Integer slot = i.next();
						ItemStack _item = player.getInventory().getItem(slot);
						
						if (ItemFilter.hasLore(_item)) {
							if (TabooCode4.isItemType()) {
								if (!_item.getItemMeta().getLore().get(checkLine).contains(itemtype.get(slot))) {
									continue;
								}
							}
							boolean itemload = true;
							List<String> lores = _item.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList());
							String lore = lores.toString();
							
							for (NormalCondition cond : TabooCode4.getNormalConditions()) {
								if (!cond.execute(player, _item, lore, lores)) {
									itemload = false;
									
									player.sendMessage(cond.getMessage().replace("%s%", itemtype.get(slot)));
									break;
								}
							}
							if (itemload) {
								items.add(_item);
								alllore.addAll(lores);
							}
						}
					}
				}
				else if (InventoryManager.playerIsLoaded(player)) {
					Inventory inv = InventoryManager.get(player).getInventory();
					
					Iterator<String> i = itemtype_rpginv.keySet().iterator();
					while (i.hasNext()) {
						String slotname = i.next();
						String key = itemtype_rpginv.get(slotname);
						
						Slot slot = SlotManager.instance().getSlot(slotname);
						if (slot == null) {
							continue;
						}
						
						for (Integer _slot : slot.getSlotIds()) {
							
							ItemStack slotitem = inv.getItem(_slot);
							if (!ItemFilter.hasLore(slotitem)) {
								continue;
							}
							
							if (slotitem.equals(slot.getCup())) {
								continue;
							}
							
							if (TabooCode4.isItemType()) {
								if (!slotitem.getItemMeta().getLore().get(checkLine).contains(key)) {
									continue;
								}
							}
							
							boolean itemload = true;
							List<String> lores = slotitem.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList());
							String lore = lores.toString();
							
							for (NormalCondition cond : TabooCode4.getNormalConditions()) {
								if (!cond.execute(player, slotitem, lore, lores)) {
									itemload = false;
									
									player.sendMessage(cond.getMessage().replace("%s%", itemtype.get(slot)));
									break;
								}
							}
							if (itemload) {
								items.add(slotitem);
								alllore.addAll(lores);
							}
						}
					}
				}
				
				for (ItemStack _item : items) {
					List<String> lores = _item.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList());
					
					for (SpecialAttribute attr : TabooCode4.getSpecialAttributes()) {
						String name = attr.getClass().getSimpleName();
						SpecialAttributeData value = attr.getAttribute(player, _item, lores);
						
						if (value != null) {
							if (data.containsKey(name)) {
								data.put(name, data.get(name).importData(value));
							}
							else {
								data.put(name, value);
							}
						}
					}
				}
				specialAttributeData.put(getUUID(player), data);
				
				if (TabooCode4.isDebug()) {
					TabooCode4.send("本次属性读取耗时: " + (System.currentTimeMillis() - time) + " 毫秒 (" + player.getName() + ")");
				}
				
				for (SpecialAttribute attr : TabooCode4.getSpecialAttributes()) {
					if (attr.getType() == DamageType.UPDATE) {
						attr.execute(player, null, getDataMap(player), null, null);
					}
				}
				
				if (Bukkit.getServer().getPluginManager().getPlugin("TabooRunes2") != null) {
					RunesExecutor.updateRunesStats(player, alllore.stream().collect(Collectors.toList()));
				}
			}
		});
	}
	
	public static void clearData() {
		
		if (TabooCode4.isDebug()) TabooCode4.send("开始清理属性缓存数据");
		
		List<UUID> entities = new ArrayList<>();
		try {
			Bukkit.getWorlds().forEach(world -> world.getLivingEntities().forEach(e -> entities.add(e.getUniqueId())));
			if (TabooCode4.isDebug()) TabooCode4.send("实体总量: &f" + entities.size());
		}
		catch (Exception e) {
		}
		
		Iterator<String> uuids = specialAttributeData.keySet().iterator();
		if (TabooCode4.isDebug()) TabooCode4.send("数据总量: &f" + specialAttributeData.size());
		
		int i = 0;
		while (uuids.hasNext()) {
			UUID uuid = UUID.fromString(uuids.next());
			if (!entities.contains(uuid)) {
				uuids.remove();
				i++;
			}
		}
		if (TabooCode4.isDebug()) TabooCode4.send("清理总量: &f" + i);
		if (TabooCode4.isDebug()) TabooCode4.send("属性缓存清理结束");
	}
	
	public static void loadManager() {
		checkLine = TabooCode4.getSettings().getInt("AttributeItemType.CheckLine") - 1;
		mainHand = TabooCode4.getSettings().getString("AttributeItemType.Main");
		conditionMessage = TabooCode4.getSettings().getString("AttributeItemType.ConditionMessage");
		conditionMessage_hand = TabooCode4.getSettings().getString("AttributeItemType.ConditionMessage_hand");
		userpginv = TabooCode4.getSettings().getBoolean("AttributeItemType.UseRPGInv");
		
		if (!userpginv) {
			for (String slot : TabooCode4.getSettings().getConfigurationSection("AttributeItemType.Type").getKeys(false)) {
				String type = TabooCode4.getSettings().getString("AttributeItemType.Type." + slot);
				
				itemtype.put(Integer.valueOf(slot), type);
				if (TabooCode4.isDebug()) TabooCode4.send("注册类型: &f" + type);
			}
		}
			
		else {
			if (Bukkit.getPluginManager().getPlugin("RPGInventory-SkyMaster") != null) {
				for (String slot : TabooCode4.getSettings().getConfigurationSection("AttributeItemType.RPGInventory").getKeys(false)) {
					String type = TabooCode4.getSettings().getString("AttributeItemType.RPGInventory." + slot);
					
					itemtype_rpginv.put(slot, type);
					if (TabooCode4.isDebug()) TabooCode4.send("注册类型: &f" + slot + " §7(RPGInventory)");
				}
			}
			else {
				if (TabooCode4.isDebug()) TabooCode4.send("&4载入类型失败. &7(RPGInventory Not Found!!)");
			}
		}
		
		for (SpecialAttribute attr : TabooCode4.getSpecialAttributes()) {
			if (attr.getClass().getSimpleName().equals(AttackSpeed.class.getSimpleName())) {
				attackspeed = (AttackSpeed) attr;
			}
		}
	}
}