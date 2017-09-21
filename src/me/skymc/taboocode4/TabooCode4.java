package me.skymc.taboocode4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import me.skymc.taboocode4.attribute.SpecialAttribute;
import me.skymc.taboocode4.attribute.manager.AttributesManager;
import me.skymc.taboocode4.attribute.skillapi.SkillAPIExpGain;
import me.skymc.taboocode4.attribute.skillapi.SkillAPIManaGain;
import me.skymc.taboocode4.attribute.skillapi.SkillAPIManaLoss;
import me.skymc.taboocode4.attribute.skillapi.SkillAPISkillDamageDamage;
import me.skymc.taboocode4.attribute.skillapi.SkillAPISkillDamageDefense;
import me.skymc.taboocode4.attribute.skillapi.SkillAPISkillHeal;
import me.skymc.taboocode4.attribute.sub.NeutralDamage;
import me.skymc.taboocode4.attribute.sub.NeutralDefense;
import me.skymc.taboocode4.attribute.sub.PlayerDamage;
import me.skymc.taboocode4.attribute.sub.PlayerDefense;
import me.skymc.taboocode4.attribute.sub.ProjectileDefense;
import me.skymc.taboocode4.attribute.sub.PhysicalDamage;
import me.skymc.taboocode4.attribute.sub.PhysicalDefense;
import me.skymc.taboocode4.attribute.sub.TrueDamage;
import me.skymc.taboocode4.attribute.sub.XPBounds;
import me.skymc.taboocode4.bstats.Metrics;
import me.skymc.taboocode4.attribute.sub.AttackSpeed;
import me.skymc.taboocode4.attribute.sub.BalanceDefense;
import me.skymc.taboocode4.attribute.sub.CritChance;
import me.skymc.taboocode4.attribute.sub.CritDamage;
import me.skymc.taboocode4.attribute.sub.CritDefense;
import me.skymc.taboocode4.attribute.sub.DodgeChance;
import me.skymc.taboocode4.attribute.sub.DurabilityFix;
import me.skymc.taboocode4.attribute.sub.LifeSteal;
import me.skymc.taboocode4.attribute.sub.SpellsDamage;
import me.skymc.taboocode4.attribute.sub.SpellsDefense;
import me.skymc.taboocode4.attribute.sub.AdditionalHealth;
import me.skymc.taboocode4.attribute.sub.AdditionalMana;
import me.skymc.taboocode4.attribute.sub.AdditionalManaRegen;
import me.skymc.taboocode4.attribute.sub.AdditionalManaRelief;
import me.skymc.taboocode4.attribute.sub.AdditionalRegen;
import me.skymc.taboocode4.attribute.sub.MonsterDamage;
import me.skymc.taboocode4.attribute.sub.MonsterDefense;
import me.skymc.taboocode4.attribute.sub.AdditionalSpeed;
import me.skymc.taboocode4.command.Identifie;
import me.skymc.taboocode4.command.MainCommand;
import me.skymc.taboocode4.condition.NormalCondition;
import me.skymc.taboocode4.condition.skillapi.SkillAPIAttributeRequest;
import me.skymc.taboocode4.condition.skillapi.SkillAPIClassLevelRequest;
import me.skymc.taboocode4.condition.skillapi.SkillAPIManaRequest;
import me.skymc.taboocode4.condition.skillapi.SkillAPIMaxManaRequest;
import me.skymc.taboocode4.condition.skillapi.SkillAPISkillLevelRequest;
import me.skymc.taboocode4.condition.sub.DurabilityRequest;
import me.skymc.taboocode4.condition.sub.FoodRequest;
import me.skymc.taboocode4.condition.sub.HealthRequest;
import me.skymc.taboocode4.condition.sub.LevelRequest;
import me.skymc.taboocode4.condition.sub.OffHandRequest;
import me.skymc.taboocode4.condition.sub.OwnderRequest;
import me.skymc.taboocode4.condition.sub.PermissionRequest;
import me.skymc.taboocode4.condition.sub.PlaceHolderRequest;
import me.skymc.taboocode4.events.TabooCodeLoadSuccess;
import me.skymc.taboocode4.identifie.IdentifieManager;
import me.skymc.taboocode4.library.WorkTask;
import me.skymc.taboocode4.listener.LisEntityDeath;
import me.skymc.taboocode4.listener.LisEntityEquip;
import me.skymc.taboocode4.listener.LisInventoryClose;
import me.skymc.taboocode4.listener.LisItemDamage;
import me.skymc.taboocode4.listener.LisPlayerAttribute;
import me.skymc.taboocode4.listener.LisPlayerClick;
import me.skymc.taboocode4.listener.LisPlayerDamage;
import me.skymc.taboocode4.listener.LisPlayerFood;
import me.skymc.taboocode4.listener.LisPlayerHealth;
import me.skymc.taboocode4.listener.LisPlayerItemHeld;
import me.skymc.taboocode4.listener.LisPlayerJoinAndQuit;
import me.skymc.taboocode4.listener.LisPlayerLevel;
import me.skymc.taboocode4.listener.LisPlayerPickup;
import me.skymc.taboocode4.listener.LisPlayerSwapHand;
import me.skymc.taboocode4.mana.ManaManager;
import me.skymc.taboocode4.manager.ItemManager;
import me.skymc.taboocode4.spells.NormalSpells;
import me.skymc.taboocode4.spells.sub.FireboltSpell;
import me.skymc.taboocode4.spells.sub.ProjectileSpell;
import me.skymc.taboocode4.spells.sub.RedStoneSpell;
import me.skymc.taboocode4.spells.sub.SparkleSpell;
import me.skymc.taboocode4.type.SoundPack;
import me.skymc.taboocode4.utils.ReflectionUtils;

