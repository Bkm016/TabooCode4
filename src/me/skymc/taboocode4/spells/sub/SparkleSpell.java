package me.skymc.taboocode4.spells.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.library.EffLib;
import me.skymc.taboocode4.mana.ManaManager;
import me.skymc.taboocode4.spells.NormalSpells;
import me.skymc.taboocode4.type.DefaultParticle;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.ActionUtils;
import me.skymc.taboocode4.utils.TitleUtils;

public class SparkleSpell implements NormalSpells {
	
	public class SpellSettings {
		
		public double damage;
		public double distance;
		public double speed;
		public double range;
		public double penetrate_entity;
		public boolean penetrate_block;
		public boolean hearteffect;
		
		public double mana;
		
		public int length;
		public int limit;
		
		public SoundPack sound;
		public SoundPack sound_hit;
		
		public List<Integer> repeat = new ArrayList<>();
		public List<DefaultParticle> path = new ArrayList<>();
		public List<DefaultParticle> hit = new ArrayList<>();
		public List<DefaultParticle> sparkle = new ArrayList<>();
	}
	
	private FileConfiguration file;
	
	private Pattern pattern;
	private Integer group;
	
	private HashMap<String, SpellSettings> data = new HashMap<>();
	
	@Override
	public boolean register(FileConfiguration conf, boolean first) {
		file = conf;
		if (first) {
			conf.set("Settings.Pattern", "Sparkle Spell: [$][#]");
			
			conf.set("Settings.SpellType.zero.mana", 1);
			conf.set("Settings.SpellType.zero.damage", 0);
			conf.set("Settings.SpellType.zero.distance", 10);
			conf.set("Settings.SpellType.zero.speed", 0.2);
			conf.set("Settings.SpellType.zero.range", 1);
			conf.set("Settings.SpellType.zero.sound", "BLOCK_FIRE_AMBIENT-2-2");
			conf.set("Settings.SpellType.zero.sound_hit", "ENTITY_GENERIC_EXPLODE-3-1");
			conf.set("Settings.SpellType.zero.repeat", "0");
			conf.set("Settings.SpellType.zero.length", 3);
			conf.set("Settings.SpellType.zero.limit", 5);
			conf.set("Settings.SpellType.zero.hearteffect", true);
			conf.set("Settings.SpellType.zero.penetrate.entity", 1);
			conf.set("Settings.SpellType.zero.penetrate.block", false);
			conf.set("Settings.SpellType.zero.particles.path", Arrays.asList("EXPLOSION_NORMAL-0-0-0-0-1"));
			conf.set("Settings.SpellType.zero.particles.hit", Arrays.asList("EXPLOSION_LARGE-0-0-0-0-1"));
			conf.set("Settings.SpellType.zero.particles.sparkle", Arrays.asList("FIREWORKS_SPARK-0.1-0.1-0.1-0.008-3"));
			
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
			spell.range = conf.getDouble("Settings.SpellType." + type + ".range");
			spell.length = conf.getInt("Settings.SpellType." + type + ".length");
			spell.limit = conf.getInt("Settings.SpellType." + type + ".limit");
			spell.hearteffect = conf.getBoolean("Settings.SpellType." + type + ".hearteffect");
			spell.penetrate_entity = conf.getDouble("Settings.SpellType." + type + ".penetrate.entity");
			spell.penetrate_block = conf.getBoolean("Settings.SpellType." + type + ".penetrate.block");
			spell.sound = new SoundPack(conf.getString("Settings.SpellType." + type + ".sound"));
			spell.sound_hit = new SoundPack(conf.getString("Settings.SpellType." + type + ".sound_hit"));
			
			try {
				Arrays.asList(conf.getString("Settings.SpellType." + type + ".repeat").split("-")).forEach(x -> spell.repeat.add(Integer.valueOf(x)));
				
				for (String part : conf.getStringList("Settings.SpellType." + type + ".particles.path")) {
					
					spell.path.add(new DefaultParticle(
							EffLib.valueOf(part.split("-")[0]), 
							Float.valueOf(part.split("-")[1]), 
							Float.valueOf(part.split("-")[2]), 
							Float.valueOf(part.split("-")[3]), 
							Integer.valueOf(part.split("-")[4]), 
							Integer.valueOf(part.split("-")[5])));
				}
				for (String part : conf.getStringList("Settings.SpellType." + type + ".particles.hit")) {
					
					spell.hit.add(new DefaultParticle(
							EffLib.valueOf(part.split("-")[0]), 
							Float.valueOf(part.split("-")[1]), 
							Float.valueOf(part.split("-")[2]), 
							Float.valueOf(part.split("-")[3]), 
							Integer.valueOf(part.split("-")[4]), 
							Integer.valueOf(part.split("-")[5])));
				}
				
				for (String part : conf.getStringList("Settings.SpellType." + type + ".particles.sparkle")) {
					
					spell.sparkle.add(new DefaultParticle(
							EffLib.valueOf(part.split("-")[0]), 
							Float.valueOf(part.split("-")[1]), 
							Float.valueOf(part.split("-")[2]), 
							Float.valueOf(part.split("-")[3]), 
							Integer.valueOf(part.split("-")[4]), 
							Integer.valueOf(part.split("-")[5])));
				}
			} catch (Exception e) {
				
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
		                	
							if (spell.hearteffect) {
		                		player.playEffect(EntityEffect.HURT);
		                	}
							
							double penetrate_entity = spell.penetrate_entity;
							double limit = spell.limit;
							
					        for (double n = 0.0; n < spell.distance; ++n) {
					        	Location add = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(n * spell.speed));
					            
					            if (!add.getBlock().getType().equals(Material.AIR) && !spell.penetrate_block) {
		                        	return;
		                        }
			                    
			                    for (DefaultParticle part : spell.path) {
			                    	part.effect.display(part.a, part.b, part.c, part.d, part.amount, add, 50);
			                    }
					            
			                    for (LivingEntity entity : add.getWorld().getLivingEntities()) {
			                        if (entity.getLocation().add(0.0, 1.0, 0.0).distance(add) < spell.range && entity != player) {
			                        	
			                        	AttributesManager.addBypass(player);
					                	((Damageable) entity).damage(spell.damage, player);
					                	AttributesManager.addBypass(player);
					                	 
					                	for (DefaultParticle part : spell.hit) {
					                    	part.effect.display(part.a, part.b, part.c, part.d, part.amount, add, 50);
					                    }
					                	
					                    for (Entity entity2 : entity.getNearbyEntities(spell.length, spell.length, spell.length)) {
					                    	
					                        if (!(entity2 instanceof LivingEntity) || entity2 == player) {
					                            continue;
					                        }
					                        if (limit-- <= 0) {
					                            return;
					                        }
					                        
					                        AttributesManager.addBypass(player);
						                	((Damageable) entity2).damage(spell.damage, player);
						                	AttributesManager.addBypass(player);
					                        
						                	for (DefaultParticle part : spell.hit) {
						                    	part.effect.display(part.a, part.b, part.c, part.d, part.amount, add, 50);
						                    }		
						                	
					                        Location add2 = entity.getLocation().add(0.0, 0.75, 0.0);
					                        Location add3 = entity2.getLocation().add(0.0, 0.75, 0.0);
					                        
					                        for (double n3 = 0.0; n3 < 1.0; n3 += 0.2) {
					                        	for (DefaultParticle part : spell.sparkle) {
							                    	part.effect.display(part.a, part.b, part.c, part.d, part.amount, add2.clone().add(add3.toVector().subtract(add2.toVector()).multiply(n3)), 50);
							                    }	
					                        }
					                    }
					                    if (penetrate_entity-- <= 0) {
		                                	return;
		                                }
					                }
					            }
					        }
						}
					}.runTaskLater(TabooCode4.getInst(), repeattick += Integer.valueOf(spell.repeat.get(i)));
				}
			}
		}
	}

}