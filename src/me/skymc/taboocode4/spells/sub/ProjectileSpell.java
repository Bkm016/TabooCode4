package me.skymc.taboocode4.spells.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.EntityEffect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.mana.ManaManager;
import me.skymc.taboocode4.spells.NormalSpells;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.ActionUtils;
import me.skymc.taboocode4.utils.TitleUtils;

public class ProjectileSpell implements NormalSpells {
	
	public class SpellSettings {
		
		public double damage;
		public double distance;
		public double speed;
		public boolean hearteffect;
		
		public double mana;
		
		public EntityType type;
		public List<Integer> repeat = new ArrayList<>();
		public SoundPack sound;
	}
	
	private FileConfiguration file;
	
	private Pattern pattern;
	private Integer group;
	
	private HashMap<String, SpellSettings> data = new HashMap<>();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "Projectile Spell: [$][#]");
			
			conf.set("Settings.SpellType.zero.mana", 1);
			conf.set("Settings.SpellType.zero.damage", 0);
			conf.set("Settings.SpellType.zero.distance", 10);
			conf.set("Settings.SpellType.zero.speed", 0.2);
			conf.set("Settings.SpellType.zero.sound", "PLAYER_LEVEL_UP-2-2");
			conf.set("Settings.SpellType.zero.hearteffect", true);
			conf.set("Settings.SpellType.zero.repeat", "0-5-5-5");
			conf.set("Settings.SpellType.zero.projectile", "SHULKER_BULLET");
			
			conf.set("Settings.GroupLocation.SpellType", 1);
		}

		pattern = Pattern.compile(conf.getString("Settings.Pattern").replace("[$]", "(\\S+)").replace("[?]", "(?:\\s|\\S)").replace("[#]", "(?:,|])"));
		group = conf.getInt("Settings.GroupLocation.SpellType");
		
		for (String type : conf.getConfigurationSection("Settings.SpellType").getKeys(false)) {
			SpellSettings spell = new SpellSettings();
			
			spell.mana = conf.getDouble("Settings.SpellType." + type + ".mana");
			spell.damage = conf.getDouble("Settings.SpellType." + type + ".damage");
			spell.distance = conf.getDouble("Settings.SpellType." + type + ".distance");
			spell.speed = conf.getDouble("Settings.SpellType." + type + ".speed");
			spell.hearteffect = conf.getBoolean("Settings.SpellType." + type + ".hearteffect");
			spell.sound = new SoundPack(conf.getString("Settings.SpellType." + type + ".sound"));
			
			try {
				Arrays.asList(conf.getString("Settings.SpellType." + type + ".repeat").split("-")).forEach(x -> spell.repeat.add(Integer.valueOf(x)));

				spell.type = EntityType.valueOf(conf.getString("Settings.SpellType." + type + ".projectile"));
			} catch (Exception e) {
				spell.type = EntityType.SHULKER_BULLET;
			}
			
			data.put(type, spell);
		}
		return true;
	}
	
	@Override
	public void execute(Player player, ItemStack item, String uncolouredlore) {

		Matcher m = pattern.matcher(uncolouredlore);
		if (m.find()) {
			if (data.containsKey(m.group(group))) {
				
				SpellSettings spell = data.get(m.group(group));
				
				double manaconsume = spell.mana + AttributesManager.getRegularValue(player, "AdditionalManaRelief");
				double manaconsume2 = manaconsume + (manaconsume * AttributesManager.getPercentValue(player, "AdditionalManaRelief"));
				
				if (ManaManager.getPlayerMana(player) < manaconsume2) {
					ManaManager.getLackSound().play(player);

					if (ManaManager.isTitle()) {
						TitleUtils.sendTitle(player, "", 10, 30, 10, ManaManager.getLakeMessage(), 10, 30, 10);
					}
					return;
				}
				else {
					ManaManager.removeMana(player, manaconsume2);
					
					if (ManaManager.isTitle()) {
						TitleUtils.sendTitle(player, "", 10, 30, 10, ManaManager.getManaString(player), 10, 30, 10);
					}
				}
				
				int repeattick = 0;
				for (int i = 0; i < spell.repeat.size() ; i++) {
					
					new BukkitRunnable() {
						
						public void run() {
							spell.sound.play(player);
		                	
		                	Vector direction = player.getEyeLocation().getDirection();
			                Projectile Projectile = (Projectile) player.getWorld().spawnEntity(player.getEyeLocation(), spell.type);
			                Projectile.setShooter(player);
			                Projectile.setMetadata("spelldamage", new FixedMetadataValue(TabooCode4.getInst(), spell.damage));
							
			                if (spell.hearteffect) {
		                		player.playEffect(EntityEffect.HURT);
		                	}
							new BukkitRunnable() {
								
				                double distance = spell.distance;
				                double speed = spell.speed;
				                
				                double startdistance = 0;
				                public void run() {
				                	startdistance += speed;
				                	
				                	if (Projectile.isDead() || startdistance >= distance / 4.0) {
				                		Projectile.remove();
				                		cancel();
				                	}
				                	Projectile.setVelocity(direction);
				                }
				            }.runTaskTimer(TabooCode4.getInst(), 0L, 1L);
						}
					}.runTaskLater(TabooCode4.getInst(), repeattick += Integer.valueOf(spell.repeat.get(i)));
				}
			}
		}
	}

}