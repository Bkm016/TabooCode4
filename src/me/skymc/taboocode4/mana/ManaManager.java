package me.skymc.taboocode4.mana;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.skymc.taboocode4.TabooCode4;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.attribute.sub.AdditionalMana;
import me.skymc.taboocode4.attribute.sub.AdditionalManaRegen;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.TitleUtils;

public class ManaManager {
	
	private static HashMap<String, Double> playerdata = new HashMap<>();
	
	private static double RegenPerSeconds;
	private static String ManaString;
	private static double ManaBarSize;
	private static String ManaBar1;
	private static String ManaBar0;
	private static SoundPack sound;
	private static boolean title = false;
	private static String laketitle;
	
	private static BukkitTask runnable;
	
	public static HashMap<String, Double> getPlayerData() {
		return playerdata;
	}
	
	public static SoundPack getLackSound() {
		return sound;
	}
	
	public static boolean isTitle() {
		return title;
	}
	
	public static String getLakeMessage() {
		return laketitle;
	}
	
	public static void startRunnable() {
		
		RegenPerSeconds = TabooCode4.getSettings().getDouble("Settings.SpellSection.RegenPerSeconds");
		ManaString = TabooCode4.getSettings().getString("Settings.SpellSection.ManaBarSettings.String");
		ManaBarSize = TabooCode4.getSettings().getDouble("Settings.SpellSection.ManaBarSettings.Size");
		ManaBar1 = TabooCode4.getSettings().getString("Settings.SpellSection.ManaBarSettings.1");
		ManaBar0 = TabooCode4.getSettings().getString("Settings.SpellSection.ManaBarSettings.0");
		title = TabooCode4.getSettings().getBoolean("Settings.SpellSection.Title");
		laketitle = TabooCode4.getSettings().getString("Settings.SpellSection.LackTitle");
		sound = new SoundPack(TabooCode4.getSettings().getString("Settings.SpellSection.LackSound"));
		
		try {
			runnable.cancel();
		} 
		catch (Exception e) {}
		
		runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				Collection<? extends Player> players = Bukkit.getOnlinePlayers();
				
				for (Player player : players) {
					
					double mana = getPlayerMana(player);
					double manaregen = getManRegen(player);
					double manamax = getMaxMana(player);
					
					if (mana + manaregen > manamax) {
						playerdata.put(player.getName(), manamax);
					}
					else {
						playerdata.put(player.getName(), mana + manaregen);
						
						if (title) {
							TitleUtils.sendTitle(player, "", 10, 30, 10, getManaString(player), 10, 30, 10);
						}
					}
				}
			}
		}.runTaskTimer(TabooCode4.getInst(), 0, TabooCode4.getSettings().getInt("Settings.SpellSection.RegenTick"));
	}
	
	public static double getPlayerMana(Player player) {
		if (playerdata.containsKey(player.getName())) {
			return playerdata.get(player.getName());
		}
		return 0;
	}
	
	public static String getManaString(Player player) {
		
		double mana = getPlayerMana(player);
		double maxmana = getMaxMana(player);
		
		StringBuffer bar = new StringBuffer("");
		for (int i = 0; i < ManaBarSize ; i++) {
			
			if (i < ManaBarSize * (mana/maxmana)) {
				bar.append(ManaBar1);
			}
			else {
				bar.append(ManaBar0);
			}
		}
		
		return ManaString.replace("%bar%", bar.toString()).replace("%mana%", String.valueOf(mana)).replace("%max%", String.valueOf(maxmana));
	}
	
	public static void removeMana(Player player, double mana) {
		if (getPlayerMana(player) - mana < 0) {
			playerdata.put(player.getName(), 0D);
		}
		else {
			playerdata.put(player.getName(), getPlayerMana(player) - mana);
		}
	}
	
	public static double getManRegen(Player player) {
		double mana =  RegenPerSeconds + AttributesManager.getRegularValue(player, AdditionalManaRegen.class.getSimpleName());
		
		return mana + (mana * AttributesManager.getPercentValue(player, AdditionalManaRegen.class.getSimpleName()));
	}
	
	public static double getMaxMana(Player player) {
		double mana =  TabooCode4.getDefaultMana() + AttributesManager.getRegularValue(player, AdditionalMana.class.getSimpleName());
		
		return mana + (mana * AttributesManager.getPercentValue(player, AdditionalMana.class.getSimpleName()));
	}
}