public class TabooCode4 extends JavaPlugin implements Listener
{
	private static final String prefix = "§7§l[§8§lTabooCode4§7§l] §7";
	
	private static List<String> disableworlds = new ArrayList<>();
	
	private static String version = null;
	private static String identitleon = null;
	private static String identitleoff = null;
	private static String disableattack = null;
	
	private static Plugin plugin = null;
	
	private static WorkTask worktask = null;
	
	private static Random random = new Random();
	
	private static DecimalFormat decimalformat = new DecimalFormat("0.00");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	private static Integer threadamount = 1;
	private static Integer stayticks = 0;
	
	private static Integer basehealth = 20;
	private static Integer minhealth = 1;
	
	private static File attributeFile = null;
	private static File conditionFile = null;
	private static File spellsFile = null;
	
	private static boolean Started = false;
	private static boolean isAsyn = false;
	private static boolean isHolo = false;
	private static boolean isDebug = false;
	private static boolean isBackup = false;
	private static boolean isItemType = false;
	
	private static double bottom = 1D;
	private static double basespeed = 0.2D;
	private static double defaultmana = 10D;
	
	private static SoundPack DeathSound;
	
	private static ItemStack buttenon;
	private static ItemStack buttenoff;
	
	private static LinkedList<SpecialAttribute> SpecialAttributes = new LinkedList<>();
	private static LinkedList<NormalCondition> normalconditions = new LinkedList<>();
	private static LinkedList<NormalSpells> normalspells = new LinkedList<>();

	private static HashMap<String, Hologram> holodata = new HashMap<>();
	private static HashMap<String, String> holospecies = new HashMap<>();
	
	public static Plugin getInst() {
		return plugin;
	}
	
	public static String getPrefix() {
		return prefix;
	}
	
	public static FileConfiguration getSettings() {
		return plugin.getConfig();
	}
	
	public static WorkTask getWorkTask() {
		return worktask;
	}
	
	public static Integer getTaskAmount() {
		return threadamount;
	}
	
	public static boolean isAsyn() {
		return isAsyn;
	}
	
	public static boolean isHolo() {
		return isHolo;
	}
	
	public static HashMap<String, Hologram> getHoloData() {
		return holodata;
	}
	
	public static List<String> getDisableWorlds() {
		return disableworlds;
	}
	
	public static String getBukkitVersion() {
		return version;
	}
	
	public static File getAttributeFile() {
		return attributeFile;
	}
	
	public static File getConditionFile() {
		return conditionFile;
	}
	
	public static File getSpellsFile() {
		return spellsFile;
	}
	
	public static List<SpecialAttribute> getSpecialAttributes() {
		return SpecialAttributes;
	}
	
	public static List<NormalCondition> getNormalConditions() {
		return normalconditions;
	}
	
	public static List<NormalSpells> getNormalSpells() {
		return normalspells;
	}
	
	public static boolean isServerStarted() {
		return Started;
	}
	
	public static double getBottomDamage() {
		return bottom;
	}
	
	public static HashMap<String, Hologram> getHolodisplayData() {
		return holodata;
	}
	
