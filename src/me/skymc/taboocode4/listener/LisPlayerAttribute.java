package me.skymc.taboocode4.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.skymc.taboocode4.attribute.manager.AttributesManager;

public class LisPlayerAttribute implements Listener {
	
	@EventHandler
	public void xpbounds(PlayerExpChangeEvent e) {
		
		Double value = AttributesManager.getPercentValue(e.getPlayer(), "XPBounds");
		int value2 = (int) (e.getAmount() + (e.getAmount() * value));
		
		if (e.getAmount() >= 0 && value2 >= 0) {
			e.setAmount(value2);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void damage(EntityDamageByEntityEvent e) {
		Player entity = null;
		
		if (e.getDamager() instanceof Projectile) {
			if (((Projectile) e.getDamager()).getShooter() == null) {
				return;
			}
			
			if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
				entity = (Player) ((Projectile) e.getDamager()).getShooter();
			}
		}
		else if (e.getDamager() instanceof Player) {
			entity = (Player) e.getDamager();
		}
		
		if (e.getEntity() instanceof Player && !entity.getWorld().getPVP()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void regen(EntityRegainHealthEvent e) {
		
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		Double value = AttributesManager.getRegularValue((LivingEntity) e.getEntity(), "AdditionalRegen");
		Double value2 = e.getAmount() + (e.getAmount() * value);
		
		if (e.getAmount() >= 0 && value2 >= 0) {
			e.setAmount(Math.floor(value2));
		}
		
		Double durafix = AttributesManager.getRegularValue((LivingEntity) e.getEntity(), "DurabilityFix");
		if (durafix > 0 && e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			
			Inventory inv = player.getInventory();
			ItemStack item = player.getInventory().getItemInMainHand();
			
			if (!item.getItemMeta().spigot().isUnbreakable() && item.getType().getMaxDurability() > 0) 
			{
				if (item.getDurability() >= durafix)
				{
					item.setDurability((short) (item.getDurability() - durafix));
				}
				else
				{
					item.setDurability((short) 0);
				}
			}
			
			int[] i = new int[] { 36, 37, 38, 39, 40 };
			for (int slot : i) 
			{
				ItemStack _item = inv.getItem(slot);
				if (_item != null)
				{
					if (!_item.getItemMeta().spigot().isUnbreakable() && _item.getType().getMaxDurability() > 0)
					{
						if (_item.getDurability() >= durafix)
						{
							_item.setDurability((short) (_item.getDurability() - durafix));
						}
						else
						{
							_item.setDurability((short) 0);
						}
					}
				}
			}
		}
	}

}