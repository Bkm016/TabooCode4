package me.skymc.taboocode4.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.identifie.IdentifieManager;
import me.skymc.taboocode4.identifie.IdentifiePack;
import me.skymc.taboocode4.manager.ItemManager;

public class Identifie implements CommandExecutor, Listener {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if (!(sender instanceof Player)) {
			TabooCode4.send(sender, "该指令不能由后台输入");
			return false;
		}
		
		Player player = (Player) sender;
		
		if (!cooldown.containsKey(player.getUniqueId().toString())) {
			cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
		}
		else {
			if (System.currentTimeMillis() - cooldown.get(player.getUniqueId().toString()) <= 6000) {
				player.sendMessage(IdentifieManager.getConf().getString("Messages.Cooldown"));
				return false;
			}
			else {
				cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
			}
		}
		
		Inventory inv = Bukkit.createInventory(null, 9, TabooCode4.getIdeTitle(false));
		
		inv.setItem(8, TabooCode4.getButtenOff());
		
		((Player) sender).openInventory(inv);
		return false;
	}
	
	@EventHandler
	public void inventory(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equals(TabooCode4.getIdeTitle(true))) {
			e.setCancelled(true);
			return;
		}
		if (e.getInventory().getTitle().equals(TabooCode4.getIdeTitle(false))) {
			Player player = (Player) e.getWhoClicked();
			
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			if (e.getCurrentItem().getType().equals(Material.AIR)) {
				return;
			}
			
			if (e.getRawSlot() > 8) {
				
				if (getContents(e.getInventory()).size() >= 8) {
					return;
				}
				else {
					e.getInventory().addItem(e.getCurrentItem());
					e.setCurrentItem(null);
					
					e.getInventory().setItem(8, TabooCode4.getButtenOn());
				}
			}
			else if (e.getRawSlot() < 8) {
				player.getInventory().addItem(e.getCurrentItem());
				e.setCurrentItem(null);
				
				if (getContents(e.getInventory()).size() == 0) {
					e.getInventory().setItem(8, TabooCode4.getButtenOff());
				}
			}
			
			if (e.getCurrentItem().equals(TabooCode4.getButtenOn())) {
				ItemStack item = new ItemStack(Material.BARRIER);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§f");
				item.setItemMeta(meta);
				
				// CLOSE BUTTEN && STAET IDENTIFIE
				if (startIdentifie(player, e.getInventory()).size() > 0) {
					
					e.getInventory().setItem(8, item);
					setTitle(player, e.getInventory(), TabooCode4.getIdeTitle(true));
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							
							if (player.isOnline() && player.getOpenInventory().getTitle().equals(TabooCode4.getIdeTitle(true))) {
								player.getOpenInventory().setItem(8, TabooCode4.getButtenOn());
								setTitle(player, player.getOpenInventory().getTopInventory(), TabooCode4.getIdeTitle(false));
							}
						}
					}.runTaskLater(TabooCode4.getInst(), 20*3);
				}
			}
		}
	}
	
	private void setTitle(Player player, Inventory inv, String title) {
		Inventory _inv = Bukkit.createInventory(null, 9, title);
		_inv.setContents(inv.getContents());
		
		player.openInventory(_inv);
	}
	
	private HashMap<String, Long> cooldown = new HashMap<>();
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		cooldown.remove(e.getPlayer().getUniqueId().toString());
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		
		if (!e.getInventory().getTitle().equals(TabooCode4.getIdeTitle(false))) {
			return;
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!e.getPlayer().getOpenInventory().getTitle().equals(TabooCode4.getIdeTitle(true))) {
					
					List<ItemStack> item = getContents(e.getInventory());
					for (ItemStack _item : item) {
						e.getPlayer().getInventory().addItem(_item);
					}
					
					e.getInventory().clear();
				}
			}
		}.runTask(TabooCode4.getInst());
	}
	
	private List<ItemStack> startIdentifie(Player player, Inventory inv) {
		
		List<ItemStack> finishitems = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			
			if (!canIdentifie(inv.getItem(i))) {
				continue;
			}
			
			IdentifiePack pack = IdentifieManager.getidentifiePack(inv.getItem(i));
			if (pack == null) {
				continue;
			}
			
			finishitems.add(ItemManager.getFinishItem(pack.getItem()));
			List<Material> materials = IdentifieManager.getDisplayMaterials(pack.getDisplay());
			
			inv.setItem(i, null);
			
			int slot = i;
			new BukkitRunnable() {
				int time = 0;
				int _slot = slot;
				boolean toInv = false;
				
				@Override
				public void run() {
					if (!player.isOnline()) {
						cancel();
						return;
					}
					
					int index = TabooCode4.getRandom().nextInt(materials.size() - 1);
					
					if (!player.getOpenInventory().getTitle().equals(TabooCode4.getIdeTitle(true)) && finishitems.size() > 0) {
						toInv = true;
					}
					else {
						player.getOpenInventory().setItem(_slot, new ItemStack(materials.get(index)));
					}
					
					pack.getSound().play(player);
					
					time++;
					if (time > 29) {
						if (toInv) {
							player.getInventory().addItem(ItemManager.getFinishItem(pack.getItem()));
						}
						else {
							player.getOpenInventory().setItem(_slot, ItemManager.getFinishItem(pack.getItem()));
						}
						cancel();
						return;
					}
				}
			}.runTaskTimer(TabooCode4.getInst(), 0, 2);
		}
		return finishitems;
	}
	
	private boolean canIdentifie(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getType().equals(Material.AIR)) {
			return false;
		}
		if (!item.hasItemMeta()) {
			return false;
		}
		if (!item.getItemMeta().hasDisplayName()) {
			return false;
		}
		return true;
	}
	
	private List<ItemStack> getContents(Inventory inv) {
		List<ItemStack> list = new ArrayList<>();
		
		for (int i = 0; i < 8 && inv.getItem(i) != null; i++) {
			list.add(inv.getItem(i));
		}
		
		return list;
	}

}