	public static Integer getHoloStayTicks() {
		return stayticks;
	}
	
	public static Random getRandom() {
		return random;
	}
	
	public static boolean isDebug() {
		return isDebug;
	}
	
	public static SoundPack getKilledSound() {
		return DeathSound;
	}
	
	public static Integer getBaseHealth() {
		return basehealth;
	}
	
	public static Double getBaseSpeed() {
		return basespeed;
	}
	
	public static Integer getMinimumHealth() {
		return minhealth;
	}
	
	public static SimpleDateFormat getDateFormat() {
		return sdf;
	}
	
	public static boolean isBackup() {
		return isBackup;
	}
	
	public static boolean isItemType() {
		return isItemType;
	}
	
	public static ItemStack getButtenOn() {
		return buttenon;
	}
	
	public static ItemStack getButtenOff() {
		return buttenoff;
	}
	
	public static String getDisableAttack() {
		return disableattack;
	}
	
	public static double getDefaultMana() {
		return defaultmana;
	}
	
	public static String getIdeTitle(boolean on) {
		if (on) return identitleon;
		return identitleoff;
	}
	
	public static Double getFormatedDouble(Double d) {
		return Double.valueOf(decimalformat.format(d));
	}
	
	public static void send(Player player, String msg) {
		player.sendMessage(prefix + msg.replace("&", "§"));
	}
	
	public static void send(CommandSender console, String msg) {
		console.sendMessage(prefix + msg.replace("&", "§"));
	}
	
	public static void send(String msg) {
		Bukkit.getConsoleSender().sendMessage(prefix + msg.replace("&", "§"));
	}
	
	public static String getHoloSpeciesString(String type) {
		if (holospecies.containsKey(type)) {
			return holospecies.get(type);
		}
		return holospecies.get("default");
	}
	
	public static void run(Runnable runnable) {
		if (isAsyn) {
			worktask.execute(runnable);
		}
		else {
			runnable.run();
		}
	}
	
	public static void loadConfig() {
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		disableworlds = plugin.getConfig().getStringList("Settings.DisableWorlds");
		bottom = plugin.getConfig().getDouble("Settings.DamageSection.Bottom");
		basespeed = plugin.getConfig().getDouble("Settings.SpeedSection.Default");
		isDebug = plugin.getConfig().getBoolean("Settings.DebugMode");
		isBackup = plugin.getConfig().getBoolean("Settings.ItemBackup");
		isHolo = plugin.getConfig().getBoolean("DamageTitleHoloDisplay.Enable");
		isItemType = plugin.getConfig().getBoolean("AttributeItemType.Enable");
		disableattack = plugin.getConfig().getString("Settings.SpellSection.DisableAttack");
		basehealth = plugin.getConfig().getInt("Settings.HealthSection.Default");
		minhealth = plugin.getConfig().getInt("Settings.HealthSection.Minimum");
		buttenon = plugin.getConfig().getItemStack("Settings.IdentifieSection.ItemData.ButtenOn");
		buttenoff = plugin.getConfig().getItemStack("Settings.IdentifieSection.ItemData.ButtenOff");
		identitleon = plugin.getConfig().getString("Settings.IdentifieSection.TitleData.TitleOn");
		identitleoff = plugin.getConfig().getString("Settings.IdentifieSection.TitleData.TitleOff");
		defaultmana = plugin.getConfig().getDouble("Settings.SpellSection.Default");
		DeathSound = new SoundPack(plugin.getConfig().getString("Settings.SoundSection.EntityKilled"));
		
		send("禁用世界: &f" + disableworlds);
		if (isHolo && plugin.getServer().getPluginManager().getPlugin("HolographicDisplays") == null) {
			isHolo = false;
		}
		else {
			for (String type : plugin.getConfig().getConfigurationSection("DamageTitleHoloDisplay.HoloSpecies").getKeys(false)) {
				holospecies.put(type, plugin.getConfig().getString("DamageTitleHoloDisplay.HoloSpecies." + type));
			}
			stayticks = plugin.getConfig().getInt("DamageTitleHoloDisplay.StayTicks");
		}
	}
	
	private void loadCommands() {
		getServer().getPluginCommand("taboocode4").setExecutor(new MainCommand());
		getServer().getPluginCommand("identifie").setExecutor(new Identifie());
	}
	
