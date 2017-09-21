package me.skymc.taboocode4.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.mana.ManaManager;
import me.skymc.taboocode4.manager.ItemFilter;
import me.skymc.taboocode4.spells.NormalSpells;
import me.skymc.taboocode4.utils.TitleUtils;
import ru.endlesscode.rpginventory.inventory.InventoryManager;
import ru.endlesscode.rpginventory.inventory.slot.Slot;
import ru.endlesscode.rpginventory.inventory.slot.SlotManager;
import ru.endlesscode.rpginventory.misc.Config;

public class LisPlayerClick implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void a(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			ItemStack item = ((Player) e.getDamager()).getInventory().getItemInMainHand();
			
			if (!ItemFilter.hasLore(item)) {
				return;
			}
			
			if (item.getItemMeta().getLore().toString().contains(TabooCode4.getDisableAttack())) {
				if (!AttributesManager.isBypass((Player) e.getDamager())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			if (!ItemFilter.hasLore(e.getItem())) {
				return;
			}
			
			if (e.getItem().getItemMeta().getLore().toString().contains(TabooCode4.getDisableAttack())) {
				e.setCancelled(true);
			}
		}
		
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getHand() != EquipmentSlot.OFF_HAND) {
			
			ItemStack item = e.getItem();
			if (!ItemFilter.hasLore(item)) {
				return;
			}
			
			long time = System.currentTimeMillis();
			
			if (AttributesManager.isRPGInv()) {
				PlayerInventory inventory = e.getPlayer().getInventory();
				boolean forceWeapon = Config.getConfig().getBoolean("attack.force-weapon");
		        boolean requireWeapon = Config.getConfig().getBoolean("attack.require-weapon");
		        
				if ((forceWeapon || requireWeapon) && SlotManager.instance().getSlot(inventory.getHeldItemSlot(), InventoryType.SlotType.QUICKBAR) == null) {
		            List<Slot> activeSlots = SlotManager.instance().getActiveSlots();
		            if (activeSlots.size() != 0) {
		                if (forceWeapon) {
		                    for (Slot activeSlot : activeSlots) {
		                        if (!InventoryManager.isQuickEmptySlot(inventory.getItem(activeSlot.getQuickSlot()))) {
		                            inventory.setHeldItemSlot(activeSlot.getQuickSlot());
		                            break;
		                        }
		                    }
		                }
		                if (requireWeapon) {
		                    e.setCancelled(true);
		                    return;
		                }
		            }
		        }
			}
			
			if (AttributesManager.getAttackSpeedAddon().canAttack(e.getPlayer())) {
				String lore = item.getItemMeta().getLore().stream().map(x -> ChatColor.stripColor(x)).collect(Collectors.toList()).toString();

				for (NormalSpells spell : TabooCode4.getNormalSpells()) {
					spell.execute(e.getPlayer(), item, lore);
				}
			}
			if (TabooCode4.isDebug()) {
				TabooCode4.send("本次魔法释放耗时: " + (System.currentTimeMillis() - time) + " 毫秒");
			}
		}
	}

}