	private void loadRegisters() {
		getServer().getPluginManager().registerEvents(this, plugin);
		getServer().getPluginManager().registerEvents(new LisPlayerDamage(), plugin);
		getServer().getPluginManager().registerEvents(new LisPlayerJoinAndQuit(), plugin);
		getServer().getPluginManager().registerEvents(new LisEntityDeath(), plugin);
		getServer().getPluginManager().registerEvents(new LisPlayerAttribute(), plugin);
		getServer().getPluginManager().registerEvents(new LisPlayerClick(), plugin);
		getServer().getPluginManager().registerEvents(new Identifie(), plugin);
		
		if (getConfig().getBoolean("AttributeUpdateEnableList.ItemHeld")) {
			getServer().getPluginManager().registerEvents(new LisPlayerItemHeld(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.SwapHand")) {
			getServer().getPluginManager().registerEvents(new LisPlayerSwapHand(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.InventoryClose")) {
			getServer().getPluginManager().registerEvents(new LisInventoryClose(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.ItemDamage")) {
			getServer().getPluginManager().registerEvents(new LisItemDamage(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.LevelChange")) {
			getServer().getPluginManager().registerEvents(new LisPlayerLevel(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.HealthChange")) {
			getServer().getPluginManager().registerEvents(new LisPlayerHealth(), plugin);
		}
		if (getConfig().getBoolean("AttributeUpdateEnableList.FoodLevelChange")) {
			getServer().getPluginManager().registerEvents(new LisPlayerFood(), plugin);
		}
		if (getConfig().getBoolean("Settings.AttributeSection.EntityCheck")) {
			getServer().getPluginManager().registerEvents(new LisEntityEquip(), plugin);
		}
		if (getConfig().getBoolean("Settings.ConditionSection.PickupOwnder")) {
			getServer().getPluginManager().registerEvents(new LisPlayerPickup(), plugin);
		}
	}
	
	//TODO REGISTER ATTRIBUTES & CONDITIONS
	public static void registerSpecialAttribute(SpecialAttribute attr) {
		if (Started) {
			send("&4无法在插件运行过程中注册新的属性 &7(&f" + attr.getClass().getSimpleName() + "&7)");
			return;
		}
		SpecialAttributes.add(attr);
	}
	public static void registerNormalCondition(NormalCondition cond) {
		if (Started) {
			send("&4无法在插件运行过程中注册新的条件 &7(&f" + cond.getClass().getSimpleName() + "&7)");
			return;
		}
		normalconditions.add(cond);
	}
	public static void registerNormalSpell(NormalSpells spell) {
		if (Started) {
			send("&4无法在插件运行过程中注册新的魔法 &7(&f" + spell.getClass().getSimpleName() + "&7)");
			return;
		}
		normalspells.add(spell);
	}
	
	public static void updatePriority() {
		SpecialAttributes.sort((a, b) -> Integer.valueOf(a.getPriority()).compareTo(Integer.valueOf(b.getPriority())));
		normalconditions.sort((a, b) -> Integer.valueOf(a.getPriority()).compareTo(Integer.valueOf(b.getPriority())));
	}
	
	private void loadDefault() {
		registerSpecialAttribute(new NeutralDamage());
		registerSpecialAttribute(new NeutralDefense());
		registerSpecialAttribute(new SpellsDamage());
		registerSpecialAttribute(new SpellsDefense());
		registerSpecialAttribute(new PhysicalDamage());
		registerSpecialAttribute(new PhysicalDefense());
		registerSpecialAttribute(new MonsterDamage());
		registerSpecialAttribute(new MonsterDefense());
		registerSpecialAttribute(new PlayerDamage());
		registerSpecialAttribute(new PlayerDefense());
		registerSpecialAttribute(new CritChance());
		registerSpecialAttribute(new CritDamage());
		registerSpecialAttribute(new CritDefense());
		registerSpecialAttribute(new DodgeChance());
		registerSpecialAttribute(new ProjectileDefense());
		registerSpecialAttribute(new BalanceDefense());
		registerSpecialAttribute(new AdditionalRegen());
		registerSpecialAttribute(new AdditionalMana());
		registerSpecialAttribute(new AdditionalManaRegen());
		registerSpecialAttribute(new AdditionalManaRelief());
		registerSpecialAttribute(new AttackSpeed());
		registerSpecialAttribute(new XPBounds());
		registerSpecialAttribute(new LifeSteal());
		registerSpecialAttribute(new TrueDamage());
		registerSpecialAttribute(new DurabilityFix());
		registerSpecialAttribute(new DurabilityFix());
		registerSpecialAttribute(new SkillAPIManaGain());
		registerSpecialAttribute(new SkillAPIManaLoss());
		registerSpecialAttribute(new SkillAPIExpGain());
		registerSpecialAttribute(new SkillAPIManaLoss());
		registerSpecialAttribute(new SkillAPISkillHeal());
		registerSpecialAttribute(new SkillAPISkillDamageDamage());
		registerSpecialAttribute(new SkillAPISkillDamageDefense());
		
		registerNormalCondition(new LevelRequest());
		registerNormalCondition(new FoodRequest());
		registerNormalCondition(new HealthRequest());
		registerNormalCondition(new DurabilityRequest());
		registerNormalCondition(new PlaceHolderRequest());
		registerNormalCondition(new PermissionRequest());
		registerNormalCondition(new OwnderRequest());
		registerNormalCondition(new OffHandRequest());
		registerNormalCondition(new SkillAPIAttributeRequest());
		registerNormalCondition(new SkillAPIClassLevelRequest());
		registerNormalCondition(new SkillAPIManaRequest());
		registerNormalCondition(new SkillAPIMaxManaRequest());
		registerNormalCondition(new SkillAPISkillLevelRequest());
		
		registerNormalSpell(new RedStoneSpell());
		registerNormalSpell(new ProjectileSpell());
		registerNormalSpell(new FireboltSpell());
		registerNormalSpell(new SparkleSpell());
		
		if (getConfig().getBoolean("Settings.HealthSection.Enable")) {
			registerSpecialAttribute(new AdditionalHealth());
		}
		if (getConfig().getBoolean("Settings.SpeedSection.Enable")) {
			registerSpecialAttribute(new AdditionalSpeed());
		}
	}
	
	//TODO PLUGIN LOAD
	public void onLoad() {
		plugin = this; Started = false; version = ReflectionUtils.getVersion();
	}
	
	//TODO PLUGIN ENABLE
	public void on_Enable() { 
		new Metrics(this);
		
		loadConfig(); loadDefault(); loadRegisters(); loadCommands();
		
		send("----------------------------------");
		send("禁忌法典已载入 &8(" + getDescription().getVersion() + ")");
		send("作者: &f坏黑");
		send("邮箱: &fSky@Asgard.me");
		send("----------------------------------");
		send("异步运算: &f" + ((isAsyn = getConfig().getBoolean("Settings.Asynchronous.Enable")) ? "&a启用" : "&c禁用"));
		
		if (isAsyn) {
			worktask = new WorkTask(threadamount = getConfig().getInt("Settings.Asynchronous.Thread"));
			send("线程数量: &f" + threadamount);
		}
		
		send("----------------------------------");
		
		attributeFile = new File(getDataFolder(), "Attributes");
		if (!attributeFile.exists()) {
			attributeFile.mkdirs();
		}
		
		conditionFile = new File(getDataFolder(), "Conditions");
		if (!conditionFile.exists()) {
			conditionFile.mkdirs();
		}
		
		spellsFile = new File(getDataFolder(), "Spells");
		if (!spellsFile.exists()) {
			spellsFile.mkdirs();
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Started = true;
				
				TabooCode4Loader.loadAttributes();
				AttributesManager.loadManager();
				IdentifieManager.loadIdentifiePacks();
				ItemManager.importConf(false);
				ManaManager.startRunnable();
				LisPlayerPickup.load();
				
				updatePriority(); 
				
				if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
					new PlaceholderHook((TabooCode4) plugin).hook();
				}
				
				Bukkit.getPluginManager().callEvent(new TabooCodeLoadSuccess());
			}
		}.runTask(this);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				AttributesManager.clearData();
			}
		}.runTaskTimerAsynchronously(this, 0, 20*60*5);
	}
	
	//TODO PLUGIN DISABLE
	public void onDisable() {
		if (isAsyn) {
			worktask.stop();
		}
		
		for (Hologram holo : holodata.values()) {
			if (!holo.isDeleted()) {
				holo.delete();
			}
		}
		
		send("----------------------------------");
		send("禁忌法典已卸载 &8(" + getDescription().getVersion() + ")");
		send("作者: &f坏黑");
		send("邮箱: &fSky@Asgard.me");
		send("----------------------------------");
		
		Bukkit.getWorlds().forEach(world -> world.getLivingEntities().forEach(e -> { if (e instanceof Creature) {((Creature) e).setTarget(null); }}));
		
		ItemManager.exportConf(false);
	}
}
